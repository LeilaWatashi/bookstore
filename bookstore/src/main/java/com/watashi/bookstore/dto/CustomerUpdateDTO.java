package com.watashi.bookstore.dto;

import com.watashi.bookstore.entity.user.Gender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@Component
public class CustomerUpdateDTO extends EntityDTO{
    @NotNull
    private Long id;

    @NotEmpty(message = "O nome é obrigatório")
    private String name;

    @NotEmpty(message = "O CPF é obrigatório")
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos")
    private String cpf;

    @NotNull(message = "A data de nascimento é obrigatória")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotNull(message = "O gênero é obrigatório")
    private Gender gender;

    @NotEmpty(message = "O telefone é obrigatório")
    @Size(min = 10, max = 11, message = "O número de telefone deve ter entre 10 e 11 dígitos (incluindo o DDD)")
    private String phone;

    @Valid
    private UserUpdateDTO user;
}
