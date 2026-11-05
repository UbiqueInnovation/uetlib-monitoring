package ch.uetlib.monitoring.controller;

import ch.uetlib.core.validator.NullOrNotEmpty;
import ch.uetlib.monitoring.check.StatusCheckService;
import ch.uetlib.monitoring.check.converter.CheckResultsToHTMLConverter;
import ch.uetlib.monitoring.check.models.CheckLevel;
import ch.uetlib.monitoring.check.models.CheckResults;
import ch.uetlib.spring.boot.validation.ValidateResponse;

import jakarta.validation.constraints.NotNull;

import org.jspecify.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@ValidateResponse
@RestController
@RequestMapping({"/check", "${ch.uetlib.monitoring.check-endpoint:/check}"})
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
            @NullOrNotEmpty @Nullable @RequestParam(value = "checks", required = false)
                    final List<String> checkClassNames) {
        return statusCheckService.runChecks(level, checkClassNames);
    }

    @NotNull
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String checkHtml(
            @NotNull @RequestParam(defaultValue = "ERROR") final CheckLevel level,
            @NullOrNotEmpty @Nullable @RequestParam(value = "checks", required = false)
                    final List<String> checkClassNames) {
        final var checkResults = statusCheckService.runChecks(level, checkClassNames);
        return checkResultsToHTMLConverter.convert(checkResults);
    }
}
