package com.example.auth.model.entity

import jakarta.persistence.*

@Entity
@Table
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(length = 50)
    val name: String,

    @Column(length = 50)
    val email: String? = null,

    @ManyToMany
    @JoinTable(
        name = "member_authority",
        joinColumns = [JoinColumn(name = "member_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "authority_name", referencedColumnName = "authority_name")]
    )
    val authorities: Set<Authority>,
)
