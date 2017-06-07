package com.basaki.example.book.config;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.codahale.metrics.servlets.AdminServlet;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.MetricsServlet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import java.lang.management.ManagementFactory;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by indra.basak on 6/3/17.
 */
@Configuration
@EnableMetrics
@Slf4j
public class MetricsConfiguration extends MetricsConfigurerAdapter {

    @Value("${metrics.report.http.url:/metrics/*}")
    private String urlMappings;

    private final MetricRegistry registry;

    @Autowired
    public MetricsConfiguration(MetricRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        JmxReporter reporter = JmxReporter.forRegistry(metricRegistry)
                .build();
        registerReporter(reporter);
        reporter.start();
    }

    @PostConstruct
    public void registerMetrics() {
        registerMetric(registry, new GarbageCollectorMetricSet(), "jvm", "gc");
        registerMetric(registry, new MemoryUsageGaugeSet(), "jvm", "memory");
        registerMetric(registry, new ThreadStatesGaugeSet(), "jvm",
                "thread-states");
        registerMetric(registry, new FileDescriptorRatioGauge(), "jvm", "fd",
                "usage");
        registerMetric(registry, new BufferPoolMetricSet(
                        ManagementFactory.getPlatformMBeanServer()), "jvm", "buffers",
                "usage");

        configureReporters(registry);
    }

    /**
     * Needed during test to avoid duplicate metric registration. Only happens
     * during testing.
     *
     * @param registry metric registry where all metrics are stored
     * @param name     first element of metric name
     * @param names    remaining elements of metric name
     * @param metric   a metric or set of metric to be registered
     */
    private void registerMetric(MetricRegistry registry, Metric metric,
            String name, String... names) {
        try {
            registry.register(MetricRegistry.name(name, names), metric);
        } catch (Exception e) {
            //don't do anything - only happens during test
        }
    }

    @Bean
    public ServletContextListener registerHealthCheckRegistry(
            MetricRegistry registry,
            HealthCheckRegistry healthCheckRegistry) {
        return new ServletContextListener() {
            @Override
            public void contextInitialized(
                    ServletContextEvent servletContextEvent) {
                servletContextEvent.getServletContext().setAttribute(
                        HealthCheckServlet.HEALTH_CHECK_REGISTRY,
                        healthCheckRegistry);
                servletContextEvent.getServletContext().setAttribute(
                        MetricsServlet.METRICS_REGISTRY, registry);
            }

            @Override
            public void contextDestroyed(
                    ServletContextEvent servletContextEvent) {
                // Do nothing
            }
        };
    }

    /**
     * Registers the admin servlet to make all the metrics available by HTTP at
     * the url http://&lt;host&gt;:&lt;port&gt;/metrics
     *
     * @return the admin servlet responsible for making the Dropwizard metrics
     * available
     */
    @Bean(name = "metricsServlet")
    public ServletRegistrationBean registerHttpReporter() {
        return new ServletRegistrationBean(new AdminServlet(), urlMappings) {
            @Override
            public String getServletName() {
                log.debug("Registering metrics admin servlet...");
                return "MetricsAdmin";
            }
        };
    }
}
