package com.watashi.bookstore.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Component
public class ProductIdDTO extends EntityDTO{

    @NotNull
    private Long id;

}
