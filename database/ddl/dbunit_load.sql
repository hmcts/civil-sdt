-- Procedure to disable all constraints and triggers prior to loading test data with DBUNIT.
create or replace
procedure Prepare_For_Dbunit_Load
(
  p_SchemaName varchar2
)
AUTHID CURRENT_USER is
BEGIN

    FOR i IN (select table_name, constraint_name from all_constraints where owner = p_SchemaName AND constraint_type = 'R')
    LOOP
        -- Disable foreign key constraints only, not the primary key, unique key, check constraints so we still get some checking.
        EXECUTE IMMEDIATE 'ALTER TABLE ' || p_SchemaName || '.' || i.table_name || ' DISABLE CONSTRAINT ' || i.constraint_name;
    END LOOP i;

    FOR j IN (select trigger_name from all_triggers where owner = p_SchemaName)
    LOOP
        EXECUTE IMMEDIATE 'ALTER TRIGGER ' || p_SchemaName || '.' || j.trigger_name || ' DISABLE';
    END LOOP j;
END Prepare_For_Dbunit_Load;
/ 

-- Procedure to enable all constraints and triggers prior to loading test data with DBUNIT.
create or replace
procedure Finish_Dbunit_Load
(
  p_SchemaName varchar2
)
AUTHID CURRENT_USER is
BEGIN

    FOR i IN (select table_name, constraint_name from all_constraints where owner = p_SchemaName AND constraint_type = 'R')
    LOOP
        -- Enable the disabled constraints
        EXECUTE IMMEDIATE 'ALTER TABLE ' || p_SchemaName || '.' || i.table_name || ' ENABLE NOVALIDATE CONSTRAINT ' || i.constraint_name;
    END LOOP i;

    FOR j IN (select trigger_name from all_triggers where owner = p_SchemaName)
    LOOP
        EXECUTE IMMEDIATE 'ALTER TRIGGER ' || p_SchemaName || '.' || j.trigger_name || ' ENABLE';
    END LOOP j;
END Finish_Dbunit_Load;
/