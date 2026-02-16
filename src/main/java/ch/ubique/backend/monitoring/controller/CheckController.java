package ch.ubique.backend.monitoring.controller;

import ch.ubique.backend.core.validator.NullOrNotBlank;
import ch.ubique.backend.monitoring.check.StatusCheckService;
import ch.ubique.backend.monitoring.check.converter.CheckResultsToHTMLConverter;
import ch.ubique.backend.monitoring.check.models.CheckLevel;
import ch.ubique.backend.monitoring.check.models.CheckResults;
import ch.ubique.backend.spring.validation.ValidateResponse;

import jakarta.validation.constraints.NotNull;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@ValidateResponse
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
    @NotNull private final CheckResultsToHTMLConverter checkResultsToHTMLConverter;

    public CheckController(
            @NotNull final StatusCheckService statusCheckService,
            @NotNull final CheckResultsToHTMLConverter checkResultsToHTMLConverter) {
        this.statusCheckService = statusCheckService;
        this.checkResultsToHTMLConverter = checkResultsToHTMLConverter;
    }

    @NotNull
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CheckResults check(
            @NotNull @RequestParam(defaultValue = "ERROR") final CheckLevel level,
            @NullOrNotBlank @RequestParam("check") final String checkClassName) {
        return statusCheckService.checkAll(level, checkClassName);
    }

    @NotNull
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String checkHtml(
            @NotNull @RequestParam(defaultValue = "ERROR") final CheckLevel level,
            @NullOrNotBlank @RequestParam("check") final String checkClassName) {
        final var checkResults = statusCheckService.checkAll(level, checkClassName);
        return checkResultsToHTMLConverter.convert(checkResults);
    }
}
