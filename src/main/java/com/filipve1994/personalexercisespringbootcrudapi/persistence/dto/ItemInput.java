package com.filipve1994.personalexercisespringbootcrudapi.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ItemInput {

    @NotNull(message = "name should not be empty")
    @Size(min = 1, max = 60, message = "name should be between 1 and 60 characters")
    private String name;

    @NotNull(message = "category should not be empty")
    @Size(min = 1, max = 60, message = "category should be between 1 and 60 characters")
    private String category;


}
