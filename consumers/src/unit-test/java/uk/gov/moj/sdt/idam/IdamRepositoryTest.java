package uk.gov.moj.sdt.idam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;
import uk.gov.moj.sdt.params.ApplicationParams;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdamRepositoryTest {

    private static final String BEARER_TOKEN = "Test bearer token";

    private static final String SDT_SYSTEM_USER_ID = "Test SDT system user id";
    private static final String SDT_SYSTEM_USER_PASSWORD = "Test SDT system user password";

    private static final String SDT_SYSTEM_USER_ACCESS_TOKEN = "Test SDT system user access token";

    private static final String USER_ID = "Test user id";

    private IdamRepository idamRepository;

    @Mock
    private UserInfo mockUserInfo;

    @Mock
    private IdamClient mockIdamClient;

    @Mock
    private ApplicationParams mockApplicationParams;

    @Mock
    private UserDetails mockUserDetails;

    @BeforeEach
    void setUp() {
        idamRepository = new IdamRepository(mockIdamClient, mockApplicationParams);
    }

    @Test
    void testGetUserInfo() {
        when(mockIdamClient.getUserInfo(BEARER_TOKEN)).thenReturn(mockUserInfo);

        UserInfo userInfo = idamRepository.getUserInfo(BEARER_TOKEN);

        assertNotNull(userInfo, "UserInfo should not be null");
        assertSame(mockUserInfo, userInfo, "Unexpected UserInfo object returned");

        verify(mockIdamClient).getUserInfo(BEARER_TOKEN);
    }

    @Test
    void testGetSdtSystemUserAccessToken() {
        when(mockApplicationParams.getSdtSystemUserId()).thenReturn(SDT_SYSTEM_USER_ID);
        when(mockApplicationParams.getSdtSystemUserPassword()).thenReturn(SDT_SYSTEM_USER_PASSWORD);

        when(mockIdamClient.getAccessToken(SDT_SYSTEM_USER_ID, SDT_SYSTEM_USER_PASSWORD))
            .thenReturn(SDT_SYSTEM_USER_ACCESS_TOKEN);

        String accessToken = idamRepository.getSdtSystemUserAccessToken();

        assertEquals(SDT_SYSTEM_USER_ACCESS_TOKEN, accessToken, "Unexpected system access token returned");

        verify(mockApplicationParams).getSdtSystemUserId();
        verify(mockApplicationParams).getSdtSystemUserPassword();
        verify(mockIdamClient).getAccessToken(SDT_SYSTEM_USER_ID, SDT_SYSTEM_USER_PASSWORD);
    }

    @Test
    void testGetUserByUserId() {
        when(mockIdamClient.getUserByUserId(BEARER_TOKEN, USER_ID)).thenReturn(mockUserDetails);

        UserDetails userDetails = idamRepository.getUserByUserId(USER_ID, BEARER_TOKEN);

        assertNotNull(userDetails, "UserDetails should not be null");
        assertSame(mockUserDetails, userDetails, "Unexpected UserDetails object returned");

        verify(mockIdamClient).getUserByUserId(BEARER_TOKEN, USER_ID);
    }
}
