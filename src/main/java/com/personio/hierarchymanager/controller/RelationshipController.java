package com.personio.hierarchymanager.controller;

import com.personio.hierarchymanager.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/relationships")
public class RelationshipController {

    private final RelationshipService relationshipService;

    @Autowired
    public RelationshipController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }


    //return json - loops and multiply roots
    //keep in database - and get the superviser and super-superviser's name

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Map<String, Object> handleRawRelationsip(@RequestBody Map<String, String> rawHierarchy) {
        return relationshipService.saveAndGenerateEmployeeHierarchy(rawHierarchy);
    }
}
