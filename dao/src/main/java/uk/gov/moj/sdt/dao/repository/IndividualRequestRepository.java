package uk.gov.moj.sdt.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.moj.sdt.domain.IndividualRequest;

@Repository
public interface IndividualRequestRepository extends JpaRepository<IndividualRequest, Long> {

    IndividualRequest findBySdtRequestReference(String sdtRequestReference);
}
