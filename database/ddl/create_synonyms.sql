alter session set current_schema=sdt_user;

CREATE SYNONYM bulk_customers             FOR sdt_owner.bulk_customers;       
CREATE SYNONYM bulk_customer_applications FOR sdt_owner.bulk_customer_applications;
CREATE SYNONYM bulk_submissions           FOR sdt_owner.bulk_submissions;
CREATE SYNONYM error_logs                 FOR sdt_owner.error_logs;
CREATE SYNONYM error_messages             FOR sdt_owner.error_messages;
CREATE SYNONYM global_parameters          FOR sdt_owner.global_parameters;
CREATE SYNONYM individual_requests        FOR sdt_owner.individual_requests;
CREATE SYNONYM message_logs               FOR sdt_owner.message_logs;
CREATE SYNONYM request_types              FOR sdt_owner.request_types;
CREATE SYNONYM request_routings           FOR sdt_owner.requst_routings;
CREATE SYNONYM service_requests           FOR sdt_owner.service_requests;
CREATE SYNONYM target_applications        FOR sdt_owner.target_applications;

