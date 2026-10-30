package ch.ubique.uetlib.monitoring.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ComponentScan(basePackages = {"ch.ubique.uetlib.monitoring", "ch.ubique.versions"})
public class UetlibMonitoringAutoConfiguration {}
