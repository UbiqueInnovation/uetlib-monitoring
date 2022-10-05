import ch.ubique.backend.monitoring.service.MonitoringService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class MonitoringServiceTest {

    private final String heartBeatEndpoint = "http://localhost:8080";

    @Mock private RestTemplate restTemplate;

    private MonitoringService monitoringService;

    @Test
    void testMockWithOKResponse() {
        monitoringService = new MonitoringService(restTemplate, null);
        Mockito.when(restTemplate.getForEntity(heartBeatEndpoint, String.class))
                .thenReturn(ResponseEntity.ok("OK"));

        // heartBeatEndpoint is null
        monitoringService.heartBeat();
        Mockito.verify(restTemplate, Mockito.times(0))
                .getForEntity(heartBeatEndpoint, String.class);

        // set heart beat endpoint to intended endpoint
        monitoringService = new MonitoringService(restTemplate, heartBeatEndpoint);
        monitoringService.heartBeat();
        Mockito.verify(restTemplate, Mockito.times(1))
                .getForEntity(heartBeatEndpoint, String.class);
    }

    @Test
    void testMockWithNotFoundResponse() {
        Mockito.when(restTemplate.getForEntity(heartBeatEndpoint, String.class))
                .thenReturn(ResponseEntity.notFound().build());

        // set heart beat endpoint to intended endpoint
        monitoringService = new MonitoringService(restTemplate, heartBeatEndpoint);
        monitoringService.heartBeat();
        Mockito.verify(restTemplate, Mockito.times(1))
                .getForEntity(heartBeatEndpoint, String.class);
    }
}
