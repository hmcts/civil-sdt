connect sdt_owner/sdt_owner


DEFINE bs_sdt_bulk_reference_i = 'TABLESPACE users'
DEFINE ir_bulk_reference_i     = 'TABLESPACE users'

CREATE INDEX bs_sdt_bulk_reference_i
ON bulk_submissions ( sdt_bulk_reference )
&bs_sdt_bulk_reference_i
;

CREATE INDEX ir_bulk_reference_i
ON individual_requests ( sdt_bulk_reference )
&ir_bulk_reference_i
;
