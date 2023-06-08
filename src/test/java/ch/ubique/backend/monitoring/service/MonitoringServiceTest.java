package ch.ubique.backend.monitoring.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class MonitoringServiceTest {

    private final String heartbeatEndpoint = "http://localhost:8080";

    @Mock private RestTemplate restTemplate;

    private MonitoringService monitoringService;

    @Test
    void testMockWithOKResponse() {
        when(restTemplate.getForEntity(nullable(String.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok("OK"));

        // heartbeatEnabled is false
        monitoringService = new MonitoringService(restTemplate, false, null);
        monitoringService.heartbeat();
        Mockito.verify(restTemplate, Mockito.times(0))
                .getForEntity(heartbeatEndpoint, String.class);

        // heartbeatEndpoint is null
        monitoringService = new MonitoringService(restTemplate, true, null);
        monitoringService.heartbeat();
        Mockito.verify(restTemplate, Mockito.times(0))
                .getForEntity(nullable(String.class), eq(String.class));

        // set heartbeat endpoint to intended endpoint
        monitoringService = new MonitoringService(restTemplate, true, heartbeatEndpoint);
        monitoringService.heartbeat();
        Mockito.verify(restTemplate, Mockito.times(1))
                .getForEntity(nullable(String.class), eq(String.class));
    }

    @Test
    void testMockWithNotFoundResponse() {
        when(restTemplate.getForEntity(nullable(String.class), eq(String.class)))
                .thenReturn(ResponseEntity.notFound().build());

        // set heartbeat endpoint to intended endpoint
        monitoringService = new MonitoringService(restTemplate, true, heartbeatEndpoint);
        monitoringService.heartbeat();
        Mockito.verify(restTemplate, Mockito.times(1))
                .getForEntity(heartbeatEndpoint, String.class);
    }
}
