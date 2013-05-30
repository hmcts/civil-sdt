connect sdt_owner/sdt_owner

ALTER TABLE bulk_submissions    ADD CONSTRAINT bs_sbr_nn CHECK (sdt_bulk_reference IS NOT NULL);
ALTER TABLE bulk_submissions    ADD CONSTRAINT bs_sbr_uni UNIQUE (sdt_bulk_reference );
ALTER TABLE individual_requests ADD CONSTRAINT ir_sbr_nn CHECK (sdt_bulk_reference IS NOT NULL);

