package uk.gov.moj.sdt.idam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class S2SRepositoryTest {

    private static final String S2S_TOKEN = "Test S2S token";

    private S2SRepository s2sRepository;

    @Mock
    AuthTokenGenerator mockAuthTokenGenerator;

    @BeforeEach
    void setUp() {
        s2sRepository = new S2SRepository(mockAuthTokenGenerator);
    }

    @Test
    void testGetS2SToken() {
        when(mockAuthTokenGenerator.generate()).thenReturn(S2S_TOKEN);

        String s2sToken = s2sRepository.getS2SToken();

        assertEquals(S2S_TOKEN, s2sToken, "Unexpected S2S token returned");

        verify(mockAuthTokenGenerator).generate();
    }
}
