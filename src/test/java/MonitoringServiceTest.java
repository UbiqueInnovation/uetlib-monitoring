import ch.ubique.backend.monitoring.service.MonitoringService;
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
        Mockito.when(restTemplate.getForEntity(heartBeatEndpoint, String.class))
                .thenReturn(new ResponseEntity("OK", HttpStatus.OK));
        monitoringService.heartBeat();
        Mockito.verify(restTemplate, Mockito.times(1)).getForEntity(heartBeatEndpoint, String.class);
    }
}
