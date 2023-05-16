package com.alsudais.configs;

import com.alsudais.commons.AlSudaisCommonConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.Collections;

@Configuration
public class AlSudaisCorsConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(AlSudaisCommonConstants.INSTANCE.FORWARD_SLASH_DOUBLE_ASTERISK_STRING)
                .allowCredentials(Boolean.TRUE)
                .allowedOriginPatterns(AlSudaisCommonConstants.INSTANCE.ASTERISK_STRING)
                .allowedHeaders(AlSudaisCommonConstants.INSTANCE.ASTERISK_STRING)
                .allowedMethods(AlSudaisCommonConstants.INSTANCE.ASTERISK_STRING)
                .exposedHeaders(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                        HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                        HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                        HttpHeaders.ACCESS_CONTROL_MAX_AGE,
                        HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS,
                        HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD);
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(Boolean.TRUE);
        corsConfiguration.setAllowedHeaders(Collections.singletonList(AlSudaisCommonConstants.INSTANCE.ASTERISK_STRING));
        corsConfiguration.setAllowedMethods(Collections.singletonList(AlSudaisCommonConstants.INSTANCE.ASTERISK_STRING));
        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList(AlSudaisCommonConstants.INSTANCE.ASTERISK_STRING));

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration(AlSudaisCommonConstants.INSTANCE.FORWARD_SLASH_DOUBLE_ASTERISK_STRING, corsConfiguration);
        return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }
}
