
  CREATE TABLE "SDT_OWNER"."BULK_CUSTOMERS"
   (	"BULK_CUSTOMER_ID" NUMBER(*,0),
	"SDT_CUSTOMER_ID" NUMBER(8,0),
	"VERSION_NUMBER" NUMBER(*,0) DEFAULT 0
   ) ;



  CREATE TABLE "SDT_OWNER"."BULK_CUSTOMER_APPLICATIONS"
   (	"BULK_CUSTOMER_APPLICATIONS_ID" NUMBER(*,0),
	"BULK_CUSTOMER_ID" NUMBER(*,0),
	"TARGET_APPLICATION_ID" NUMBER(*,0),
	"CUSTOMER_APPLICATION_ID" VARCHAR2(32),
	"VERSION_NUMBER" NUMBER(*,0) DEFAULT 0
   ) ;



  CREATE TABLE "SDT_OWNER"."BULK_SUBMISSIONS"
   (	"BULK_SUBMISSION_ID" NUMBER(*,0),
	"BULK_CUSTOMER_ID" NUMBER(*,0),
	"TARGET_APPLICATION_ID" NUMBER(*,0),
	"SERVICE_REQUEST_ID" NUMBER(*,0),
	"SDT_BULK_REFERENCE" VARCHAR2(29),
	"CUSTOMER_REFERENCE" VARCHAR2(32),
	"CREATED_DATE" TIMESTAMP (6),
	"NUMBER_OF_REQUESTS" NUMBER(*,0),
	"BULK_SUBMISSION_STATUS" VARCHAR2(20),
	"COMPLETED_DATE" TIMESTAMP (6),
	"UPDATED_DATE" TIMESTAMP (6),
	"ERROR_CODE" VARCHAR2(32),
	"ERROR_TEXT" VARCHAR2(1000),
	"VERSION_NUMBER" NUMBER(*,0) DEFAULT 0,
	"BULK_PAYLOAD" BLOB
   ) ;



  CREATE TABLE "SDT_OWNER"."ERROR_LOGS"
   (	"ERROR_LOG_ID" NUMBER(*,0),
	"INDIVIDUAL_REQUEST_ID" NUMBER(*,0),
	"ERROR_CODE" VARCHAR2(32),
	"CREATED_DATE" TIMESTAMP (6),
	"UPDATED_DATE" TIMESTAMP (6),
	"VERSION_NUMBER" NUMBER(*,0) DEFAULT 0,
	"ERROR_TEXT" VARCHAR2(1000)
   ) ;



  CREATE TABLE "SDT_OWNER"."ERROR_MESSAGES"
   (	"ERROR_MESSAGE_ID" NUMBER(*,0),
	"ERROR_CODE" VARCHAR2(32),
	"ERROR_TEXT" VARCHAR2(1000),
	"ERROR_DESCRIPTION" VARCHAR2(2000),
	"VERSION_NUMBER" NUMBER(*,0) DEFAULT 0
   ) ;



  CREATE TABLE "SDT_OWNER"."GLOBAL_PARAMETERS"
   (	"GLOBAL_PARAMETER_ID" NUMBER(*,0),
	"PARAMETER_NAME" VARCHAR2(32),
	"PARAMETER_VALUE" VARCHAR2(32),
	"PARAMETER_DESCRIPTION" VARCHAR2(2000),
	"VERSION_NUMBER" NUMBER(*,0) DEFAULT 0
   ) ;



  CREATE TABLE "SDT_OWNER"."INDIVIDUAL_REQUESTS"
   (	"INDIVIDUAL_REQUEST_ID" NUMBER(*,0),
	"BULK_SUBMISSION_ID" NUMBER(*,0),
	"CUSTOMER_REQUEST_REF" VARCHAR2(32),
	"REQUEST_STATUS" VARCHAR2(32),
	"SDT_BULK_REFERENCE" VARCHAR2(29),
	"LINE_NUMBER" NUMBER(*,0),
	"SDT_REQUEST_REFERENCE" VARCHAR2(37),
	"CREATED_DATE" TIMESTAMP (6),
	"UPDATED_DATE" TIMESTAMP (6),
	"COMPLETED_DATE" TIMESTAMP (6),
	"FORWARDING_ATTEMPTS" NUMBER(*,0),
	"DEAD_LETTER" CHAR(1),
	"INTERNAL_SYSTEM_ERROR" VARCHAR2(4000),
	"REQUEST_TYPE" VARCHAR2(50),
	"VERSION_NUMBER" NUMBER(*,0) DEFAULT 0,
	"INDIVIDUAL_PAYLOAD" BLOB,
	"TARGET_APPLICATION_RESPONSE" BLOB
   ) ;



  CREATE TABLE "SDT_OWNER"."SERVICE_REQUESTS"
   (	"SERVICE_REQUEST_ID" NUMBER(*,0),
	"REQUEST_PAYLOAD" BLOB,
	"REQUEST_TIMESTAMP" TIMESTAMP (6),
	"RESPONSE_PAYLOAD" BLOB,
	"RESPONSE_TIMESTAMP" TIMESTAMP (6),
	"REQUEST_TYPE" VARCHAR2(32),
	"SDT_CUSTOMER_ID" VARCHAR2(32),
	"SDT_BULK_REFERENCE" VARCHAR2(29),
	"SERVER_HOST_NAME" VARCHAR2(255),
	"VERSION_NUMBER" NUMBER(*,0) DEFAULT 0
   ) ;



  CREATE TABLE "SDT_OWNER"."SERVICE_ROUTINGS"
   (	"SERVICE_ROUTINGS_ID" NUMBER(*,0),
	"SERVICE_TYPE_ID" NUMBER(*,0),
	"TARGET_APPLICATION_ID" NUMBER(*,0),
	"WEB_SERVICE_ENDPOINT" VARCHAR2(255),
	"VERSION_NUMBER" NUMBER(*,0) DEFAULT 0
   ) ;



  CREATE TABLE "SDT_OWNER"."SERVICE_TYPES"
   (	"SERVICE_TYPE_ID" NUMBER(*,0),
	"SERVICE_TYPE_NAME" VARCHAR2(50),
	"SERVICE_TYPE_STATUS" VARCHAR2(1),
	"SERVICE_TYPE_DESCRIPTION" VARCHAR2(2000),
	"VERSION_NUMBER" NUMBER(*,0) DEFAULT 0
   ) ;



  CREATE TABLE "SDT_OWNER"."TARGET_APPLICATIONS"
   (	"TARGET_APPLICATION_ID" NUMBER(*,0),
	"TARGET_APPLICATION_CODE" VARCHAR2(4),
	"TARGET_APPLICATION_NAME" VARCHAR2(255),
	"VERSION_NUMBER" NUMBER(*,0) DEFAULT 0
   ) ;


