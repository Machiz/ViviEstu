package com.ViviEstu.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dl3r73p4j",
                "api_key", "927197454337423",
                "api_secret", "UshGmTe1JJnoKU18Z4X8wtF_ir8"
        ));
    }
}
