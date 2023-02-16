package uk.gov.moj.sdt.cmc.consumers.model;

public interface ICmcRequest {

    String getIdAmId();

    String getFromDateTime();

    String getToDateTime();
}
