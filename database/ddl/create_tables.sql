connect sdt_owner/sdt_owner


define bulk_customers              = 'TABLESPACE users'
define bulk_customer_applications  = 'TABLESPACE users'
define bulk_submissions            = 'TABLESPACE users'
define bulk_submissions_lob        = 'LOB (bulk_payload) STORE AS bs_lob(TABLESPACE users)'
define error_log                   = 'TABLESPACE users'
define error_messages              = 'TABLESPACE users'
define global_parameters           = 'TABLESPACE users'
define individual_requests         = 'TABLESPACE users'
define individual_requests_lob     = 'LOB (individual_payload) STORE AS is_lob(TABLESPACE users)'
define request_types               = 'TABLESPACE users'
define request_routings            = 'TABLESPACE users'
define target_applications         = 'TABLESPACE users'


CREATE TABLE bulk_customers
(bulk_customer_id         INTEGER         -- synthetic pk
,bulk_customer_case_code  CHAR(2)      
,sdt_customer_id          NUMBER(4,0)
,customer_identifier      NUMBER(4,0)     -- between 1000 and 9999
,version_number           INTEGER         -- hiberate versioning column
) &bulk_customers
;


CREATE TABLE bulk_customer_applications
(bulk_customer_id         INTEGER         -- pk, fk from bulk_customers
,target_application_id    INTEGER         -- pk, fk from target_applications
,customer_application_id  VARCHAR2(32)   
,version_number           INTEGER         -- hiberate versioning column
) &bulk_customer_applications
;

CREATE TABLE bulk_submissions
(bulk_submission_id       INTEGER         -- pk
,bulk_customer_id         INTEGER         -- fk from bulk_customers
,target_application_id    INTEGER         -- fk from target_applications
,sdt_bulk_reference       VARCHAR2(32)    -- fixed format 
,customer_reference       VARCHAR2(32)
,created_date             TIMESTAMP       -- date/time of record created
,number_of_requests       INTEGER
,bulk_submission_status   VARCHAR2(20)
,completed_date           TIMESTAMP
,updated_date             TIMESTAMP       -- date/time of last change to record
,version_number           INTEGER         -- hiberate versioning column
,bulk_payload             BLOB
) &bulk_submissions_lob
&bulk_submissions
;

CREATE TABLE error_log
(error_log_id             INTEGER         -- pk, synthetic
,bulk_submission_id       INTEGER         -- fk from bulk_submissions
,individual_request_id    INTEGER         -- fk from individual_requests, null for error raised on bulk file 
,error_message_id         INTEGER         -- fk from error_messages
,created_date             TIMESTAMP       -- date/time of record created
,updated_date             TIMESTAMP       -- date/time of last change to record
,version_number           INTEGER         -- hiberate versioning column
,error_text               VARCHAR2(1000)  -- detailed error mesg
) &error_log
;

CREATE TABLE error_messages
(error_message_id         INTEGER         -- pk, synthetic
,error_code               VARCHAR2(32)    -- 
,error_text               VARCHAR2(1000)  -- error mesg with placeholder for context
,error_description        VARCHAR2(2000)  -- generic description of error
,version_number           INTEGER         -- hiberate versioning column
) &error_messages
;

CREATE TABLE global_parameters
(global_parameter_id      INTEGER
,parameter_name           VARCHAR2(32)
,parameter_value          VARCHAR2(32)
,version_number           INTEGER         -- hiberate versioning column
) &global_parameters
;

CREATE TABLE individual_requests
(individual_request_id    INTEGER        -- pk
,bulk_submission_id       INTEGER        -- fk from bulk_submissions
,request_type_id          INTEGER        -- fk from request_type
,customer_request_ref     VARCHAR2(32)
,case_number              VARCHAR2(32)
,request_status           VARCHAR2(32)
,issued_date              TIMESTAMP
,sdt_bulk_reference       VARCHAR2(32)
,line_number              INTEGER
,sdt_request_reference    VARCHAR2(38)   -- fixed format
,created_date             TIMESTAMP      -- date/time of record created
,updated_date             TIMESTAMP      -- date/time of last change to record
,completed_date           TIMESTAMP
,service_date             TIMESTAMP
,warrant_number           INTEGER
,enforcing_court_code     VARCHAR2(32)
,enforcing_court_name     VARCHAR2(255)
,fee                      NUMBER(8,2)
,version_number           INTEGER         -- hiberate versioning column
,individual_payload       BLOB
) &individual_requests_lob 
&individual_requests 
;

CREATE TABLE request_types
(request_type_id          INTEGER
,request_type_name        VARCHAR2(50)
,request_type_status      VARCHAR2(1)
,request_type_description VARCHAR2(2000)
,version_number           INTEGER         -- hiberate versioning column
) &request_types
;

CREATE TABLE request_routings
(request_type_id          INTEGER        -- pk, fk to request_types
,target_application_id    INTEGER        -- pk, fk to valid_services
,web_service_endpoint     VARCHAR2(255)
,version_number           INTEGER         -- hiberate versioning column
) &request_routings
;

CREATE TABLE target_applications
(target_application_id    INTEGER         -- pk
,target_application_code  VARCHAR2(4)     -- mcol, pcol,c_mc, c_pc
,target_application_name  VARCHAR2(255)
,version_number           INTEGER         -- hiberate versioning column
) &target_applications
;

