package uk.gov.moj.sdt.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import uk.gov.hmcts.reform.authorisation.ServiceAuthorisationApi;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGeneratorFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public class AuthClientConfigurationTest {

    private static final String S2S_AUTH_SECRET = "Test auth secret";
    private static final String S2S_AUTH_MICROSERVICE = "Test auth microservice";

    private AuthClientConfiguration authClientConfiguration;

    @BeforeEach
    void setUp() {
        authClientConfiguration = new AuthClientConfiguration();
    }

    @Test
    void testAuthTokenGenerator() {
        try (
            MockedStatic<AuthTokenGeneratorFactory> mockStaticAuthTokenGeneratorFactory =
                mockStatic(AuthTokenGeneratorFactory.class)
            ) {
            ServiceAuthorisationApi mockServiceAuthorisationApi = mock(ServiceAuthorisationApi.class);
            AuthTokenGenerator mockAuthTokenGenerator = mock(AuthTokenGenerator.class);

            mockStaticAuthTokenGeneratorFactory.when(
                () -> AuthTokenGeneratorFactory.createDefaultGenerator(S2S_AUTH_SECRET,
                                                                       S2S_AUTH_MICROSERVICE,
                                                                       mockServiceAuthorisationApi)
            ).thenReturn(mockAuthTokenGenerator);

            AuthTokenGenerator authTokenGenerator =
                authClientConfiguration.authTokenGenerator(S2S_AUTH_SECRET,
                                                           S2S_AUTH_MICROSERVICE,
                                                           mockServiceAuthorisationApi);

            assertNotNull(authTokenGenerator, "AuthTokenGenerator should not be null");
            assertSame(mockAuthTokenGenerator, authTokenGenerator, "Unexpected AuthTokenGenerator object returned");

            mockStaticAuthTokenGeneratorFactory.verify(
                () -> AuthTokenGeneratorFactory.createDefaultGenerator(S2S_AUTH_SECRET,
                                                                       S2S_AUTH_MICROSERVICE,
                                                                       mockServiceAuthorisationApi)
            );
        }
    }
}
