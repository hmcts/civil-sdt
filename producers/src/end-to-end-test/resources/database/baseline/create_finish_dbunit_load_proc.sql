CREATE OR REPLACE PROCEDURE FINISH_DBUNIT_LOAD(p_SchemaName varchar) language plpgsql AS '
DECLARE i record;
DECLARE j record;
BEGIN
    FOR i IN (SELECT  rel.relname as table_name, con.conname as constraint_name
                FROM pg_catalog.pg_constraint con
                    INNER JOIN pg_catalog.pg_class rel
                       ON rel.oid = con.conrelid
                    INNER JOIN pg_catalog.pg_namespace nsp
                       ON nsp.oid = connamespace
                WHERE con.contype = ''f''
    )
    LOOP
        -- Enable the disabled constraints
        EXECUTE''ALTER TABLE '' || i.table_name || '' ALTER CONSTRAINT '' || i.constraint_name || '' NOT DEFERRABLE'';
    END LOOP;

    FOR j IN (SELECT event_object_table AS table_name, trigger_name
				FROM information_schema.triggers
				GROUP BY table_name , trigger_name
				ORDER BY table_name ,trigger_name
    )
    LOOP
        EXECUTE ''ALTER TRIGGER '' || j.trigger_name || '' ENABLE'';
    END LOOP;
END;
'
