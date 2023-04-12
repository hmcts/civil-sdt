
  CREATE INDEX "PUBLIC"."BCA_BULK_CUSTOMER_I" ON "PUBLIC"."BULK_CUSTOMER_APPLICATIONS" ("BULK_CUSTOMER_ID")
  ;



  CREATE INDEX "PUBLIC"."BCA_TARGET_APPLICATION_I" ON "PUBLIC"."BULK_CUSTOMER_APPLICATIONS" ("TARGET_APPLICATION_ID")
  ;



  CREATE INDEX "PUBLIC"."BS_BULK_CUSTOMER_ID_I" ON "PUBLIC"."BULK_SUBMISSIONS" ("BULK_CUSTOMER_ID")
  ;



  CREATE INDEX "PUBLIC"."BS_SDT_BULK_REFERENCE_I" ON "PUBLIC"."BULK_SUBMISSIONS" ("SDT_BULK_REFERENCE")
  ;



  CREATE INDEX "PUBLIC"."BS_SERVICE_REQUEST_ID_I" ON "PUBLIC"."BULK_SUBMISSIONS" ("SERVICE_REQUEST_ID")
  ;



  CREATE INDEX "PUBLIC"."BS_TARGET_APPLICATION_ID_I" ON "PUBLIC"."BULK_SUBMISSIONS" ("TARGET_APPLICATION_ID")
  ;



  CREATE INDEX "PUBLIC"."BULK_CUSTOMERS_PK" ON "PUBLIC"."BULK_CUSTOMERS" ("BULK_CUSTOMER_ID")
  ;



  CREATE INDEX "PUBLIC"."BULK_CUSTOMER_APPLICATIONS_PK" ON "PUBLIC"."BULK_CUSTOMER_APPLICATIONS" ("BULK_CUSTOMER_APPLICATIONS_ID")
  ;



  CREATE INDEX "PUBLIC"."BULK_SUBMISSIONS_PK" ON "PUBLIC"."BULK_SUBMISSIONS" ("BULK_SUBMISSION_ID")
  ;



  CREATE INDEX "PUBLIC"."EL_INDIVIDUAL_REQUEST_ID_I" ON "PUBLIC"."ERROR_LOGS" ("INDIVIDUAL_REQUEST_ID")
  ;



  CREATE INDEX "PUBLIC"."ERROR_LOGS_PK" ON "PUBLIC"."ERROR_LOGS" ("ERROR_LOG_ID")
  ;



  CREATE INDEX "PUBLIC"."ERROR_MESSAGES_PK" ON "PUBLIC"."ERROR_MESSAGES" ("ERROR_MESSAGE_ID")
  ;



  CREATE INDEX "PUBLIC"."GLOBAL_PARAMETERS_PK" ON "PUBLIC"."GLOBAL_PARAMETERS" ("GLOBAL_PARAMETER_ID")
  ;



  CREATE INDEX "PUBLIC"."INDIVIDUAL_REQUESTS_PK" ON "PUBLIC"."INDIVIDUAL_REQUESTS" ("INDIVIDUAL_REQUEST_ID")
  ;



  CREATE INDEX "PUBLIC"."IR_BULK_REFERENCE_I" ON "PUBLIC"."INDIVIDUAL_REQUESTS" ("SDT_BULK_REFERENCE")
  ;



  CREATE INDEX "PUBLIC"."IR_BULK_SUBMISSION_ID_I" ON "PUBLIC"."INDIVIDUAL_REQUESTS" ("BULK_SUBMISSION_ID")
  ;



  CREATE INDEX "PUBLIC"."IR_LWR_CUSTOMER_REQ_REF_CD_I" ON "PUBLIC"."INDIVIDUAL_REQUESTS" (LOWER("CUSTOMER_REQUEST_REF"), "CREATED_DATE")
  ;



  CREATE INDEX "PUBLIC"."IR_SDT_REQUEST_REFERENCE_I" ON "PUBLIC"."INDIVIDUAL_REQUESTS" ("SDT_REQUEST_REFERENCE")
  ;



  CREATE INDEX "PUBLIC"."SERVICE_REQUESTS_PK" ON "PUBLIC"."SERVICE_REQUESTS" ("SERVICE_REQUEST_ID")
  ;



  CREATE INDEX "PUBLIC"."SERVICE_ROUTINGS_PK" ON "PUBLIC"."SERVICE_ROUTINGS" ("SERVICE_ROUTINGS_ID")
  ;



  CREATE INDEX "PUBLIC"."SERVICE_TYPES_PK" ON "PUBLIC"."SERVICE_TYPES" ("SERVICE_TYPE_ID")
  ;



  CREATE INDEX "PUBLIC"."SR_SERVICE_TYPE_I" ON "PUBLIC"."SERVICE_ROUTINGS" ("SERVICE_TYPE_ID")
  ;



  CREATE INDEX "PUBLIC"."SR_TARGET_APPLICATION_I" ON "PUBLIC"."SERVICE_ROUTINGS" ("TARGET_APPLICATION_ID")
  ;



  CREATE INDEX "PUBLIC"."ST_SERVICE_TYPE_NAME" ON "PUBLIC"."SERVICE_TYPES" ("SERVICE_TYPE_NAME")
  ;



  CREATE INDEX "PUBLIC"."TARGET_APPLICATIONS_PK" ON "PUBLIC"."TARGET_APPLICATIONS" ("TARGET_APPLICATION_ID")
  ;



  CREATE INDEX "PUBLIC"."TA_TARGET_APPLICATION_NAME" ON "PUBLIC"."TARGET_APPLICATIONS" ("TARGET_APPLICATION_NAME")
  ;


