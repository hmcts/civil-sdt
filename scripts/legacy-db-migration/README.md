# Legacy Database Migration Scripts

This directory contains the scripts to be used to migrate from the legacy SDT Oracle database to the new Postgres database and validate the migration.  They have been added to this repository for reference purposes.

The scripts are contained in the following directories:

* export
* import
* validation

The following sections describe the contents of each directory.

## export

Contains the scripts used to extract the data from the legacy SDT Oracle database.  These export the data in the form of CSV files.

## import

Contains the import and reset_sequences scripts.

The import script used to import the data into the new Postgres database from the export CSV files.

The reset_sequences script is used to reset the sequences used to generate the primary keys on the new Postgres database.  The script is to be run after the import and sets the current value of the sequences to be one greater than the highest primary key value in the corresponding table.

## validation

Contains the scripts used to validate the migration of the data.

The scripts produce CSV files containing details of the tables from the Oracle and Postgres databases.

The Oracle script is to be run prior to the data being exported from the legacy SDT Oracle database.  The Postgres script is to be run after the data has been imported into the new Postgres database.  The two sets of output files will be compared to ensure that the import has completed successfully.
