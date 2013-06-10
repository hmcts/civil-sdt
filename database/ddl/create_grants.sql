alter session set current_schema=sdt_owner;


GRANT SELECT,INSERT,UPDATE,DELETE ON bulk_customers             TO sdt_user;  
GRANT SELECT,INSERT,UPDATE,DELETE ON bulk_customer_applications TO sdt_user; 
GRANT SELECT,INSERT,UPDATE,DELETE ON bulk_submissions           TO sdt_user;
GRANT SELECT,INSERT,UPDATE,DELETE ON error_logs                 TO sdt_user; 
GRANT SELECT,INSERT,UPDATE,DELETE ON error_messages             TO sdt_user;
GRANT SELECT,INSERT,UPDATE,DELETE ON global_parameters          TO sdt_user;
GRANT SELECT,INSERT,UPDATE,DELETE ON individual_requests        TO sdt_user;
GRANT SELECT,INSERT,UPDATE,DELETE ON message_logs               TO sdt_user;
GRANT SELECT,INSERT,UPDATE,DELETE ON request_types              TO sdt_user; 
GRANT SELECT,INSERT,UPDATE,DELETE ON request_routings           TO sdt_user; 
GRANT SELECT,INSERT,UPDATE,DELETE ON target_applications        TO sdt_user;

