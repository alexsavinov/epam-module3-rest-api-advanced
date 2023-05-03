package com.epam.esm.epammodule3.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HighestCostDto extends RepresentationModel<HighestCostDto> {

    private Double highestCost;
}
