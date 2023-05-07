package com.example.auth.service

import com.example.auth.model.entity.Authority
import com.example.auth.model.entity.Member
import com.example.auth.persistence.MemberRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Component("userDetailsService")
class CustomUserDetailsService(
    private val memberRepository: MemberRepository
    ) : UserDetailsService {
    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        return memberRepository.findOneWithAuthoritiesById(username.toLong())
            .map { member: Member -> createUser(username, member) }
            .orElseThrow { UsernameNotFoundException("not found $username") }
    }

    private fun createUser(username: String, member: Member): User {
        val grantedAuthorities = member.authorities.stream()
            .map { authority: Authority -> SimpleGrantedAuthority(authority.authorityName) }
            .collect(Collectors.toList())

        return User(
            member.id.toString(),
            BCryptPasswordEncoder().encode(member.email),
            grantedAuthorities
        )
    }
}
