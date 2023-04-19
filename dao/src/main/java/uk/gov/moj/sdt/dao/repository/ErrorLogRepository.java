package uk.gov.moj.sdt.dao.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.moj.sdt.domain.ErrorLog;

@Repository
public interface ErrorLogRepository extends CrudRepository<ErrorLog, Long> {
}
