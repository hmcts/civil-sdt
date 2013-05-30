connect sdt_owner/sdt_owner


define bulk_customers          = 'TABLESPACE users'
define bulk_customer_services  = 'TABLESPACE users'
define bulk_submissions        = 'TABLESPACE users'
define bulk_submissions_lob    = 'LOB (bulk_payload) STORE AS bs_lob(TABLESPACE users)'
define error_log               = 'TABLESPACE users'
define error_messages          = 'TABLESPACE users'
define global_parameters       = 'TABLESPACE users'
define individual_requests     = 'TABLESPACE users'
define individual_requests_lob = 'LOB (individual_payload) STORE AS is_lob(TABLESPACE users)'
define request_types           = 'TABLESPACE users'
define routing_table           = 'TABLESPACE users'
define valid_services          = 'TABLESPACE users'


CREATE TABLE bulk_customers
(bulk_customer_id         INTEGER         -- synthetic pk
,bulk_customer_case_code  CHAR(2)      
,sdt_customer_id          NUMBER(4,0)
,customer_identifier      NUMBER(4,0)     -- between 1000 and 9999
) &bulk_customers
;


CREATE TABLE bulk_customer_services
(bulk_customer_id         INTEGER         -- pk, fk from bulk_customers
,valid_service_id         CHAR(4)         -- mcol, pcol,c_mc, c_pc
) &bulk_customer_services
;

CREATE TABLE bulk_submissions
(bulk_submission_id       INTEGER         -- pk
,bulk_customer_id         INTEGER         -- fk from bulk_customers
,valid_service_id         CHAR(4)         -- fk from valid_services
,sdt_bulk_reference       VARCHAR2(32)    -- fixed format 
,user_file_ref            VARCHAR2(32)
,submitted_date           TIMESTAMP
,number_of_requests       INTEGER
,bulk_submission_status   VARCHAR2(20)
,completed_date           TIMESTAMP
,updated_date             TIMESTAMP
,bulk_payload             clob
) &bulk_submissions_lob
&bulk_submissions
;

CREATE TABLE error_log
(error_log_id             INTEGER        -- pk, synthetic
,individual_request_id    INTEGER        -- fk from individual_requests
,error_code               INTEGER        -- fk from error_messages
,error_locator            INTEGER        -- ref to error in xml
,error_text               VARCHAR2(1000) -- detailed error mesg
) &error_log
;

CREATE TABLE error_messages
(error_code               INTEGER        -- pk, synthetic
,error_text               VARCHAR2(1000) -- error mesg with placeholder for context
,error_description        VARCHAR2(2000) -- generic description of error
) &error_messages
;

CREATE TABLE global_parameters
(parameter_id             INTEGER
,parameter_name           VARCHAR2(32)
,parameter_value          VARCHAR2(32)
) &global_parameters
;

CREATE TABLE individual_requests
(individual_request_id    INTEGER        -- pk
,bulk_submission_id       INTEGER        -- fk from bulk_submissions
,request_type_id          INTEGER        -- fk from request_type
,user_request_reference   VARCHAR2(32)
,case_number              VARCHAR2(32)
,request_status           VARCHAR2(32)
,issued_date              TIMESTAMP
,sdt_bulk_reference       VARCHAR2(32)
,line_number              INTEGER
,sdt_request_reference    VARCHAR2(38) -- fixed format
,individual_payload       clob
) &individual_requests_lob 
&individual_requests 
;

CREATE TABLE request_types
(request_type_id          INTEGER
,request_type_name        VARCHAR2(50)
) &request_types
;

CREATE TABLE routing_table
(request_type_id          INTEGER        -- pk, fk to request_types
,valid_service_id         CHAR(4)        -- pk, fk to valid_services
,web_service_name         VARCHAR2(32)
) &routing_table
;

CREATE TABLE valid_services
(valid_service_id         CHAR(4)         -- pk, mcol, pcol,c_mc, c_pc
,valid_service_name       VARCHAR2(30)
) &valid_services
;

