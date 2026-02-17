package com.nvminh162.employeeservice.query.projection;

import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.nvminh162.employeeservice.command.data.Employee;
import com.nvminh162.employeeservice.command.data.EmployeeRepository;
import com.nvminh162.employeeservice.query.model.EmployeeResponseModel;
import com.nvminh162.employeeservice.query.queries.GetAllEmployeeQuery;
import com.nvminh162.employeeservice.query.queries.GetDetailEmployeeQuery;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EmployeeProjection {

    EmployeeRepository employeeRepository;

    @QueryHandler
    public List<EmployeeResponseModel> handle(GetAllEmployeeQuery query) {
        return employeeRepository.findAll().stream().map(employee -> {
            EmployeeResponseModel model = new EmployeeResponseModel();
            BeanUtils.copyProperties(employee, model);
            return model;
        }).toList();
    }

    @QueryHandler
    public EmployeeResponseModel handle(GetDetailEmployeeQuery query) throws Exception {
        EmployeeResponseModel model = new EmployeeResponseModel();
        Employee employee = employeeRepository.findById(query.getId())
                .orElseThrow(() -> new Exception("Employee ID not found " + query.getId()));
        BeanUtils.copyProperties(employee, model);
        return model;
    }
}
