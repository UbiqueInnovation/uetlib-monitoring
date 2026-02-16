# CHANGELOG

## 2.3.0

### Improvements

- Add new parameter `check` to `/check` endpoint to run only checks with given class name

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

