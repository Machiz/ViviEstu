package com.ViviEstu.config;

import com.google.maps.GeoApiContext;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class GoogleMapsConfig {

    @Value("${google.maps.api.key}")
    private String apiKey;

    private GeoApiContext geoApiContext;

    @Bean
    public GeoApiContext geoApiContext() {
        this.geoApiContext = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();
        return this.geoApiContext;
    }

    @PreDestroy
    public void shutdown() {
        if (this.geoApiContext != null) {
            this.geoApiContext.shutdown();
        }
    }
}

