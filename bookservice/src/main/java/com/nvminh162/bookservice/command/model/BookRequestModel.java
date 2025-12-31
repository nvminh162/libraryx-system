package com.nvminh162.bookservice.command.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookRequestModel {
    String id;

    @NotBlank(message = "Name is mandatory") // khác null, khoảng trắng
    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
    String name;
    @NotBlank(message = "Author is mandatory")
    String author;

    Boolean isReady;
}
