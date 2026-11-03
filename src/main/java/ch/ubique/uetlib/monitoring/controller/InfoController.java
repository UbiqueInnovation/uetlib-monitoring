package ch.ubique.uetlib.monitoring.controller;

import ch.ubique.uetlib.monitoring.model.InfoResponse;
import ch.ubique.uetlib.monitoring.service.InfoService;
import ch.ubique.uetlib.spring.boot.validation.ValidateResponse;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Validated
@ValidateResponse
@RestController
@RequestMapping({"/v1/info", "${ch.ubique.backend.monitoring.info-endpoint:/v1/info}"})
@CrossOrigin(
        origins = {
            "https://status.systems.ubique.ch",
            "https://status-dev.systems.ubique.ch",
            "http://localhost:3000",
            "${ch.ubique.backend.monitoring.cors-origins:}"
        })
public class InfoController {

    private final InfoService infoService;

    public InfoController(final InfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InfoResponse> info() {
        final var stale = Duration.ofMinutes(15);
        return ResponseEntity.ok()
                .cacheControl(
                        CacheControl.maxAge(Duration.ofMinutes(5))
                                .cachePrivate() // Do not cache in CDNs
                                .staleWhileRevalidate(stale)
                                .staleIfError(stale))
                .body(infoService.getInfo());
    }
}
