package uk.gov.moj.sdt.cmc.consumers.model;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class McolDefenceDetailTypes {

    @XmlElement(required = false)
    protected List<McolDefenceDetailType> mcolDefenceDetailTypeList;

    public void setMcolDefenceDetailTypeList(List<McolDefenceDetailType> mcolDefenceDetailTypeList) {
        this.mcolDefenceDetailTypeList = mcolDefenceDetailTypeList;
    }

}
