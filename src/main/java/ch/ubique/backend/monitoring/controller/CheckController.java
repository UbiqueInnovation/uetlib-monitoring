package ch.ubique.backend.monitoring.controller;

import ch.ubique.backend.monitoring.check.StatusCheckService;
import ch.ubique.backend.monitoring.check.models.CheckResults;

import jakarta.validation.constraints.NotNull;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/check", "${ch.ubique.backend.monitoring.check-endpoint:/check}"})
public class CheckController {

    @NotNull private final StatusCheckService statusCheckService;

    public CheckController(@NotNull final StatusCheckService statusCheckService) {
        this.statusCheckService = statusCheckService;
    }

    @NotNull
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public CheckResults check() {
        return statusCheckService.checkAll();
    }
}
