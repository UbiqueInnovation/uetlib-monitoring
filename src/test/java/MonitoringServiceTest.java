import ch.ubique.backend.monitoring.service.MonitoringService;

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

    @Test
    public void testMockWithOKResponse() {
        Mockito.when(restTemplate.getForEntity(heartBeatEndpoint, String.class))
                .thenReturn(new ResponseEntity("OK", HttpStatus.OK));

        // heartBeatEndpoint is null
        monitoringService.heartBeat();
        Mockito.verify(restTemplate, Mockito.times(0))
                .getForEntity(heartBeatEndpoint, String.class);

        // set heart beat endpoint to intended endpoint
        monitoringService.setHeartBeatEndpoint(heartBeatEndpoint);
        monitoringService.heartBeat();
        Mockito.verify(restTemplate, Mockito.times(1))
                .getForEntity(heartBeatEndpoint, String.class);
    }

    @Test
    public void testMockWithNotFoundResponse() {
        Mockito.when(restTemplate.getForEntity(heartBeatEndpoint, String.class))
                .thenReturn(new ResponseEntity("NOT_FOUND", HttpStatus.NOT_FOUND));

        // set heart beat endpoint to intended endpoint
        monitoringService.setHeartBeatEndpoint(heartBeatEndpoint);
        monitoringService.heartBeat();
        Mockito.verify(restTemplate, Mockito.times(1))
                .getForEntity(heartBeatEndpoint, String.class);
    }
}
