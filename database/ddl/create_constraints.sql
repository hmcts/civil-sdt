connect sdt_owner/sdt_owner

--
-- bulk_submissions
--

ALTER TABLE bulk_submissions    ADD CONSTRAINT bs_sbr_nn CHECK (sdt_bulk_reference IS NOT NULL);
ALTER TABLE bulk_submissions    ADD CONSTRAINT bs_sbr_uni UNIQUE (sdt_bulk_reference );


--
-- individual_requests
--

ALTER TABLE individual_requests ADD CONSTRAINT ir_sbr_nn CHECK (sdt_bulk_reference IS NOT NULL);

--
-- request_types
--

ALTER TABLE request_types       ADD CONSTRAINT rt_rtn_uni UNIQUE (request_type_name) ;


--
--target_applications
--

ALTER TABLE target_applications ADD CONSTRAINT ta_target_application_name_uni UNIQUE (target_application_name) ;
