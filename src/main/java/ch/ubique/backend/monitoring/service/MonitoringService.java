package ch.ubique.backend.monitoring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class MonitoringService {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringService.class);
    private final RestTemplate restTemplate;
    private String heartBeatEndpoint;

    public MonitoringService(RestTemplate restTemplate, String heartBeatEndpoint) {
        this.restTemplate = restTemplate;
        this.heartBeatEndpoint = heartBeatEndpoint;
    }

    public void setHeartBeatEndpoint(String heartBeatEndpoint) {
        this.heartBeatEndpoint = heartBeatEndpoint;
    }

    public String heartBeat() {
        if (!heartBeatEndpoint.isEmpty()) {
            logger.debug("Ping heart beat endpoint.");
            ResponseEntity<String> response =
                    restTemplate.getForEntity(heartBeatEndpoint, String.class);
            return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
        }
        return null;
    }
}
