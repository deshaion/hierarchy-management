package com.personio.hierarchymanager.service;

import com.personio.hierarchymanager.dao.RelationshipRepository;
import com.personio.hierarchymanager.util.EmployeeHierarchyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RelationshipService {
    private final RelationshipRepository relationshipRepository;

    @Autowired
    public RelationshipService(RelationshipRepository relationshipRepository) {
        this.relationshipRepository = relationshipRepository;
    }



    public Map<String, Object> saveAndGenerateEmployeeHierarchy(Map<String, String> rawHierarchy) {
        Map<String, Object> employeeHierarchy = new EmployeeHierarchyBuilder(rawHierarchy).build();

        // if it was built successfully
        relationshipRepository.saveRelationships(rawHierarchy);

        return employeeHierarchy;
    }
}
