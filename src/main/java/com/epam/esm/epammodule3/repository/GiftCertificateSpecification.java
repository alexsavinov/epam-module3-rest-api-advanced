package com.epam.esm.epammodule3.repository;

import com.epam.esm.epammodule3.model.dto.SearchRequest;
import com.epam.esm.epammodule3.model.entity.GiftCertificate;
import com.epam.esm.epammodule3.model.entity.Tag;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@ToString
public class GiftCertificateSpecification implements Specification<GiftCertificate> {

    private SearchRequest searchRequest;

    @Override
    public Predicate toPredicate(Root<GiftCertificate> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        List<Predicate> predicates = new ArrayList<>();

        if (searchRequest.getName() != null) {
            Predicate predicate = builder.like(root.get("name"), "%" + searchRequest.getName() + "%");
            predicates.add(predicate);
        }
        if (searchRequest.getDescription() != null) {
            Predicate predicate = builder.like(root.get("description"), "%" + searchRequest.getDescription() + "%");
            predicates.add(predicate);
        }
        if (searchRequest.getTags() != null) {
            Join<GiftCertificate, Tag> certificateTags = root.join("tags");
            Expression<String> expression = certificateTags.get("name");
            Predicate predicate = expression.in(searchRequest.getTags());
            predicates.add(predicate);

//            searchRequest.getTags().stream()
//                    .forEach(tagName -> predicates.add(
//                            builder.equal(certificateTags.get("name"), tagName))
//                    );
        }

        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
