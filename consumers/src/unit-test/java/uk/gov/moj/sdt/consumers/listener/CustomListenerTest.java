package uk.gov.moj.sdt.consumers.listener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CustomListenerTest {

    CustomListener customListener;

    @Mock
    ServletContextEvent mockServletContextEvent;

    @Mock
    Logger mockLogger;

    @Test
    void testContextMethods() {
        try (MockedStatic<LoggerFactory> mockLoggerFactory = Mockito.mockStatic(LoggerFactory.class)) {
            mockLoggerFactory.when(() -> LoggerFactory.getLogger(any(Class.class)))
                    .thenReturn(mockLogger);

            customListener = new CustomListener();
            customListener.contextInitialized(mockServletContextEvent);
            customListener.contextDestroyed(mockServletContextEvent);

            Mockito.verify(mockLogger).info("CustomListener is initialized");
            Mockito.verify(mockLogger).info("CustomListener is destroyed");
        }

    }
}
