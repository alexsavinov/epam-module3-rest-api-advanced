package com.epam.esm.epammodule3.repository;

import com.epam.esm.epammodule3.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findFirstByName(String name);

    List<Tag> findTagsByCertificatesId(Long id);
}
