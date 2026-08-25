package ch.ubique.backend.monitoring.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ComponentScan(basePackages = {"ch.ubique.backend.monitoring", "ch.ubique.versions"})
public class UetlibMonitoringAutoConfiguration {}
