
  CREATE INDEX "SDT_OWNER"."BCA_BULK_CUSTOMER_I" ON "SDT_OWNER"."BULK_CUSTOMER_APPLICATIONS" ("BULK_CUSTOMER_ID")
  ;



  CREATE INDEX "SDT_OWNER"."BCA_TARGET_APPLICATION_I" ON "SDT_OWNER"."BULK_CUSTOMER_APPLICATIONS" ("TARGET_APPLICATION_ID")
  ;



  CREATE INDEX "SDT_OWNER"."BS_BULK_CUSTOMER_ID_I" ON "SDT_OWNER"."BULK_SUBMISSIONS" ("BULK_CUSTOMER_ID")
  ;



  CREATE INDEX "SDT_OWNER"."BS_SDT_BULK_REFERENCE_I" ON "SDT_OWNER"."BULK_SUBMISSIONS" ("SDT_BULK_REFERENCE")
  ;



  CREATE INDEX "SDT_OWNER"."BS_SERVICE_REQUEST_ID_I" ON "SDT_OWNER"."BULK_SUBMISSIONS" ("SERVICE_REQUEST_ID")
  ;



  CREATE INDEX "SDT_OWNER"."BS_TARGET_APPLICATION_ID_I" ON "SDT_OWNER"."BULK_SUBMISSIONS" ("TARGET_APPLICATION_ID")
  ;



  CREATE INDEX "SDT_OWNER"."BULK_CUSTOMERS_PK" ON "SDT_OWNER"."BULK_CUSTOMERS" ("BULK_CUSTOMER_ID")
  ;



  CREATE INDEX "SDT_OWNER"."BULK_CUSTOMER_APPLICATIONS_PK" ON "SDT_OWNER"."BULK_CUSTOMER_APPLICATIONS" ("BULK_CUSTOMER_APPLICATIONS_ID")
  ;



  CREATE INDEX "SDT_OWNER"."BULK_SUBMISSIONS_PK" ON "SDT_OWNER"."BULK_SUBMISSIONS" ("BULK_SUBMISSION_ID")
  ;



  CREATE INDEX "SDT_OWNER"."EL_INDIVIDUAL_REQUEST_ID_I" ON "SDT_OWNER"."ERROR_LOGS" ("INDIVIDUAL_REQUEST_ID")
  ;



  CREATE INDEX "SDT_OWNER"."ERROR_LOGS_PK" ON "SDT_OWNER"."ERROR_LOGS" ("ERROR_LOG_ID")
  ;



  CREATE INDEX "SDT_OWNER"."ERROR_MESSAGES_PK" ON "SDT_OWNER"."ERROR_MESSAGES" ("ERROR_MESSAGE_ID")
  ;



  CREATE INDEX "SDT_OWNER"."GLOBAL_PARAMETERS_PK" ON "SDT_OWNER"."GLOBAL_PARAMETERS" ("GLOBAL_PARAMETER_ID")
  ;



  CREATE INDEX "SDT_OWNER"."INDIVIDUAL_REQUESTS_PK" ON "SDT_OWNER"."INDIVIDUAL_REQUESTS" ("INDIVIDUAL_REQUEST_ID")
  ;



  CREATE INDEX "SDT_OWNER"."IR_BULK_REFERENCE_I" ON "SDT_OWNER"."INDIVIDUAL_REQUESTS" ("SDT_BULK_REFERENCE")
  ;



  CREATE INDEX "SDT_OWNER"."IR_BULK_SUBMISSION_ID_I" ON "SDT_OWNER"."INDIVIDUAL_REQUESTS" ("BULK_SUBMISSION_ID")
  ;



  CREATE INDEX "SDT_OWNER"."IR_LWR_CUSTOMER_REQ_REF_CD_I" ON "SDT_OWNER"."INDIVIDUAL_REQUESTS" (LOWER("CUSTOMER_REQUEST_REF"), "CREATED_DATE")
  ;



  CREATE INDEX "SDT_OWNER"."IR_SDT_REQUEST_REFERENCE_I" ON "SDT_OWNER"."INDIVIDUAL_REQUESTS" ("SDT_REQUEST_REFERENCE")
  ;



  CREATE INDEX "SDT_OWNER"."SERVICE_REQUESTS_PK" ON "SDT_OWNER"."SERVICE_REQUESTS" ("SERVICE_REQUEST_ID")
  ;



  CREATE INDEX "SDT_OWNER"."SERVICE_ROUTINGS_PK" ON "SDT_OWNER"."SERVICE_ROUTINGS" ("SERVICE_ROUTINGS_ID")
  ;



  CREATE INDEX "SDT_OWNER"."SERVICE_TYPES_PK" ON "SDT_OWNER"."SERVICE_TYPES" ("SERVICE_TYPE_ID")
  ;



  CREATE INDEX "SDT_OWNER"."SR_SERVICE_TYPE_I" ON "SDT_OWNER"."SERVICE_ROUTINGS" ("SERVICE_TYPE_ID")
  ;



  CREATE INDEX "SDT_OWNER"."SR_TARGET_APPLICATION_I" ON "SDT_OWNER"."SERVICE_ROUTINGS" ("TARGET_APPLICATION_ID")
  ;



  CREATE INDEX "SDT_OWNER"."ST_SERVICE_TYPE_NAME" ON "SDT_OWNER"."SERVICE_TYPES" ("SERVICE_TYPE_NAME")
  ;



  CREATE INDEX "SDT_OWNER"."TARGET_APPLICATIONS_PK" ON "SDT_OWNER"."TARGET_APPLICATIONS" ("TARGET_APPLICATION_ID")
  ;



  CREATE INDEX "SDT_OWNER"."TA_TARGET_APPLICATION_NAME" ON "SDT_OWNER"."TARGET_APPLICATIONS" ("TARGET_APPLICATION_NAME")
  ;


