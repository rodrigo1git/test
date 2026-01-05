package com.prototype.socialNetwork.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Entity
@Table(name="profile")
@Data
@AllArgsConstructor

@NoArgsConstructor

public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="second_name")
    private String secondName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    @Column(name="email", nullable = false)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="public_name", nullable = false)
    private String publicName;

    @JdbcTypeCode(SqlTypes.VECTOR)
    @Column(name = "user_embedding") // Opcional si el nombre coincide
    private float[] userEmbedding;

// No olvides generar los Getters y Setters

}
