
  ALTER TABLE "PUBLIC"."BULK_CUSTOMER_APPLICATIONS" ADD CONSTRAINT "BCA_CAI_NN" CHECK (customer_application_id IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."BULK_CUSTOMER_APPLICATIONS" ADD CONSTRAINT "BCA_VN_NN" CHECK (version_number          IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."BULK_CUSTOMERS" ADD CONSTRAINT "BC_SCI_NN" CHECK (sdt_customer_id IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."BULK_CUSTOMERS" ADD CONSTRAINT "BC_VN_NN" CHECK (version_number  IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."BULK_SUBMISSIONS" ADD CONSTRAINT "BS_BCI_NN" CHECK (bulk_customer_id       IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."BULK_SUBMISSIONS" ADD CONSTRAINT "BS_BP_NN" CHECK (bulk_payload           IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."BULK_SUBMISSIONS" ADD CONSTRAINT "BS_BSS_NN" CHECK (bulk_submission_status IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."BULK_SUBMISSIONS" ADD CONSTRAINT "BS_CD_NN" CHECK (created_date           IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."BULK_SUBMISSIONS" ADD CONSTRAINT "BS_NOR_NN" CHECK (number_of_requests     IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."BULK_SUBMISSIONS" ADD CONSTRAINT "BS_SBR_NN" CHECK (sdt_bulk_reference     IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."BULK_SUBMISSIONS" ADD CONSTRAINT "BS_TAI_NN" CHECK (target_application_id  IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."BULK_SUBMISSIONS" ADD CONSTRAINT "BS_VN_NN" CHECK (version_number         IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."ERROR_LOGS" ADD CONSTRAINT "EL_CD_NN" CHECK (created_date       IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."ERROR_LOGS" ADD CONSTRAINT "EL_EC_NN" CHECK (error_code         IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."ERROR_LOGS" ADD CONSTRAINT "EL_ET_NN" CHECK (error_text         IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."ERROR_LOGS" ADD CONSTRAINT "EL_IRI_NN" CHECK (individual_request_id IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."ERROR_LOGS" ADD CONSTRAINT "EL_VN_NN" CHECK (version_number     IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."ERROR_MESSAGES" ADD CONSTRAINT "EM_EC_NN" CHECK (error_code        IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."ERROR_MESSAGES" ADD CONSTRAINT "EM_ED_NN" CHECK (error_description IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."ERROR_MESSAGES" ADD CONSTRAINT "EM_ET_NN" CHECK (error_text        IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."ERROR_MESSAGES" ADD CONSTRAINT "EM_VN_NN" CHECK (version_number    IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."GLOBAL_PARAMETERS" ADD CONSTRAINT "GP_PN_NN" CHECK (parameter_name  IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."GLOBAL_PARAMETERS" ADD CONSTRAINT "GP_PV_NN" CHECK (parameter_value IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."GLOBAL_PARAMETERS" ADD CONSTRAINT "GP_VN_NN" CHECK (version_number  IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."INDIVIDUAL_REQUESTS" ADD CONSTRAINT "IR_BSI_NN" CHECK (bulk_submission_id    IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."INDIVIDUAL_REQUESTS" ADD CONSTRAINT "IR_CD_NN" CHECK (created_date          IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."INDIVIDUAL_REQUESTS" ADD CONSTRAINT "IR_DL_NN" CHECK (dead_letter           IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."INDIVIDUAL_REQUESTS" ADD CONSTRAINT "IR_LN_NN" CHECK (line_number           IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."INDIVIDUAL_REQUESTS" ADD CONSTRAINT "IR_RS_NN" CHECK (request_status        IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."INDIVIDUAL_REQUESTS" ADD CONSTRAINT "IR_SBR_NN" CHECK (sdt_bulk_reference    IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."INDIVIDUAL_REQUESTS" ADD CONSTRAINT "IR_SRR_NN" CHECK (sdt_request_reference IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."INDIVIDUAL_REQUESTS" ADD CONSTRAINT "IR_VN_NN" CHECK (version_number        IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."SERVICE_REQUESTS" ADD CONSTRAINT "SRE_RT_NN" CHECK (request_timestamp    IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."SERVICE_ROUTINGS" ADD CONSTRAINT "SR_VN_NN" CHECK (version_number       IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."SERVICE_ROUTINGS" ADD CONSTRAINT "SR_WSE_NN" CHECK (web_service_endpoint IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."SERVICE_TYPES" ADD CONSTRAINT "ST_STN_NN" CHECK (service_type_name   IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."SERVICE_TYPES" ADD CONSTRAINT "ST_STS_NN" CHECK (service_type_status IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."SERVICE_TYPES" ADD CONSTRAINT "ST_VN_NN" CHECK (version_number      IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."TARGET_APPLICATIONS" ADD CONSTRAINT "TA_TAC_NN" CHECK (target_application_code IS NOT NULL) ENABLE;



  ALTER TABLE "PUBLIC"."TARGET_APPLICATIONS" ADD CONSTRAINT "TA_VN_NN" CHECK (version_number          IS NOT NULL) ENABLE;

