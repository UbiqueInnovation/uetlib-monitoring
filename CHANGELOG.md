# CHANGELOG

## 3.0.1

- Rename uetlib configuration keys and property references to `ch.uetlib.*`, including Spring bindings, `@Value`,
  conditions, scheduling expressions and configuration files. Migrate external deployment configuration as well.

- Move Java packages and class references from `ch.ubique.uetlib` to `ch.uetlib`.

### Breaking changes

- Move Java packages from `ch.ubique.backend` to `ch.uetlib`, including class-name resources. The combined OpenRewrite
  migration also renames known Spring configuration keys to `ch.uetlib.*`.
- Remove the inherited Ubique Artifactory repository and deployment destination; private consumers must configure their
  own registry.
- Add Apache-2.0 licensing for original code, preserve third-party licenses, and add public project metadata and
  contribution/security guidance.

- Move Maven coordinates from `ch.ubique:uetlib-*` to `ch.uetlib:uetlib-*`.
- Align uetlib parent, BOM and library references with the migration releases.

### Migration

After the migration releases and `uetlib-rewrite` are published, run this from the consuming project, then review the
diff and run its tests:

```sh
mvn -U org.openrewrite.maven:rewrite-maven-plugin:6.46.1:run \
  -Drewrite.recipeArtifactCoordinates=ch.uetlib:uetlib-rewrite:RELEASE \
  -Drewrite.activeRecipes=ch.uetlib.MigrateToPublicUetlib
```

The recipe updates Maven coordinates/versions, Java references and known class-name resources. Review application-owned
packages under the old namespace, Kotlin, reflection patterns, native-access module flags and persisted class names
separately. Use `:dryRun` instead of `:run` to preview a patch.

## 2.6.0

- Add optional `ch.ubique.backend.monitoring.cors-origins` to extend the info endpoint’s allowed origins without
  repeating the defaults.

## 2.5.2

- Allow Infobox frontend origins and localhost:3001 on the info endpoint.

## 2.5.1

- Add both `de.westemeyer:artifact-version-service/-spring-boot` unconditionally as it is used `uetlib-monitoring`
- version updates

## 2.5.0

### Improvements

- Collect artifacts that register themselves as a Spring bean in `ch.uetlib.versions` as well as the traditional way
  through `META-INF/services` classes. That package is now part of the component scan in
  `UetlibMonitoringAutoConfiguration`.

## 2.4.1

- latest uetlib parent
- version updates

## 2.4.0

### Improvements

- Throw a better exception on `Version::parse` with info about what version failed person for easier debugging.
- Java 25 with newest `uetlib-parent:2026.8.0`

## 2.3.3

### Bugfixes

- Fix deprecated `@Valid` placement on `InfoResponse.artifacts`.

## 2.3.2

- updated actions to not run tests twice for sonar
- update uetlib version

## 2.3.1

### Bugfixes

- Mark new parameter `checks` on the `/check` endpoint as optional. Any project with `WsPublicEndpointTest` or similar
  with might need to fix the following line in a test:
  ```diff
  - when(statusCheckService.runChecks(any())).thenReturn(new CheckResults(List.of()));
  + when(statusCheckService.runChecks(any(), any())).thenReturn(new CheckResults(List.of()));
  ```

## 2.3.0

### Improvements

- Null-marked it all
- Add new parameter `checks` to `/check` endpoint to run only checks with given class name

## 2.2.0

- Drop `uetlib-spring-boot-validation` in favor of `uetlib-spring-boot@v2.1.0`

## 2.1.0

- Adapt `MonitoringService` to `de:westemeyer:artifact-version-service/maven-plugin:2.0.0`
- Add new autoconfiguration for this package: `ch.ubique.backend.monitoring` can be safely removed from component scan
  in projects.

## 2.0.0

### Improvements

- Support for Spring Boot 4.0
- Use `RestClient` instead of `RestTemplate` in `MonitoringService`

## 1.3.0

### Improvements

- Add response validation to `InfoController` and `CheckController`. Configurable via
  ```properties
  ch.ubique.backend.spring.validation.response.fail.action=throw
  ch.ubique.backend.spring.validation.response.fail.log-level=error
  ```
  Defaults to `log` and `warn`, respectively.

## 1.2.3

### Bugfixes

- More mobile-friendly style for HTML in /check endpoint

## 1.2.2

### Bugfixes

- Fix inverted filter condition for mostSeverFailedCheckLevel

## 1.2.1

### Bugfixes

- fix NPE in `CheckResultsToHTMLConverter` in the case when all checks fail

## 1.2.0

### Improvements

- Add human-readable HTML table response for /check endpoint

## 1.1.2

### Improvements

- Add system info with `jvmUptime` and `jvmStartTime`
- Add kotlin support and expose `kotlinVersion` like `javaVersion` in info endpoint
- Added `whoami` Artifact to info endpoint, to see project artifact.
- Graceful handling of uncaught StatusCheck exceptions for check endpoint

## 1.1.1

- Internal cleanup, removed PostConstruct

## 1.1.0

### Improvements

- Latest uetlib dependencies with Spring Boot 3.5 support

## 1.0.0

First release. The code was moved
from [UbiqueInnovation/backend-jvm-lib](https://github.com/UbiqueInnovation/backend-jvm-lib).
