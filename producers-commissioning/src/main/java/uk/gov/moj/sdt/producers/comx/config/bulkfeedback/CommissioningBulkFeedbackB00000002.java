package uk.gov.moj.sdt.producers.comx.config.bulkfeedback;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.moj.sdt.domain.BulkSubmission;
import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.producers.comx.utils.BulkFeedbackFactory;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class CommissioningBulkFeedbackB00000002 {
    @Bean
    @Qualifier("IBulkSumissionB00000002")
    private static IBulkSubmission bulkSumissionB00000002() {
        BulkSubmission bulkSubmission = new BulkSubmission();
        bulkSubmission.setCustomerReference("USER_FILE_REFERENCE_B2");
        bulkSubmission.setCreatedDate(LocalDateTime.of(2014, 1, 22, 13, 0));
        bulkSubmission.setNumberOfRequest(19);
        bulkSubmission.markAsValidated();
        return bulkSubmission;
    }

    @Bean
    @Qualifier("BulkFeedbackFactoryB00000002")
    private static BulkFeedbackFactory bulkFeedbackFactoryB00000002(@Qualifier("IBulkSumissionB00000002")
                                                            IBulkSubmission bulkSubmission) {
        return new BulkFeedbackFactory(bulkSubmission);
    }

    @Bean
    @Qualifier("createIndividualRequestB00000002")
    public static MethodInvokingFactoryBean createIndividualRequestB00000002() {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setTargetObject(bulkFeedbackFactoryB00000002(bulkSumissionB00000002()));
        methodInvokingFactoryBean.setTargetMethod("createIndividualRequests");
        List<List<String>> arguments = List.of(
            List.of("USER_REQUEST_ID_B1a", "mcolClaim", "Rejected", "DUP_CUST_REQID", "Duplicate User Request Identifier submitted USER_REQUEST_ID_B1a.", ""),
            List.of("USER_REQUEST_ID_B1b", "mcolJudgment", "Rejected", "DUPLD_CUST_REQID", "Unique Request Identifier has been specified more than once within the originating Bulk Request.", ""),
            List.of("USER_REQUEST_ID_B1b", "mcolJudgment", "Rejected", "DUPLD_CUST_REQID", "Unique Request Identifier has been specified more than once within the originating Bulk Request.", ""),
            List.of("USER_REQUEST_ID_B2", "mcolClaim", "Rejected", "8", "First defendant's postcode is not in England or Wales.", ""),
            List.of("USER_REQUEST_ID_B3", "mcolJudgment", "Rejected", "75", "Rejected by CCBC - rejection number 6.", ""),
            List.of("USER_REQUEST_ID_B4", "mcolClaimStatusUpdate", "Accepted", "", "", ""),
            List.of("USER_REQUEST_ID_B5", "mcolJudgment", "Rejected", "24", "This judgment request is invalid on the referenced claim.", ""),
            List.of("USER_REQUEST_ID_B6", "mcolWarrant", "Accepted", "", "",
                    """
                        <![CDATA[
                            <ind:mcolResponseDetail xmlns:ind="http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema" xmlns:mresp="http://ws.sdt.moj.gov.uk/2013/mcol/ResponseDetailSchema">
                              <mresp:issueDate>2014-01-23</mresp:issueDate>
                                <mresp:warrantNumber>0Z000150</mresp:warrantNumber>
                                <mresp:enforcingCourtCode>127</mresp:enforcingCourtCode>
                                <mresp:enforcingCourtName>BIRMINGHAM</mresp:enforcingCourtName>
                                <mresp:fee>10000</mresp:fee>
                                <mresp:judgmentWarrantStatus>Warrant accepted by CCBC</mresp:judgmentWarrantStatus>
                            </ind:mcolResponseDetail>
                        ]]>
                        """),
            List.of("USER_REQUEST_ID_B7", "mcolJudgmentWarrant", "Accepted", "", "",
                    """
                        <![CDATA[
                            <ind:mcolResponseDetail xmlns:ind="http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema" xmlns:mresp="http://ws.sdt.moj.gov.uk/2013/mcol/ResponseDetailSchema">
                              <mresp:issueDate>2014-01-23</mresp:issueDate>
                              <mresp:judgmentEnteredDate>2014-01-23</mresp:judgmentEnteredDate>
                                <mresp:firstPaymentDate>2014-03-03</mresp:firstPaymentDate>
                                <mresp:warrantNumber>0Z000151</mresp:warrantNumber>
                                <mresp:enforcingCourtCode>127</mresp:enforcingCourtCode>
                                <mresp:enforcingCourtName>BIRMINGHAM</mresp:enforcingCourtName>
                                <mresp:fee>10000</mresp:fee>
                                <mresp:judgmentWarrantStatus>Judgment accepted by CCBC. Warrant accepted by CCBC</mresp:judgmentWarrantStatus>
                            </ind:mcolResponseDetail>
                        ]]>
                        """),
            List.of("USER_REQUEST_ID_B8", "mcolClaimStatusUpdate", "Rejected", "67", "This claim status update is invalid on the referenced claim.", ""),
            List.of("USER_REQUEST_ID_B9", "mcolWarrant", "Rejected", "28", "Defendant 2 is specified but there is only 1 defendant on the claim.", ""),
            List.of("USER_REQUEST_ID_B10", "mcolJudgmentWarrant", "Rejected", "24", "This judgment request is invalid on the referenced claim.",
                    """
                        <![CDATA[
                            <ind:mcolResponseDetail xmlns:ind="http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema" xmlns:mresp="http://ws.sdt.moj.gov.uk/2013/mcol/ResponseDetailSchema">
                                <mresp:judgmentWarrantStatus>Judgment Request error</mresp:judgmentWarrantStatus>
                            </ind:mcolResponseDetail>
                        ]]>
                        """),
            List.of("USER_REQUEST_ID_B11", "mcolClaim", "Accepted", "", "",
                    """
                        <![CDATA[
                          <ind:mcolResponseDetail xmlns:ind="http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema" xmlns:mresp="http://ws.sdt.moj.gov.uk/2013/mcol/ResponseDetailSchema">
                            <mresp:claimNumber>A0ZZ0045</mresp:claimNumber>
                            <mresp:issueDate>2014-01-23</mresp:issueDate>
                            <mresp:serviceDate>2014-01-28</mresp:serviceDate>
                            <mresp:fee>21000</mresp:fee>
                          </ind:mcolResponseDetail>
                        ]]>
                        """),
            List.of("USER_REQUEST_ID_B12", "mcolWarrant", "Accepted", "", "",
                    """
                        <![CDATA[
                            <ind:mcolResponseDetail xmlns:ind="http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema" xmlns:mresp="http://ws.sdt.moj.gov.uk/2013/mcol/ResponseDetailSchema">
                                <mresp:issueDate>2014-01-23</mresp:issueDate>
                                <mresp:warrantNumber>0Z000152</mresp:warrantNumber>
                                <mresp:enforcingCourtCode>127</mresp:enforcingCourtCode>
                                <mresp:enforcingCourtName>BIRMINGHAM</mresp:enforcingCourtName>
                                <mresp:fee>10000</mresp:fee>
                                <mresp:judgmentWarrantStatus>Warrant accepted by CCBC</mresp:judgmentWarrantStatus>
                            </ind:mcolResponseDetail>
                        ]]>
                        """),
            List.of("USER_REQUEST_ID_B13", "mcolJudgment", "Accepted", "", "",
                    """
                        <![CDATA[
                            <ind:mcolResponseDetail xmlns:ind="http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema" xmlns:mresp="http://ws.sdt.moj.gov.uk/2013/mcol/ResponseDetailSchema">
                                <mresp:judgmentEnteredDate>2014-01-23</mresp:judgmentEnteredDate>
                                <mresp:firstPaymentDate>2014-02-21</mresp:firstPaymentDate>
                                <mresp:judgmentWarrantStatus>Judgment accepted by CCBC</mresp:judgmentWarrantStatus>
                            </ind:mcolResponseDetail>
                        ]]>
                        """),
            List.of("USER_REQUEST_ID_B14", "mcolClaim", "Accepted", "", "",
                    """
                        <![CDATA[
                            <ind:mcolResponseDetail xmlns:ind="http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema" xmlns:mresp="http://ws.sdt.moj.gov.uk/2013/mcol/ResponseDetailSchema">
                                <mresp:claimNumber>A0ZZ0046</mresp:claimNumber>
                                <mresp:issueDate>2014-01-23</mresp:issueDate>
                                <mresp:serviceDate>2014-01-28</mresp:serviceDate>
                                <mresp:fee>34000</mresp:fee>
                            </ind:mcolResponseDetail>
                        ]]>
                        """),
            List.of("USER_REQUEST_ID_B15", "mcolJudgment", "Rejected", "CUST_XML_ERR", "Individual Request format could not be processed by the Target Application. Please check the data and resubmit the request, or  contact 'SDT Contact Details' for assistance.", ""),
            List.of("USER_REQUEST_ID_B16", "mcolSetAside", "Accepted", "", "",
                    """
                        <![CDATA[
                            <ind:mcolResponseDetail xmlns:ind="http://ws.sdt.moj.gov.uk/2013/sdt/BulkFeedbackResponseSchema" xmlns:mresp="http://ws.sdt.moj.gov.uk/2013/mcol/ResponseDetailSchema">
                                <mresp:issueDate>2014-01-23</mresp:issueDate>
                                <mresp:fee>5000</mresp:fee>
                            </ind:mcolResponseDetail>
                        ]]>
                        """),
            List.of("USER_REQUEST_ID_B17", "mcolBreathingSpace", "Accepted", "", "", "")
        );
        methodInvokingFactoryBean.setArguments(arguments);
        return methodInvokingFactoryBean;
    }
}
