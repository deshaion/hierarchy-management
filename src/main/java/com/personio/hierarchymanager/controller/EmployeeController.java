package com.personio.hierarchymanager.controller;

import com.personio.hierarchymanager.dao.RelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final RelationshipRepository relationshipRepository;

    @Autowired
    public EmployeeController(RelationshipRepository relationshipRepository) {
        this.relationshipRepository = relationshipRepository;
    }

    @GetMapping(path = "/{employee}", produces = APPLICATION_JSON_VALUE)
    public Map<String, String> get(@PathVariable String employee) {
        return relationshipRepository.findEmployee(employee);
    }
}
