alter session set current_schema=sdt_user;

CREATE SYNONYM bulk_customers             FOR sdt_owner.bulk_customers;       
CREATE SYNONYM bulk_customer_applications FOR sdt_owner.bulk_customer_applications;
CREATE SYNONYM bulk_submissions           FOR sdt_owner.bulk_submissions;
CREATE SYNONYM error_logs                 FOR sdt_owner.error_logs;
CREATE SYNONYM error_messages             FOR sdt_owner.error_messages;
CREATE SYNONYM global_parameters          FOR sdt_owner.global_parameters;
CREATE SYNONYM individual_requests        FOR sdt_owner.individual_requests;
CREATE SYNONYM message_logs               FOR sdt_owner.message_logs;
CREATE SYNONYM service_types              FOR sdt_owner.service_types;
CREATE SYNONYM service_routings           FOR sdt_owner.service_routings;
CREATE SYNONYM service_requests           FOR sdt_owner.service_requests;
CREATE SYNONYM target_applications        FOR sdt_owner.target_applications;

CREATE SYNONYM BULK_CUST_SEQ              FOR sdt_owner.BULK_CUST_SEQ;
CREATE SYNONYM BULK_CUST_APP_SEQ          FOR sdt_owner.BULK_CUST_APP_SEQ;
CREATE SYNONYM BULK_SUB_SEQ               FOR sdt_owner.BULK_SUB_SEQ;
CREATE SYNONYM ERR_LOG_SEQ                FOR sdt_owner.ERR_LOG_SEQ;
CREATE SYNONYM ERR_MESG_SEQ               FOR sdt_owner.ERR_MESG_SEQ;
CREATE SYNONYM GLB_PAR_SEQ                FOR sdt_owner.GLB_PAR_SEQ;
CREATE SYNONYM IND_REQ_SEQ                FOR sdt_owner.IND_REQ_SEQ;
CREATE SYNONYM SER_ROU_SEQ                FOR sdt_owner.SER_ROU_SEQ;
CREATE SYNONYM SER_TYP_SEQ                FOR sdt_owner.SER_TYP_SEQ;
CREATE SYNONYM TAR_APP_SEQ                FOR sdt_owner.TAR_APP_SEQ;
CREATE SYNONYM SDT_REF_SEQ                FOR sdt_owner.SDT_REF_SEQ;
CREATE SYNONYM SRV_REQ_SEQ                FOR sdt_owner.SRV_REQ_SEQ;

alter session set current_schema=sdt_batch_user;

CREATE SYNONYM bulk_submissions           FOR sdt_owner.bulk_submissions;
CREATE SYNONYM error_logs                 FOR sdt_owner.error_logs;
CREATE SYNONYM individual_requests        FOR sdt_owner.individual_requests;
CREATE SYNONYM service_requests           FOR sdt_owner.service_requests;
CREATE SYNONYM global_parameters          FOR sdt_owner.global_parameters;
CREATE SYNONYM purge_bulk_submissions     FOR sdt_owner.purge_bulk_submissions;
CREATE SYNONYM purge_individual_requests  FOR sdt_owner.purge_individual_requests;
CREATE SYNONYN purge_error_logs           FOR sdt_owner.purge_error_logs;
CREATE SYNONYM purge_service_requests     FOR sdt_owner.purge_service_requests;


