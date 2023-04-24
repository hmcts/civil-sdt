INSERT INTO public.bulk_customers(
    bulk_customer_id, sdt_customer_id, version_number, ready_for_alternate_service)
    VALUES (1, 12345678, 1, false);

INSERT INTO public.bulk_customer_applications(
    bulk_customer_applications_id, bulk_customer_id, target_application_id, customer_application_id, version_number)
    VALUES (1, 1, 1, 'MC0000000001', 0);
