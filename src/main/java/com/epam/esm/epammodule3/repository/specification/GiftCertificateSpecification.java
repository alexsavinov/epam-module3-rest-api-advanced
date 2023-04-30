package com.epam.esm.epammodule3.repository.specification;

import com.epam.esm.epammodule3.model.dto.SearchGiftCertificateRequest;
import com.epam.esm.epammodule3.model.entity.GiftCertificate;
import com.epam.esm.epammodule3.model.entity.Tag;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.epammodule3.model.entity.GiftCertificate.DESCRIPTION;
import static com.epam.esm.epammodule3.model.entity.GiftCertificate.NAME;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GiftCertificateSpecification implements Specification<GiftCertificate> {

    private SearchGiftCertificateRequest searchRequest;

    @Override
    public Predicate toPredicate(Root<GiftCertificate> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        Optional.ofNullable(searchRequest.getName()).ifPresent(name ->
                predicates.add(builder.like(root.get(NAME), getStringLike(name)))
        );

        Optional.ofNullable(searchRequest.getDescription()).ifPresent(name ->
                predicates.add(builder.like(root.get(DESCRIPTION), getStringLike(name)))
        );

        Optional.ofNullable(searchRequest.getTags()).ifPresent(tags -> {
            Join<GiftCertificate, Tag> certificateTags = root.join("tags");
            Expression<String> expression = certificateTags.get("name");
            predicates.add(expression.in(tags));
        });

        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
    }

    private String getStringLike(String name) {
        return "%%%s%%".formatted(name);
    }
}
