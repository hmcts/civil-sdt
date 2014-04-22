alter session set current_schema=sdt_owner;


GRANT SELECT,INSERT,UPDATE,DELETE ON bulk_customers             TO sdt_user;  
GRANT SELECT,INSERT,UPDATE,DELETE ON bulk_customer_applications TO sdt_user; 
GRANT SELECT,INSERT,UPDATE,DELETE ON bulk_submissions           TO sdt_user;
GRANT SELECT,INSERT,UPDATE,DELETE ON error_logs                 TO sdt_user; 
GRANT SELECT,INSERT,UPDATE,DELETE ON error_messages             TO sdt_user;
GRANT SELECT,INSERT,UPDATE,DELETE ON global_parameters          TO sdt_user;
GRANT SELECT,INSERT,UPDATE,DELETE ON individual_requests        TO sdt_user;
GRANT SELECT,INSERT,UPDATE,DELETE ON service_types              TO sdt_user; 
GRANT SELECT,INSERT,UPDATE,DELETE ON service_routings           TO sdt_user; 
GRANT SELECT,INSERT,UPDATE,DELETE ON service_requests           TO sdt_user; 
GRANT SELECT,INSERT,UPDATE,DELETE ON target_applications        TO sdt_user;

GRANT SELECT ON BULK_CUST_SEQ TO sdt_user;
GRANT SELECT ON BULK_CUST_APP_SEQ TO sdt_user;
GRANT SELECT ON BULK_SUB_SEQ TO sdt_user;
GRANT SELECT ON ERR_LOG_SEQ TO sdt_user;
GRANT SELECT ON ERR_MESG_SEQ TO sdt_user;
GRANT SELECT ON GLB_PAR_SEQ TO sdt_user;
GRANT SELECT ON IND_REQ_SEQ TO sdt_user;
GRANT SELECT ON SER_ROU_SEQ TO sdt_user;
GRANT SELECT ON SER_TYP_SEQ TO sdt_user;
GRANT SELECT ON TAR_APP_SEQ TO sdt_user;
GRANT SELECT ON SDT_REF_SEQ TO sdt_user;
GRANT SELECT ON SRV_REQ_SEQ TO sdt_user;

GRANT SELECT,DELETE on bulk_submissions     TO sdt_batch_user;
GRANT SELECT,DELETE on individual_requests  TO sdt_batch_user;
GRANT SELECT,DELETE on error_logs           TO sdt_batch_user;
GRANT SELECT,DELETE on service_requests     TO sdt_batch_user;