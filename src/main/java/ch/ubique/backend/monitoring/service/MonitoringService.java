package ch.ubique.backend.monitoring.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class MonitoringService {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringService.class);

    private final RestTemplate restTemplate;
    private final String heartBeatEndpoint;

    public MonitoringService(final RestTemplate restTemplate, final String heartBeatEndpoint) {
        this.restTemplate = restTemplate;
        this.heartBeatEndpoint = heartBeatEndpoint;
    }

    public void heartBeat() {
        if (StringUtils.isNotBlank(heartBeatEndpoint)) {
            logger.debug("Ping heart beat endpoint {}.", heartBeatEndpoint);
            ResponseEntity<String> response =
                    restTemplate.getForEntity(heartBeatEndpoint, String.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                logger.warn(
                        "Heart beat endpoint {} returned status {}.",
                        heartBeatEndpoint,
                        response.getStatusCode());
            }
        }
    }
}
