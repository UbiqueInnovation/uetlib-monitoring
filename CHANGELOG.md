# CHANGELOG

## 2.0.0

### Improvements

- Support for Spring Boot 4.0
- Use `RestClient` instead of `RestTemplate` in `MonitoringService`

## 1.2.3

### Bugfixes

- More mobile friendly style for HTML in /check endpoint

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

