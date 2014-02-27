ALTER SESSION SET current_schema=sdt_owner;


CREATE OR REPLACE FUNCTION purge_bulk_submissions (nParam NUMBER,nBatch NUMBER) RETURN user_tables.num_rows%TYPE AS
  CURSOR c1 (nParam NUMBER)
            IS SELECT rowid
               FROM bulk_submissions
               WHERE created_date < (sysdate - nParam);
  nIteration   NUMBER;
  BEGIN
    nIteration := 0;
    FOR c1_rec IN c1(nParam) LOOP
      DELETE FROM bulk_submissions
      WHERE ROWID = c1_rec.rowid;
      nIteration := nIteration + 1;
      IF MOD(nIteration,nBatch)=0 THEN
        COMMIT;
      END IF;
    END LOOP;
    COMMIT;
    RETURN nIteration;
  END;
/


CREATE OR REPLACE FUNCTION purge_individual_requests (nParam NUMBER,nBatch NUMBER) RETURN user_tables.num_rows%TYPE AS
  CURSOR c1 (nParam NUMBER)
            IS SELECT rowid
               FROM individual_requests
               WHERE created_date < (trunc(sysdate) - nParam);
  nIteration   NUMBER;
  BEGIN
    nIteration := 0;
    FOR c1_rec IN c1(nParam) LOOP
      DELETE FROM individual_requests
      WHERE ROWID = c1_rec.rowid;
      nIteration := nIteration + 1;
      IF MOD(nIteration,nBatch)=0 THEN
        COMMIT;
      END IF;
    END LOOP;
    COMMIT;
    RETURN nIteration;
  END;
/

CREATE OR REPLACE FUNCTION purge_error_logs (nParam NUMBER,nBatch NUMBER) RETURN user_tables.num_rows%TYPE AS
  CURSOR c1 (nParam NUMBER)
            IS SELECT rowid
               FROM error_logs
               WHERE created_date < (trunc(sysdate) - nParam);
  nIteration   NUMBER;
  BEGIN
    nIteration := 0;
    FOR c1_rec IN c1(nParam) LOOP
      DELETE FROM error_logs
      WHERE ROWID = c1_rec.rowid;
      nIteration := nIteration + 1;
      IF MOD(nIteration,nBatch)=0 THEN
        COMMIT;
      END IF;
    END LOOP;
    COMMIT;
    RETURN nIteration;
  END;
/

CREATE OR REPLACE FUNCTION purge_service_requests (nParam NUMBER,nBatch NUMBER) RETURN user_tables.num_rows%TYPE AS
  CURSOR c1 (nParam NUMBER)
            IS SELECT rowid
               FROM service_requests
               WHERE created_date < (trunc(sysdate) - nParam);
  nIteration   NUMBER;
  BEGIN
    nIteration := 0;
    FOR c1_rec IN c1(nParam) LOOP
      DELETE FROM service_requests
      WHERE ROWID = c1_rec.rowid;
      nIteration := nIteration + 1;
      IF MOD(nIteration,nBatch)=0 THEN
        COMMIT;
      END IF;
    END LOOP;
    COMMIT;
    RETURN nIteration;
  END;
/
