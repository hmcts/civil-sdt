alter session set current_schema=sdt_owner;


define bulk_customers              = 'TABLESPACE users'
define bulk_customer_applications  = 'TABLESPACE users'
define bulk_submissions            = 'TABLESPACE users'
define bulk_submissions_lob        = 'LOB (bulk_payload) STORE AS bs_lob(TABLESPACE users)'
define error_logs                  = 'TABLESPACE users'
define error_messages              = 'TABLESPACE users'
define global_parameters           = 'TABLESPACE users'
define individual_requests         = 'TABLESPACE users'
define individual_requests_lob     = 'LOB (individual_payload) STORE AS is_lob(TABLESPACE users)'
define message_logs                = 'TABLESPACE users'
define service_types               = 'TABLESPACE users'
define service_routings            = 'TABLESPACE users'
define service_requests            = 'TABLESPACE users'
define target_applications         = 'TABLESPACE users'



CREATE TABLE bulk_customers
(bulk_customer_id         INTEGER               -- synthetic pk
--,bulk_customer_case_code  CHAR(2)      
,sdt_customer_id          NUMBER(8,0)           -- external id for customer identification, 10000000-99999999
,version_number           INTEGER DEFAULT 0     -- hiberate versioning column
) &bulk_customers
;

CREATE TABLE bulk_customer_applications
(bulk_customer_id         INTEGER               -- pk, fk from bulk_customers
,target_application_id    INTEGER               -- pk, fk from target_applications
,customer_application_id  VARCHAR2(32)   
,version_number           INTEGER DEFAULT 0     -- hiberate versioning column
) &bulk_customer_applications
;

CREATE TABLE bulk_submissions
(bulk_submission_id       INTEGER               -- pk
,bulk_customer_id         INTEGER               -- fk from bulk_customers
,target_application_id    INTEGER               -- fk from target_applications
,sdt_bulk_reference       VARCHAR2(29)          -- fixed format 
,customer_reference       VARCHAR2(32)
,created_date             TIMESTAMP             -- date/time of record created
,number_of_requests       INTEGER
,bulk_submission_status   VARCHAR2(20)
,completed_date           TIMESTAMP
,updated_date             TIMESTAMP             -- date/time of last change to record
,version_number           INTEGER DEFAULT 0     -- hiberate versioning column
,bulk_payload             BLOB
) &bulk_submissions_lob
&bulk_submissions
;

CREATE TABLE error_logs
(error_log_id             INTEGER               -- pk, synthetic
,bulk_submission_id       INTEGER               -- fk from bulk_submissions
,individual_request_id    INTEGER               -- fk from individual_requests, null for error raised on bulk file 
,error_code               INTEGER               -- unimplemented fk from error_messages
,created_date             TIMESTAMP             -- date/time of record created
,updated_date             TIMESTAMP             -- date/time of last change to record
,version_number           INTEGER DEFAULT 0     -- hiberate versioning column
,error_text               VARCHAR2(1000)        -- detailed error mesg
) &error_logs
;

CREATE TABLE error_messages
(error_message_id         INTEGER               -- pk, synthetic
,error_code               VARCHAR2(32)         
,error_text               VARCHAR2(1000)        -- error mesg with placeholder for context
,error_description        VARCHAR2(2000)        -- generic description of error
,version_number           INTEGER DEFAULT 0     -- hiberate versioning column
) &error_messages
;

CREATE TABLE global_parameters
(global_parameter_id      INTEGER
,parameter_name           VARCHAR2(32)
,parameter_value          VARCHAR2(32)
,parameter_description    VARCHAR2(2000)
,version_number           INTEGER DEFAULT 0     -- hiberate versioning column
) &global_parameters
;

CREATE TABLE individual_requests
(individual_request_id        INTEGER           -- pk
,bulk_submission_id           INTEGER           -- fk from bulk_submissions
--,request_type_id              INTEGER           -- fk from request_type
,customer_request_ref         VARCHAR2(32)      -- unique request ref
--,case_number                VARCHAR2(32)
,request_status               VARCHAR2(32)
--,issued_date                TIMESTAMP
,sdt_bulk_reference           VARCHAR2(29)
--,request_retry_count          INTEGER           -- number of attempts made to submit request
,line_number                  INTEGER
,sdt_request_reference        VARCHAR2(37)      -- fixed format
,created_date                 TIMESTAMP         -- date/time of record created
,updated_date                 TIMESTAMP         -- date/time of last change to record
,completed_date               TIMESTAMP
--,service_date               TIMESTAMP
--,warrant_number             INTEGER
--,enforcing_court_code       VARCHAR2(32)
--,enforcing_court_name       VARCHAR2(255)
--,fee                        NUMBER(8,2)
--,rejection_reason_code        VARCHAR2(32)
--,rejection_reason_description VARCHAR2(4000)
,forwarding_attempts          INTEGER
--,target_application_status    VARCHAR2(4000)
,target_application_response  VARCHAR2(4000) 
,internal_system_error        VARCHAR2(4000)
,version_number               INTEGER DEFAULT 0  -- hiberate versioning column
,individual_payload           BLOB
) &individual_requests_lob 
&individual_requests 
;


CREATE TABLE service_routings
(service_type_id          INTEGER            -- pk, fk to service_types
,target_application_id    INTEGER            -- pk, fk to valid_services
,web_service_endpoint     VARCHAR2(255)
,version_number           INTEGER DEFAULT 0  -- hiberate versioning column
) &service_routings
;

CREATE TABLE service_types
(service_type_id          INTEGER
,service_type_name        VARCHAR2(50)
,service_type_status      VARCHAR2(1)
,service_type_description VARCHAR2(2000)
,version_number           INTEGER DEFAULT 0  -- hiberate versioning column
) &service_types
;

CREATE TABLE service_requests
(service_request_id       INTEGER            -- synthetic pk
--,service_request_header   VARCHAR2(4000)     
,request_payload          BLOB               -- the full incoming message including headers 
,request_timestamp        TIMESTAMP          -- date/time of request receipt
,response_payload         BLOB               -- the full outgoing message including headers
,response_timestamp       TIMESTAMP          -- date/time of request response
,request_type             VARCHAR2(32)       -- the type of request
,sdt_customer_id          VARCHAR2(32)       -- should logically map bulk customers but not guaranteed
,sdt_bulk_reference       VARCHAR2(29)       -- should logically map bulk submissions but not guaranteed 
,internal_system_error    VARCHAR2(4000)     
,target_application_id    INTEGER            
,version_number           INTEGER DEFAULT 0  -- hiberate versioning column 
) &service_requests
;

CREATE TABLE target_applications
(target_application_id    INTEGER            -- pk
,target_application_code  VARCHAR2(4)        -- mcol, pcol,c_mc, c_pc
,target_application_name  VARCHAR2(255)
,version_number           INTEGER DEFAULT 0  -- hiberate versioning column
) &target_applications
;

