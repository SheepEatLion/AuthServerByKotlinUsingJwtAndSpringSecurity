package com.example.auth.util

import com.example.auth.config.ExternalFeignConfig
import com.example.auth.model.dto.GetKakaoUserResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@Component
@FeignClient(
    name = "ExternalUserFeignClient",
    url = "\${oauth2.kakao.infoUrl}",
    configuration = [ExternalFeignConfig::class]
)
interface KakaoUserFeignClient {
    @GetMapping("/v2/user/me")
    fun getUser(@RequestHeader(name = "Authorization") authorization: String): GetKakaoUserResponse
}
