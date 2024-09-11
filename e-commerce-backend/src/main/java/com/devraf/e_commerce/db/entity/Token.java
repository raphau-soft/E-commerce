package com.devraf.e_commerce.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "token")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {
    @Id
    @SequenceGenerator(name = "token_seq", sequenceName = "token_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String token;

    @Column(name = "token_type", nullable = false)
    private String tokenType;

    @Column(nullable = false)
    private Boolean active;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "expired_at", nullable = false)
    private Date expiredAt;

    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
