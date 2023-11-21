DO $$

DECLARE

  SEQUENCE_NAME CONSTANT int = 1;
  PRIMARY_KEY_COLUMN_NAME CONSTANT int = 2;
  TABLE_NAME CONSTANT int = 3;

  new_seq_value bigint;
  sequence_data text[];
  sequence text[];

BEGIN

  sequence_data = ARRAY[
    ['bulk_cust_app_seq', 'bulk_customer_applications_id', 'bulk_customer_applications'],
    ['bulk_cust_seq', 'bulk_customer_id', 'bulk_customers'],
    ['bulk_sub_seq', 'bulk_submission_id', 'bulk_submissions'],
    ['err_log_seq', 'error_log_id', 'error_logs'],
    ['err_mesg_seq', 'error_message_id', 'error_messages'],
    ['glb_par_seq', 'global_parameter_id', 'global_parameters'],
    ['ind_req_seq', 'individual_request_id', 'individual_requests'],
    ['ser_rou_seq', 'service_routings_id', 'service_routings'],
    ['ser_typ_seq', 'service_type_id', 'service_types'],
    ['srv_req_seq', 'service_request_id', 'service_requests'],
    ['tar_app_seq', 'target_application_id', 'target_applications']
  ];

  FOREACH sequence SLICE 1 IN ARRAY sequence_data
  LOOP

    -- Get highest value from primary key column that sequence is used to populate and add one to get next available value
    EXECUTE 'SELECT COALESCE(MAX(' || sequence[PRIMARY_KEY_COLUMN_NAME] || '), 0) + 1 FROM ' || sequence[TABLE_NAME]
    INTO new_seq_value;

    -- Update sequence so that next value is the next available value
    RAISE NOTICE 'Resetting % sequence value to %', sequence[SEQUENCE_NAME], new_seq_value;
    EXECUTE 'ALTER SEQUENCE ' || sequence[SEQUENCE_NAME] || ' RESTART WITH ' || new_seq_value;

  END LOOP;

END$$;
