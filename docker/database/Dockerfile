FROM postgres:15

COPY init-db.sh /docker-entrypoint-initdb.d

HEALTHCHECK --interval=10s --timeout=10s --retries=10 CMD psql -c 'select 1' -d civil_sdt -U ${SDT_DB_USERNAME}

EXPOSE 5432
