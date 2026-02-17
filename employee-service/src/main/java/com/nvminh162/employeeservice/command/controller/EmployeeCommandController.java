package com.nvminh162.employeeservice.command.controller;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nvminh162.employeeservice.command.command.CreateEmployeeCommand;
import com.nvminh162.employeeservice.command.model.CreateEmployeeModel;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/employees")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EmployeeCommandController {

    CommandGateway commandGateway;

    @PostMapping
    public String addEmployee(@RequestBody CreateEmployeeModel model) {
        CreateEmployeeCommand command = CreateEmployeeCommand.builder()
            .id(UUID.randomUUID().toString())
            .firstName(model.getFirstName())
            .lastName(model.getLastName())
            .kin(model.getKin())
            .isDisciplined(false)
            .build();
        return commandGateway.sendAndWait(command);
    }

}
