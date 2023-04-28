package uk.gov.moj.sdt.test.utils;

import org.apache.commons.lang.time.DateUtils;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class DBUnitUtilityBean.
 *
 * @author Peter Bonnett.
 */
@Component
public class DBUnitUtilityBean {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DBUnitUtilityBean.class);

    @Inject
    protected DataSource datasource;

    /**
     * Map of database connections for all schemas (one each).
     */
    private static Map<String, IDatabaseConnection> allConnections = new HashMap<>();

    DBUnitUtilityBean() {
        LOGGER.debug("DBUnitUtilityBean - Instantiated");
    }

    /**
     * Loads the database from a given file.
     *
     * @param classToLoadFor Which class is requesting data
     * @param reloadRefdata  the reload refdata
     */
    public void loadDatabase(final Class<?> classToLoadFor, final boolean reloadRefdata) {
        // Can have individual XML for a method, or just one for a class
        final String fileXml = "/" + classToLoadFor.getName().replace(".", "/") + ".xml";
        if (this.getClass().getResource(fileXml) != null) {
            LOGGER.info("Loading DB with: {}", fileXml);
            loadDatabase("public", fileXml, reloadRefdata);
            LOGGER.info("Loaded database");
        } else {
            LOGGER.error("Cannot see file {} to load DB with ", fileXml);

        }
    }

    /**
     * Loads the database from a given file.
     *
     * @param targetSchema  Schema to load
     * @param filePath      Path to file
     * @param reloadRefdata the reload refdata
     */
    public void loadDatabase(final String targetSchema, final String filePath, final boolean reloadRefdata) {
        loadDatabase(targetSchema, filePath, reloadRefdata, true);
    }

    /**
     * Loads the database from a given file.
     *
     * @param targetSchema    Schema to load
     * @param filePath        Path to file
     * @param reloadRefdata   the reload refdata
     * @param cleanseDatabase clean database?
     */
    public void loadDatabase(final String targetSchema, final String filePath, final boolean reloadRefdata,
                                    final boolean cleanseDatabase) {

        try {
            // database connection
            LOGGER.info("Starting load for schema[{}], filePath[{}], reloadRefdata[{}]",
                    targetSchema, filePath, reloadRefdata);
            final IDatabaseConnection dbConnection = getConnectionInstance(targetSchema);

            // Create a DBUnit Dataset builder using the target schemas DTD
            final FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
            builder.setDtdMetadata(true);
            builder.setMetaDataSetFromDtd(this.getClass().getResourceAsStream("/DTD/" + targetSchema + ".dtd"));
            builder.setColumnSensing(true);

            // Generate the clean dataset, if loadRefData is true use
            // clean_all.xml otherwise use clean.xml
            final String cleanDataPath = "/clean/" + targetSchema + "_clean_minus_refdata" + ".xml";
            LOGGER.info("Reading clean dataset for data path={}", cleanDataPath);
            final IDataSet cleanDataset = builder.build(this.getClass().getResourceAsStream(cleanDataPath));

            final IDataSet dtdDataSet =
                    new FlatDtdDataSet(this.getClass().getResourceAsStream("/DTD/" + targetSchema + ".dtd"));
            final CompositeDataSet compositeDataSet = new CompositeDataSet(dtdDataSet, cleanDataset);

            // prepare for DBUnit load - Disable triggers & constraints
            prepareForDbunitLoad(targetSchema);

            if (cleanseDatabase) {
                // Clean the DB
                LOGGER.info("Cleaning database");
                DatabaseOperation.TRUNCATE_TABLE.execute(dbConnection, compositeDataSet);
                dbConnection.getConnection().commit();
            }

            // If there's reference data insert it
            final String refDataPath = "/refdata/" + targetSchema + "_refdata.xml";
            final InputStream refDataStream = this.getClass().getResourceAsStream(refDataPath);
            if (refDataStream != null && reloadRefdata) {
                LOGGER.info("Inserting reference data from {}", refDataPath);
                final IDataSet referenceDataset = builder.build(refDataStream);
                DatabaseOperation.INSERT.execute(dbConnection, referenceDataset);
                dbConnection.getConnection().commit();
            }

            // Insert the test data
            LOGGER.info("Inserting data from filePath={}", filePath);
            final IDataSet targetDataset = buildTargetDataset(builder, filePath);

            DatabaseOperation.INSERT.execute(dbConnection, targetDataset);

            // Enable triggers & constraints
            finishDbunitLoad(targetSchema);

        } catch (final DatabaseUnitException|IOException|SQLException e) {
            LOGGER.error("Error loading data for Schema '{}': {}", targetSchema, e);
            e.printStackTrace();
        }
    }

    /**
     * Build DBUnit dataset. The dataset replaces placeholders with dynamic values. This is especially useful for date
     * fields when date needs to be relative to current system date.
     *
     * @param builder  dbunit builder.
     * @param filePath file containing test data.
     * @return DBUnit dataset.
     * @throws DataSetException in case of any dataset related errors.
     */
    private IDataSet buildTargetDataset(final FlatXmlDataSetBuilder builder, final String filePath)
            throws DataSetException {

        final IDataSet targetDataset = builder.build(this.getClass().getResourceAsStream(filePath));
        final ReplacementDataSet replacementDataSet = new ReplacementDataSet(targetDataset);
        replacementDataSet.addReplacementObject("[yesterday_date]", DateUtils.addDays(new Date(), -1));

        return replacementDataSet;
    }

    /**
     * Method to disable all constraints and triggers for specific schema prior
     * to loading data via DBUNIT for a specific integration test.
     *
     * @param schemaName name of the schema in which constraints and triggers are to be
     *                   disabled
     */
    public void prepareForDbunitLoad(final String schemaName) {
        try {
            LOGGER.info("Disabling triggers and constraints for schema = {}", schemaName);

            // Get the connection already setup for DBUNIT.
            final Connection connection = getConnectionInstance(schemaName).getConnection();

            // Call the reset stored procedure which should have been loaded into
            // the database by ANT script.
            final CallableStatement cs = connection.prepareCall("CALL PREPARE_FOR_DBUNIT_LOAD(?)");
            cs.setString(1, schemaName);

            // Close result set and callable statement.
            final ResultSet rs = cs.executeQuery();
            if (rs != null) {
                rs.close();
            }
            cs.close();

        } catch (final Exception e) {
            LOGGER.error(e.toString());
        }
    }

    /**
     * Method to enable all constraints and triggers for specific schema prior
     * to loading data via DBUNIT for a specific integration test.
     *
     * @param schemaName name of the schema in which constraints and triggers are to be
     *                   enabled
     */
    public void finishDbunitLoad(final String schemaName) {
        try {
            LOGGER.info("Enabling triggers and constraints for schema = {}", schemaName);

            // Get the connection already setup for DBUNIT.
            final IDatabaseConnection dbConnection = getConnectionInstance(schemaName);
            final Connection connection = dbConnection.getConnection();

            // Call the reset stored procedure which should have been loaded into the database by ANT script.
            final CallableStatement cs =  connection.prepareCall("CALL FINISH_DBUNIT_LOAD(?)");
            cs.setString(1, schemaName);

            // Close result set and calleable statement.
            final ResultSet rs = cs.executeQuery();
            if (rs != null) {
                rs.close();
            }
            cs.close();
        } catch (final SQLException e) {
            LOGGER.error(e.toString());
        }
    }

    /**
     * Internal method to create a DBUnit connection to the DB defined in
     * dbunit.properties.
     *
     * @param schema Target Schema
     * @return DBUnit connection to database
     */
    private IDatabaseConnection getConnectionInstance(final String schema) {
        IDatabaseConnection dbConnection;

        synchronized (allConnections) {
            // Find the connection for this schema.
            dbConnection = allConnections.get(schema);

            // If no connection found for this schema, create one.
            if (null == dbConnection) {
                try {

                    final Connection connection = datasource.getConnection();
                    dbConnection = new DatabaseConnection(connection, schema);
                    dbConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
                            new PostgresqlDataTypeFactory());

                    dbConnection.getConnection().setAutoCommit(false);

                    LOGGER.info("connection: {}", dbConnection.getConfig());

                    // Cache the connection for this schema.
                    allConnections.put(schema, dbConnection);
                } catch (final DatabaseUnitException|SQLException e) {
                    LOGGER.error("Unable to initialise DBUnit Connection: " + e);
                }
            }
        }

        return dbConnection;
    }

}
