package com.nvminh162.commonservice.model;

import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ErrorMessage {
    String code;
    String message;
    HttpStatus status;
}
