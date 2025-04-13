package com.babystore.config;


import jakarta.servlet.Filter;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SiteMeshConfig {
    @Bean
    public FilterRegistrationBean<Filter> siteMeshFilter() {
        FilterRegistrationBean<Filter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new ConfigurableSiteMeshFilter() {
            @Override
            protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
                builder.addDecoratorPath("/admin/*", "/admin.jsp")

                        .addDecoratorPath("/user/*", "/index.jsp")
                        .addExcludedPath("/api/*");

            }
        });
        return filter;
    }
}
