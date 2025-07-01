package com.ecommercebackend.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "date_created",insertable = false,updatable = false)
    private LocalDate date_created;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.MERGE)
    private Set<CartItem> cartItems=new LinkedHashSet<>();

}
