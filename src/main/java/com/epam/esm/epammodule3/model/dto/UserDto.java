package com.epam.esm.epammodule3.model.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends RepresentationModel<UserDto> {

    private Long id;
    private String name;
}
