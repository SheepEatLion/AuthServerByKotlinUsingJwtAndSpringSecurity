package com.example.auth.config

import feign.Logger
import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.context.annotation.Bean

class ExternalFeignConfig {
    @Bean
    fun requestInterceptor(): RequestInterceptor? {
        return RequestInterceptor { template: RequestTemplate ->
            template.header(
                "Content-Type",
                "application/x-www-form-urlencoded"
            )
        }
    }

    @Bean
    fun feignLoggerLevel(): Logger.Level? {
        return Logger.Level.FULL
    }
}