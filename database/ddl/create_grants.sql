connect sdt_owner/sdt_owner


GRANT SELECT,INSERT,UPDATE,DELETE ON bulk_customers         TO sdt_user;  
GRANT SELECT,INSERT,UPDATE,DELETE ON bulk_customer_services TO sdt_user; 
GRANT SELECT,INSERT,UPDATE,DELETE ON bulk_submissions       TO sdt_user;
GRANT SELECT,INSERT,UPDATE,DELETE ON error_log              TO sdt_user; 
GRANT SELECT,INSERT,UPDATE,DELETE ON error_messages         TO sdt_user;
GRANT SELECT,INSERT,UPDATE,DELETE ON global_parameters      TO sdt_user;
GRANT SELECT,INSERT,UPDATE,DELETE ON individual_requests    TO sdt_user; 
GRANT SELECT,INSERT,UPDATE,DELETE ON request_types          TO sdt_user; 
GRANT SELECT,INSERT,UPDATE,DELETE ON routing_table          TO sdt_user; 
GRANT SELECT,INSERT,UPDATE,DELETE ON valid_services         TO sdt_user;

