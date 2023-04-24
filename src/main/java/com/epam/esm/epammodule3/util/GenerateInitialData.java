package com.epam.esm.epammodule3.util;

import com.epam.esm.epammodule3.model.entity.GiftCertificate;
import com.epam.esm.epammodule3.model.entity.Tag;
import com.epam.esm.epammodule3.repository.GiftCertificateRepository;
import com.epam.esm.epammodule3.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.kohsuke.randname.RandomNameGenerator;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Configuration
public class GenerateInitialData {

    private final TagRepository tagRepository;
    private final GiftCertificateRepository certificateRepository;

    public void generate(int numberOfRecords) {
        RandomNameGenerator rnd = new RandomNameGenerator();

        Tag previousTag = null;

        for (int i = 0; i < numberOfRecords; i++) {
            Tag tag = Tag.builder()
                    .name(capitalizeWord(rnd.next()))
                    .build();
            tagRepository.save(tag);

            double price = getRandomInt(1, 100) * 1.55;

            GiftCertificate cert = GiftCertificate.builder()
                    .name(capitalizeWord(rnd.next()))
                    .description(capitalizeWord(rnd.next()))
                    .duration(getRandomInt(1, 10))
                    .price((double) Math.round(price * 100) / 100)
                    .build();

            List<Tag> tags = new ArrayList<>();

            tags.add(tag);
            if (i > 1) {
                tags.add(previousTag);
            }

            cert.setTags(tags);

            previousTag = tag;

            certificateRepository.save(cert);
        }
    }

    private String capitalizeWord(String word) {
        return StringUtils.capitalize(word.replace("_", " "));
    }

    private int getRandomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
}
