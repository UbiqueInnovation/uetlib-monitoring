import ch.ubique.backend.monitoring.service.MonitoringService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class MonitoringServiceTest {

    private String heartBeatEndpoint = "http://localhost:8080";

    @Mock private RestTemplate restTemplate;

    @InjectMocks private MonitoringService monitoringService;

    @BeforeEach
    public void setHeartBeatEndpoint() {
        monitoringService.setHeartBeatEndpoint(heartBeatEndpoint);
    }

    @Test
    public void givenMockingIsDoneByMockito_whenGetIsCalled_shouldReturnMockedObject() {
        String str = "OK";
        Mockito.when(restTemplate.getForEntity(heartBeatEndpoint, String.class))
                .thenReturn(new ResponseEntity(str, HttpStatus.OK));

        String expected = monitoringService.heartBeat();
        Assertions.assertEquals(str, expected);
    }
}
