@echo off

REM ------------------------------------------------------------------------
REM Windows script to extract all ORACLE ddl in a given schema. 
REM
REM Usage: extract_schema_ddl.bat
REM
REM ------------------------------------------------------------------------

REM ------------------------------------------------------------------------
REM Create directory for scripts and then create separate script of each 
REM object type in a given schema, and then for each constraint type.
REM ------------------------------------------------------------------------


echo EXTRACTING DDL FOR SCHEMA SDT_OWNER
rmdir /s /q ..\generated
mkdir ..\generated\SDT_OWNER
sqlplus system/elephant @extract_objects_ddl.sql SDT_OWNER
sqlplus system/elephant @extract_constraints_ddl.sql SDT_OWNER
sqlplus system/elephant @extract_grants_ddl.sql SDT_OWNER


echo.
echo.
echo.
echo.
echo EXTRACTING DDL FOR SCHEMA SDT_USER
mkdir ..\generated\SDT_USER
sqlplus system/elephant @extract_objects_ddl.sql SDT_USER
sqlplus system/elephant @extract_constraints_ddl.sql SDT_USER
sqlplus system/elephant @extract_grants_ddl.sql SDT_USER


echo.
echo.
echo.
echo.
echo EXTRACTING DDL FOR SCHEMA SDT_BATCH_USER
mkdir ..\generated\SDT_BATCH_USER
sqlplus system/elephant @extract_objects_ddl.sql SDT_BATCH_USER
sqlplus system/elephant @extract_constraints_ddl.sql SDT_BATCH_USER
sqlplus system/elephant @extract_grants_ddl.sql SDT_BATCH_USER


echo.
echo.
echo.
echo.
echo OUTPUT WRITTEN TO .\generated
