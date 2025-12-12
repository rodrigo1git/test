package com.prototype.socialNetwork.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "is_friend_to")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsFriendTo {

    // 1. Instanciamos la clave compuesta
    @EmbeddedId
    private IsFriendToId id;

    // 2. Columna extra de la relación
    @Column(name = "since", nullable = false)
    private LocalDate since;

    // --- RELACIONES ---

    // Relación para 'id1'. @MapsId vincula el atributo 'id1' de IsFriendToId con este objeto Profile
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("id1")
    @JoinColumn(name = "id1")
    private Profile profile1;

    // Relación para 'id2'. @MapsId vincula el atributo 'id2' de IsFriendToId con este objeto Profile
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("id2")
    @JoinColumn(name = "id2")
    private Profile profile2;

    // Constructor auxiliar para facilitar la creación
    public IsFriendTo(Profile p1, Profile p2, LocalDate since) {
        this.profile1 = p1;
        this.profile2 = p2;
        this.since = since;
        // Es importante inicializar el ID con los valores de los perfiles
        this.id = new IsFriendToId(p1.getId(), p2.getId());
    }
    public IsFriendTo(Profile p1, Profile p2) {
        this.profile1 = p1;
        this.profile2 = p2;
        this.since = LocalDate.now();
        // Es importante inicializar el ID con los valores de los perfiles
        this.id = new IsFriendToId(p1.getId(), p2.getId());
    }
}