alter session set current_schema=sdt_owner;


--
-- bulk_customers
--

ALTER TABLE bulk_customers                ADD CONSTRAINT bc_sci_nn CHECK (sdt_customer_id IS NOT NULL);
ALTER TABLE bulk_customers                ADD CONSTRAINT bc_vn_nn  CHECK (version_number  IS NOT NULL);

--
-- bulk_customer_applications
--

ALTER TABLE bulk_customer_applications    ADD CONSTRAINT bca_cai_nn CHECK (customer_application_id IS NOT NULL);
ALTER TABLE bulk_customer_applications    ADD CONSTRAINT bca_vn_nn  CHECK (version_number          IS NOT NULL);


--
-- bulk_submissions
--

ALTER TABLE bulk_submissions              ADD CONSTRAINT bs_bci_nn CHECK (bulk_customer_id       IS NOT NULL);
ALTER TABLE bulk_submissions              ADD CONSTRAINT bs_tai_nn CHECK (target_application_id  IS NOT NULL);
ALTER TABLE bulk_submissions              ADD CONSTRAINT bs_sbr_nn CHECK (sdt_bulk_reference     IS NOT NULL);
ALTER TABLE bulk_submissions              ADD CONSTRAINT bs_nor_nn CHECK (number_of_requests     IS NOT NULL);
ALTER TABLE bulk_submissions              ADD CONSTRAINT bs_bss_nn CHECK (bulk_submission_status IS NOT NULL);
ALTER TABLE bulk_submissions              ADD CONSTRAINT bs_cd_nn  CHECK (created_date           IS NOT NULL);
ALTER TABLE bulk_submissions              ADD CONSTRAINT bs_bp_nn  CHECK (bulk_payload           IS NOT NULL);
ALTER TABLE bulk_submissions              ADD CONSTRAINT bs_vn_nn  CHECK (version_number         IS NOT NULL);

ALTER TABLE bulk_submissions              ADD CONSTRAINT bs_sbr_uni UNIQUE (sdt_bulk_reference );

--
-- error_logs
--

ALTER TABLE error_logs                    ADD CONSTRAINT el_bsi_nn CHECK (bulk_submission_id IS NOT NULL);
ALTER TABLE error_logs                    ADD CONSTRAINT el_ec_nn  CHECK (error_code         IS NOT NULL);
ALTER TABLE error_logs                    ADD CONSTRAINT el_cd_nn  CHECK (created_date       IS NOT NULL);
ALTER TABLE error_logs                    ADD CONSTRAINT el_et_nn  CHECK (error_text         IS NOT NULL);
ALTER TABLE error_logs                    ADD CONSTRAINT el_vn_nn  CHECK (version_number     IS NOT NULL);

--
-- error_messages
--

ALTER TABLE error_messages                ADD CONSTRAINT em_ec_nn  CHECK (error_code        IS NOT NULL);
ALTER TABLE error_messages                ADD CONSTRAINT em_et_nn  CHECK (error_text        IS NOT NULL);
ALTER TABLE error_messages                ADD CONSTRAINT em_ed_nn  CHECK (error_description IS NOT NULL);
ALTER TABLE error_messages                ADD CONSTRAINT em_vn_nn  CHECK (version_number    IS NOT NULL);

--
-- global_parameters
--

ALTER TABLE global_parameters             ADD CONSTRAINT gp_pn_nn  CHECK (parameter_name  IS NOT NULL);
ALTER TABLE global_parameters             ADD CONSTRAINT gp_pv_nn  CHECK (parameter_value IS NOT NULL);
ALTER TABLE global_parameters             ADD CONSTRAINT gp_vn_nn  CHECK (version_number  IS NOT NULL);

--
-- individual_requests
--

ALTER TABLE individual_requests           ADD CONSTRAINT ir_bsi_nn CHECK (bulk_submission_id    IS NOT NULL);
ALTER TABLE individual_requests           ADD CONSTRAINT ir_rs_nn  CHECK (request_status        IS NOT NULL);
ALTER TABLE individual_requests           ADD CONSTRAINT ir_sbr_nn CHECK (sdt_bulk_reference    IS NOT NULL);
ALTER TABLE individual_requests           ADD CONSTRAINT ir_ln_nn  CHECK (line_number           IS NOT NULL);
ALTER TABLE individual_requests           ADD CONSTRAINT ir_srr_nn CHECK (sdt_request_reference IS NOT NULL);
ALTER TABLE individual_requests           ADD CONSTRAINT ir_cd_nn  CHECK (created_date          IS NOT NULL);
ALTER TABLE individual_requests           ADD CONSTRAINT ir_ip_nn  CHECK (individual_payload    IS NOT NULL);
ALTER TABLE individual_requests           ADD CONSTRAINT ir_vn_nn  CHECK (version_number        IS NOT NULL);


--
-- service_routings
--

ALTER TABLE service_routings              ADD CONSTRAINT sr_wse_nn CHECK (web_service_endpoint IS NOT NULL);
ALTER TABLE service_routings              ADD CONSTRAINT sr_vn_nn  CHECK (version_number       IS NOT NULL);

--
-- service_types
--

ALTER TABLE service_types                 ADD CONSTRAINT st_stn_nn CHECK (service_type_name   IS NOT NULL);
ALTER TABLE service_types                 ADD CONSTRAINT st_sts_nn CHECK (service_type_status IS NOT NULL);
ALTER TABLE service_types                 ADD CONSTRAINT st_vn_nn  CHECK (version_number      IS NOT NULL);

ALTER TABLE service_types                 ADD CONSTRAINT rt_rtn_uni UNIQUE (service_type_name) ;


--
-- target_applications
--

ALTER TABLE target_applications          ADD CONSTRAINT ta_tac_nn  CHECK (target_application_code IS NOT NULL);
ALTER TABLE target_applications          ADD CONSTRAINT ta_vn_nn   CHECK (version_number          IS NOT NULL);

ALTER TABLE target_applications          ADD CONSTRAINT ta_tan_uni UNIQUE (target_application_name) ;
