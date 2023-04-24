package com.epam.esm.epammodule3.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "gift_certificate")
public class GiftCertificate extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "duration")
    private Integer duration;

//    @Column(updatable = false)
//    @CreationTimestamp
//    private LocalDateTime createDate;
//
//    @UpdateTimestamp
//    private LocalDateTime lastUpdateDate;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "certificate_tag",
            joinColumns = @JoinColumn(name = "cert_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;
}
