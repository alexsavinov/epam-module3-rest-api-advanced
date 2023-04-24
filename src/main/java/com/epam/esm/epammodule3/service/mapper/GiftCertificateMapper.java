package com.epam.esm.epammodule3.service.mapper;

import com.epam.esm.epammodule3.model.dto.CreateGiftCertificateRequest;
import com.epam.esm.epammodule3.model.dto.GiftCertificateDto;
import com.epam.esm.epammodule3.model.dto.GiftCertificateWithTagsDto;
import com.epam.esm.epammodule3.model.dto.TagDto;
import com.epam.esm.epammodule3.model.entity.GiftCertificate;
import com.epam.esm.epammodule3.model.entity.Tag;
import com.epam.esm.epammodule3.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GiftCertificateMapper {


    public GiftCertificate toGiftCertificate(CreateGiftCertificateRequest createRequest) {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name(createRequest.getName())
                .description(createRequest.getDescription())
                .duration(createRequest.getDuration())
                .price(createRequest.getPrice())
                .build();

        if (createRequest.getTags() != null) {
            TagMapper tagMapper = new TagMapper();
            giftCertificate.setTags(createRequest.getTags().stream().map(tagMapper::toTag).toList());
        }

        return giftCertificate;
    }

    public GiftCertificateDto toDto(GiftCertificate certificate) {
        GiftCertificateDto certificateDto = GiftCertificateDto.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .description(certificate.getDescription())
                .duration(certificate.getDuration())
                .price(certificate.getPrice())
                .createDate(DateUtil.toIso8601Format(certificate.getCreateDate()))
                .lastUpdateDate(DateUtil.toIso8601Format(certificate.getLastUpdateDate()))
                .build();

        if (certificate.getTags() != null) {
            TagMapper tagMapper = new TagMapper();
            List<TagDto> tagsDto = certificate.getTags().stream().map(tagMapper::toDto).toList();
            certificateDto.setTags(tagsDto);
        }

        return certificateDto;
    }
}
