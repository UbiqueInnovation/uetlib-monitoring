package ch.ubique.backend.monitoring.controller;

import ch.ubique.backend.monitoring.models.InfoResponse;
import ch.ubique.backend.monitoring.service.InfoService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/info")
@CrossOrigin({
    "https://status.systems.ubique.ch",
    "https://status-dev.systems.ubique.ch",
    "http://localhost:3000"
})
public class InfoController {

    private final InfoService infoService;

    public InfoController(final InfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public InfoResponse info() {
        return infoService.getInfo();
    }
}
