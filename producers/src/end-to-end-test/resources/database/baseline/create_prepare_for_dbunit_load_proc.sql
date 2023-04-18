CREATE OR REPLACE PROCEDURE PREPARE_FOR_DBUNIT_LOAD
(
  p_SchemaName varchar
)
AS '
DECLARE i record;
DECLARE j record;
BEGIN

    FOR i IN (SELECT  rel.relname as table_name, con.conname as constraint_name
       FROM pg_catalog.pg_constraint con
            INNER JOIN pg_catalog.pg_class rel
                       ON rel.oid = con.conrelid
            INNER JOIN pg_catalog.pg_namespace nsp
                       ON nsp.oid = connamespace
       WHERE nsp.nspname = p_SchemaName AND con.contype in (''f''))
    LOOP
        -- Disable foreign key constraints only, not the primary key, unique key, check constraints so we still get some checking.
        EXECUTE ''ALTER TABLE '' || p_SchemaName || ''.'' || i.table_name || '' DROP CONSTRAINT '' || i.constraint_name;
    END LOOP;

END
'
LANGUAGE plpgsql;
