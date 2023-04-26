package uk.gov.moj.sdt.test.utils;

import org.apache.commons.lang.time.DateUtils;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.filter.ExcludeTableFilter;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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
     * Generates a DBUnit DTD for the given schema.
     *
     * @param targetSchema Schema required.
     */
    public void generateDTD(final String targetSchema) {

        try {
            LOGGER.debug("Generating DTD for schema {}", targetSchema);
            // database connection
            final IDatabaseConnection dbConnection = getConnectionInstance(targetSchema);
            final File theFile = new File("src/integ-test/resources/DTD/" + targetSchema + ".dtd");
            theFile.createNewFile();
            // write DTD file
            FlatDtdDataSet.write(dbConnection.createDataSet(), new FileOutputStream(theFile));
            LOGGER.debug("Generated DTD for schema {} at {}", targetSchema, theFile.getAbsolutePath());
        } catch (final NullPointerException e) {
            LOGGER.error("Error with db connection for schema: {} : {}", targetSchema, e);
        } catch (final DatabaseUnitException|IOException|SQLException e) {
            LOGGER.error("Error generating DTD for Schema '{}': {}", targetSchema, e);
        }
    }

    /**
     * Exports the database for the target schema.
     *
     * @param targetSchema   schema to use
     * @param file           File to save to
     * @param includeRefdata Should we export the refdata?
     */
    public void exportDatabase(final String targetSchema, final String file, final boolean includeRefdata) {

        try {
            // database connection
            LOGGER.debug("Exporting data for schema {}", targetSchema);
            final IDatabaseConnection dbConnection = getConnectionInstance(targetSchema);

            // full database export
            ITableFilter filter = new ExcludeTableFilter();
            // If there's reference data exclude those tables
            final InputStream refDataStream =
                   this.getClass().getResourceAsStream("/refdata/" + targetSchema + "_refdata.xml");
            if (!includeRefdata && refDataStream != null) {
                // Create a DBUnit Dataset builder using the target schemas DTD
                final FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
                builder.setDtdMetadata(true);
                builder.setMetaDataSetFromDtd(
                        this.getClass().getResourceAsStream("/DTD/" + targetSchema + ".dtd"));
                builder.setColumnSensing(true);
                final IDataSet referenceDataset = builder.build(refDataStream);
                filter = new ExcludeTableFilter(referenceDataset.getTableNames());
            }
            final IDataSet dataSet = new FilteredDataSet(filter,  dbConnection.createDataSet());
            FlatXmlDataSet.write(dataSet, new FileOutputStream(file));
            LOGGER.debug("Exported data for schema {}", targetSchema);
        } catch (final DatabaseUnitException|IOException|SQLException e) {
            LOGGER.error("Error exporting data for Schema '{}': {}", targetSchema, e);
            e.printStackTrace();
        }
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
 //       replacementDataSet.addReplacementObject("[yesterday_date]", DateUtils.addDays(new Date(), -1));
        replacementDataSet.addReplacementObject("[yesterday_date]", LocalDate.now().minusDays(1));

        return replacementDataSet;
    }

    /**
     * Close connection statement for specific schema.
     *
     * @param schema the schema for which the connection is to be closed.
     */
    @SuppressWarnings("unused")
    private synchronized void closeConnection(final String schema) {
        synchronized (allConnections) {
            // Get the connection to be closed.
            final IDatabaseConnection dbConnection = allConnections.get(schema);

            // If it was found, close it.
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                } catch (final SQLException e) {
                    LOGGER.error("Error closing connection for Schema '{}': {}",
                            dbConnection.getSchema(), e);
                }

                // Remove it from the map.
                allConnections.remove(schema);
            }
        }
    }

    /**
     * Drop primary keys.
     */
    public void dropPrimaryKeys() {
        LOGGER.info("Dropping primary keys");
        runSQLFile("./../utils/src/integ-test/resources/init/drop_primary_keys.sql");
    }

    /**
     * Re-enable primary keys.
     */
    public void enablePrimaryKeys() {
        LOGGER.info("Enabling primary keys");
        runSQLFile("./../utils/src/integ-test/resources/init/create_primary_keys.sql");

    }

    /**
     * Run sql files.
     *
     * @param fileName the name of the file to run.
     */
    private void runSQLFile(final String fileName) {
        try {
            LOGGER.info("Running {}", fileName);

            // Get the connection already setup for DBUNIT.
            final IDatabaseConnection dbConnection = getConnectionInstance("PUBLIC");
            final Connection connection = dbConnection.getConnection();
            connection.prepareStatement("SET CONSTRAINTS ALL DEFERRED").execute();

            final FileInputStream inputStream = new FileInputStream(fileName);

            final StringBuilder stringBuilder = new StringBuilder();
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            final BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (null != line) {
                stringBuilder.append(' ');
                stringBuilder.append(line);
                line = reader.readLine();
            }
            final String sqlString = stringBuilder.toString();
            final Statement stmt = connection.createStatement();

            // Execute the query
            stmt.executeUpdate(sqlString);

            // Close the result set, statement and the connection
            stmt.close();

        } catch (final SQLException e) {
            LOGGER.error("Failure to drop constraints", e);
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
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
            // dropPrimaryKeys();
            // Get the connection already setup for DBUNIT.
            final Connection connection = getConnectionInstance(schemaName).getConnection();
            // connection.prepareStatement ("SET CONSTRAINTS ALL DEFERRED").execute ();

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
        // enablePrimaryKeys ();
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
     * Clean database.
     *
     * @param targetSchema  the target schema
     * @param reloadRefdata the reload refdata
     */
    public void cleanDatabase(final String targetSchema, final boolean reloadRefdata) {
        try {
            // database connection
            LOGGER.debug("Loading data for schema {}", targetSchema);
            final IDatabaseConnection dbConnection = getConnectionInstance(targetSchema);

            // Create a DBUnit Dataset builder using the target schemas DTD
            final FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
            builder.setDtdMetadata(true);
            builder.setMetaDataSetFromDtd(this.getClass().getResourceAsStream("/DTD/" + targetSchema + ".dtd"));
            builder.setColumnSensing(true);

            // Generate the clean dataset, if loadRefData is true use
            // clean_all.xml otherwise use clean.xml
            final String cleanDataPath = "/clean/" + targetSchema + "_clean" + (reloadRefdata ? "_all" : "") + ".xml";
            final IDataSet cleanDataset = builder.build(this.getClass().getResourceAsStream(cleanDataPath));

            // Disable triggers & constraints
            prepareForDbunitLoad(targetSchema);

            // Clean the DB
            LOGGER.debug("Cleaning database");
            DatabaseOperation.TRUNCATE_TABLE.execute(dbConnection, cleanDataset);
            dbConnection.getConnection().commit();

            // If there's reference data insert it
            // InputStream refDataStream =
            // this.getClass().getResourceAsStream("/refdata/" +
            // targetSchema +
            // "_refdata.xml");
            // if (refDataStream != null && reloadRefdata)
            // {
            // LOGGER.debug("Inserting reference data");
            // IDataSet referenceDataset = builder.build(refDataStream);
            // DatabaseOperation.INSERT.execute(connection, referenceDataset);
            // connection.getConnection().commit();
            // }

            // Enable triggers & constraints
            finishDbunitLoad(targetSchema);
        } catch (final DatabaseUnitException|IOException|SQLException e) {
            LOGGER.error("Error loading data for Schema '{}': {}", targetSchema, e);
            e.printStackTrace();
        }
    }

    /**
     * Disables/enables all triggers.
     *
     * @param schema  DB Schema
     * @param enabled are the triggers to be enabled or disabled?
     * @throws SQLException the sQL exception
     */
    public void updateTriggers(final String schema, final boolean enabled) throws SQLException {
        updateTriggers(getConnectionInstance(schema).getConnection(), schema, enabled);
    }

    /**
     * Disables/enables all triggers.
     *
     * @param dbConn  DB Connection
     * @param schema  DB Schema
     * @param enabled are the triggers to be enabled or disabled?
     * @throws SQLException the sQL exception
     */
    private void updateTriggers(final Connection dbConn, final String schema, final boolean enabled)
            throws SQLException {
        LOGGER.debug("Editing Audit triggers, enabled: {}", enabled);
        PreparedStatement s = null;
        ResultSet rs = null;
        final String modifier = enabled ? "ENABLE" : "DISABLE";
        String queryToExecute = "SELECT 'ALTER TRIGGER ' || owner || '.' || trigger_name || ' " + modifier +
                "' FROM all_triggers WHERE owner = ?";
        try {
            s = dbConn.prepareStatement(queryToExecute);
            s.setString(1, schema);
            rs = s.executeQuery();
            while (rs.next()) {
                queryToExecute = rs.getString(1);
                final Statement s2 = dbConn.createStatement();
                LOGGER.debug("Executing: {}", queryToExecute);
                s2.execute(queryToExecute);
                s2.close();
            }
        } catch (final SQLException e) {
            LOGGER.error("Error updating database triggers for Schema '{}', Query: {}, Error: {}",
                    schema,  queryToExecute, e);
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (s != null) {
                s.close();
            }
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

    /**
     * Get a standard connection instance.
     *
     * @param schemaName the name of schema
     * @return a standard connection instance.
     */
    public Connection getStandardConnection(final String schemaName) {
        try {
            return getConnectionInstance(schemaName).getConnection();
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
