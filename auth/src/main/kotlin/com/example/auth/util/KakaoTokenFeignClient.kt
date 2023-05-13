package com.example.auth.util

import com.example.auth.config.ExternalFeignConfig
import com.example.auth.model.dto.GetKakaoTokenRequest
import com.example.auth.model.dto.GetKakaoTokenResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Component
@FeignClient(
    name = "ExternalTokenFeignClient",
    url = "\${oauth2.kakao.baseUrl}",
    configuration = [ExternalFeignConfig::class]
)
interface KakaoTokenFeignClient {
    @PostMapping("/oauth/token")
    fun getToken(@RequestBody tokenRequest: String): GetKakaoTokenResponse
}
