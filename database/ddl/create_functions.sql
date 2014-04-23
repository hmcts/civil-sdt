ALTER SESSION SET current_schema=sdt_owner;


CREATE OR REPLACE FUNCTION purge_service_requests (i_RetentionPeriod IN global_parameters.parameter_value%TYPE
                                                  ,i_CommitInterval  IN NUMBER) RETURN user_tables.num_rows%TYPE AS
  CURSOR c1 (nParam NUMBER)
            IS SELECT rowid
               FROM service_requests
               WHERE request_timestamp < (TRUNC(SYSTIMESTAMP) - i_RetentionPeriod);
  nIteration   NUMBER;
  BEGIN
    nIteration := 0;
    FOR c1_rec IN c1(i_RetentionPeriod) LOOP
      DELETE FROM service_requests
      WHERE ROWID = c1_rec.rowid;
      nIteration := nIteration + 1;
      IF MOD(nIteration,i_CommitInterval)=0 THEN
        COMMIT;
      END IF;
    END LOOP;
    COMMIT;
    RETURN nIteration;
  END;
/
