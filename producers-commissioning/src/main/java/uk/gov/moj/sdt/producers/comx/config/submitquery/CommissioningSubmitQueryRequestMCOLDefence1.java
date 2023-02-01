package uk.gov.moj.sdt.producers.comx.config.submitquery;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.moj.sdt.domain.SubmitQueryRequest;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;

@Configuration
public class CommissioningSubmitQueryRequestMCOLDefence1 {
    @Bean
    @Qualifier("ISubmitQueryRequestMCOLDefence1")
    public static ISubmitQueryRequest submitQueryRequestMCOLDefence1() {
        SubmitQueryRequest submitQueryRequest = new SubmitQueryRequest();
        submitQueryRequest.setResultCount(6);
        submitQueryRequest.setTargetApplicationResponse(
            """
                <![CDATA[
                      <sub:mcolDefenceDetail xmlns:sub="http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryResponseSchema" xmlns:mquer="http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema">
                <mquer:claimNumber>A0ZZ1234</mquer:claimNumber>
                        <mquer:defendantResponse defendantId="1">
                              <mquer:filedDate>2013-12-18</mquer:filedDate>
                                <mquer:eventCreatedDateOnMcol>2013-12-19T10:04:21</mquer:eventCreatedDateOnMcol>
                                <mquer:raisedOnMcol>true</mquer:raisedOnMcol>
                              <mquer:responseType>DE</mquer:responseType>
                              <mquer:defence>MCOL entered defence text defendant 1</mquer:defence>
                        </mquer:defendantResponse>
                      </sub:mcolDefenceDetail>
                      <sub:mcolDefenceDetail xmlns:sub="http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryResponseSchema" xmlns:mquer="http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema">
                        <mquer:claimNumber>A0ZZ1234</mquer:claimNumber>
                        <mquer:defendantResponse defendantId="2">
                                <mquer:filedDate>2013-12-18</mquer:filedDate>
                              <mquer:eventCreatedDateOnMcol>2013-12-19T10:45:58</mquer:eventCreatedDateOnMcol>
                                <mquer:raisedOnMcol>true</mquer:raisedOnMcol>
                              <mquer:responseType>DE</mquer:responseType>
                              <mquer:defence>MCOL entered defence text defendant 2</mquer:defence>
                        </mquer:defendantResponse>
                      </sub:mcolDefenceDetail>
                      <sub:mcolDefenceDetail xmlns:sub="http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryResponseSchema" xmlns:mquer="http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema">
                        <mquer:claimNumber>A0ZZ1235</mquer:claimNumber>
                        <mquer:defendantResponse defendantId="1">
                                <mquer:filedDate>2013-12-19</mquer:filedDate>
                          <mquer:eventCreatedDateOnMcol>2013-12-19T12:04:21</mquer:eventCreatedDateOnMcol>
                                <mquer:raisedOnMcol>false</mquer:raisedOnMcol>
                          <mquer:responseType>PA</mquer:responseType>
                        </mquer:defendantResponse>
                      </sub:mcolDefenceDetail>
                      <sub:mcolDefenceDetail xmlns:sub="http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryResponseSchema" xmlns:mquer="http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema">
                        <mquer:claimNumber>A0ZZ1236</mquer:claimNumber>
                        <mquer:defendantResponse defendantId="1">
                                <mquer:filedDate>2013-12-19</mquer:filedDate>
                              <mquer:eventCreatedDateOnMcol>2013-12-19T13:32:09</mquer:eventCreatedDateOnMcol>
                                <mquer:raisedOnMcol>false</mquer:raisedOnMcol>
                              <mquer:responseType>DC</mquer:responseType>
                        </mquer:defendantResponse>
                      </sub:mcolDefenceDetail>
                      <sub:mcolDefenceDetail xmlns:sub="http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryResponseSchema" xmlns:mquer="http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema">
                        <mquer:claimNumber>A0ZZ1237</mquer:claimNumber>
                        <mquer:defendantResponse defendantId="1">
                                <mquer:filedDate>2013-12-19</mquer:filedDate>
                          <mquer:eventCreatedDateOnMcol>2013-12-19T13:56:47</mquer:eventCreatedDateOnMcol>
                                <mquer:raisedOnMcol>false</mquer:raisedOnMcol>
                          <mquer:responseType>DC</mquer:responseType>
                        </mquer:defendantResponse>
                      </sub:mcolDefenceDetail>
                        <sub:mcolDefenceDetail xmlns:sub="http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryResponseSchema" xmlns:mquer="http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema">
                            <mquer:claimNumber>A0ZZ1238</mquer:claimNumber>
                            <mquer:defendantResponse defendantId="1">
                                <mquer:filedDate>2013-12-19</mquer:filedDate>
                                <mquer:eventCreatedDateOnMcol>2013-12-19T14:04:23</mquer:eventCreatedDateOnMcol>
                                <mquer:raisedOnMcol>true</mquer:raisedOnMcol>
                                <mquer:responseType>AS</mquer:responseType>
                            </mquer:defendantResponse>
                        </sub:mcolDefenceDetail>
                    ]]>
                """
        );

        return submitQueryRequest;
    }
}
