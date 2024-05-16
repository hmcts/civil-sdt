# Legacy Database Migration Scripts

This directory contains the scripts to be used to migrate from the legacy SDT Oracle database to the new Postgres database and validate the migration.  They have been added to this repository for reference purposes.

## Overview

The migration process uses [ora2pg](https://ora2pg.darold.net/index.html) to export the data from the source Oracle database.  The data is exported into files in the `data` directory which are then imported into the target Postgres database.

Separate scripts are used to export the error log id values for individual requests and the latest sequence values.

Validation scripts are used to check the contents of the source and target databases to ensure that the data has been migrated correctly.

The export process was developed using [release 24.3](https://github.com/darold/ora2pg/releases/tag/v24.3) of ora2pg.

## Prerequisites

ora2pg and an Oracle client, such as [Oracle Instant Client](https://www.oracle.com/uk/database/technologies/instant-client/downloads.html) must be installed.

ora2pg can be installed as a [Perl module](https://ora2pg.darold.net/documentation.html#Installing-Ora2Pg) or a [Docker container](https://hub.docker.com/r/georgmoser/ora2pg).  If installed as a Perl module then the required DBI, DBD and Time::HiRes modules must be also installed - see ora2pg installation instructions.

The Oracle client must be compatible with the Oracle server version.

## Scripts

| Script                                          | Description                                                                                                                                                |
|-------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ora2pg.conf                                     | The ora2pg configuration file.                                                                                                                             |
| export_individual_requests_error_log_id.sql     | Produces a script used to populate the new error_log_id column on the individual_requests table in the target database with data from the source database. |
| export_sequence_values.sql                      | Produces a script used to update the sequences in the target database with the values from the source database.                                            |
| import.sql                                      | Imports the exported data into the target database.                                                                                                        |
| revert/revert_target_db.sql                     | Deletes imported data from target database and resets sequences.                                                                                           |
| revert/revert_target_db_check.sql               | Check revert of target database.                                                                                                                           |
| validation/data_migration_validation_oracle.sql | Produces a set of pre-migration audit files from the source database.                                                                                      |
| validation/data_migration_validation.sql        | Produces a set of post-migration audit files from the target database.                                                                                     |

The revert scripts are used to revert the changes to the target database should the need arise to do so.

## ora2pg Configuration

`ora2pg.conf` is the ora2pg configuration file.  The settings in it control the export process.

The following settings have been changed from their default values:

| Setting          | New Value                                                                                                  | Description                                                      |
|------------------|------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------|
| ORACLE_HOME      | C:\oracle\instantclient_19_22                                                                              | The location of the Oracle libraries.                            |
| TYPE             | COPY                                                                                                       | The type of export.  COPY exports table data.                    |
| ALLOW            | BULK_CUSTOMERS BULK_CUSTOMER_APPLICATIONS SERVICE_REQUESTS BULK_SUBMISSIONS INDIVIDUAL_REQUESTS ERROR_LOGS | The list of tables from which data should be exported.           |
| DISABLE_SEQUENCE | 1                                                                                                          | Disable update of sequences on target database during migration. |
| OUTPUT_DIR       | data                                                                                                       | The directory that the table data will be exported to.           |
| FILE_PER_TABLE   | 1                                                                                                          | Export the data from each table as a separate file.              |

To help diagnose problems with the ora2pg configuration, the DEBUG setting can be changed to 1.

DISABLE_SEQUENCE will only update the sequences in the target database if ora2pg is being used to update the target database directly rather than to produce export files.  The configuration is set up to produce export files, so DISABLE_SEQUENCES has been set to 1 to prevent it from including a misleading "Restarting sequences" section in the output.

## Running ora2pg

### Perl

The following command is used to run the Perl version of ora2pg:

`ora2pg -c <config_file_location> -s <oracle_dsn> -u <oracle_db_user> -w <oracle_db_password>`

The `<oracle_dsn>` value needs to be specified in the format `dbi:Oracle:host=<db_host_name>;service_name=<db_service_name>;port=<db_port>`

If desired, the `ORACLE_DSN`, `ORACLE_USER` and `ORACLE_PWD` settings in `ora2pg.conf` can be used instead of the -s, -u and -w command line arguments.

### Docker

The following compose file can be used to run the Docker container version of ora2pg.

```
version: '3.3'
services:
  ora2pg:
    container_name: ora2pg
    environment:
      - CONFIG_LOCATION = /config/ora2pg.conf
      - OUTPUT_LOCATION = /data
    volumes:
      - ./config:/config
      - ./data:/data
    image: georgmoser/ora2pg
```

Run it by executing `docker compose up` in the directory containing the compose file.

The directory containing the compose file must have `config` and `data` subdirectories.  The `config` directory must contain the `ora2pg.conf` file.
