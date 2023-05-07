package com.example.auth.persistence

import com.example.auth.model.entity.Member
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberRepository : JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = ["authorities"])
    fun findOneWithAuthoritiesById(id: Long): Optional<Member>
}
