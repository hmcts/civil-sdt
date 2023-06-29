package uk.gov.moj.sdt.interceptors.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import uk.gov.moj.sdt.dao.ServiceRequestDao;
import uk.gov.moj.sdt.domain.api.IDomainObject;

public class PersistServiceRequestImpl implements IPersistServiceRequest {

    private ServiceRequestDao serviceRequestDao;

    @Autowired
    public PersistServiceRequestImpl(ServiceRequestDao serviceRequestDao) {
        this.serviceRequestDao = serviceRequestDao;
    }

    @Override
    public void persist(Object domainObject) throws DataAccessException {
        serviceRequestDao.persist(domainObject);
    }

    @Override
    public <D extends IDomainObject> D fetch(Class<D> domainType, long id) throws DataAccessException {
        return serviceRequestDao.fetch(domainType, id);
    }
}
