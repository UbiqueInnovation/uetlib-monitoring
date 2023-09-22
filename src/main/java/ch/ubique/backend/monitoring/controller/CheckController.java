package ch.ubique.backend.monitoring.controller;

import ch.ubique.backend.monitoring.check.StatusCheckService;
import ch.ubique.backend.monitoring.check.models.CheckLevel;
import ch.ubique.backend.monitoring.check.models.CheckResults;

import jakarta.validation.constraints.NotNull;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/check", "${ch.ubique.backend.monitoring.check-endpoint:/check}"})
@CrossOrigin(
        value = {
            "https://status.systems.ubique.ch",
            "https://status-dev.systems.ubique.ch",
            "http://localhost:3000"
        },
        allowedHeaders = "Content-Type")
public class CheckController {

    @NotNull private final StatusCheckService statusCheckService;

    public CheckController(@NotNull final StatusCheckService statusCheckService) {
        this.statusCheckService = statusCheckService;
    }

    @NotNull
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CheckResults check(
            @NotNull @RequestParam(defaultValue = "ERROR") final CheckLevel level) {
        return statusCheckService.checkAll(level);
    }
}
