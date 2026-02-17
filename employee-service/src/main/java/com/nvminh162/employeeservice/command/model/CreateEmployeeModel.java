package com.nvminh162.employeeservice.command.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateEmployeeModel {
    String firstName;
    String lastName;
    String kin;
    Boolean isDisciplined;
}
