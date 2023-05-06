package com.example.auth.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table
class Authority(
    @Id
    @Column(name = "authority_name")
    val authorityName: String,
)
