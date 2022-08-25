package ch.ubique.backend.monitoring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class MonitoringService {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringService.class);
    private final RestTemplate restTemplate;
    private final String heartBeatEndpoint;

    public MonitoringService(RestTemplate restTemplate, String heartBeatEndpoint) {
        this.restTemplate = restTemplate;
        this.heartBeatEndpoint = heartBeatEndpoint;
    }

    public void heartBeat() {
        if (!heartBeatEndpoint.isEmpty()) {
            logger.debug("Post for heart beat endpoint.");
            restTemplate.postForLocation(heartBeatEndpoint, "Monitoring Payload");
        }
    }
}
