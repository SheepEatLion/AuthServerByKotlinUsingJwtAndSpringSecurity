package com.example.auth.persistence

import com.example.auth.model.entity.Authority
import org.springframework.data.jpa.repository.JpaRepository

interface AuthorityRepository : JpaRepository<Authority, String> {
}
