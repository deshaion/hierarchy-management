package com.personio.hierarchymanager.util;

import com.personio.hierarchymanager.exception.LoopInRequestException;
import com.personio.hierarchymanager.exception.MultiplyRootException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EmployeeHierarchyBuilder {
    private Map<String, String> rawHierarchy;

    public EmployeeHierarchyBuilder(Map<String, String> rawHierarchy) {
        this.rawHierarchy = rawHierarchy;
    }

    public Map<String, Object> build() {
        Map<String, Set<String>> employees = new HashMap<>();
        Set<String> rootList = new HashSet<>(rawHierarchy.values());

        rawHierarchy.forEach((employee, superviser) -> {
            employees.computeIfAbsent(superviser, k -> new HashSet<>()).add(employee);
            rootList.remove(employee);
        });

        if (rootList.size() == 0) {
            //TODO generate loop
            throw new LoopInRequestException("No root was found. The loop in requested data can be the reason.");
        }
        if (rootList.size() > 1) {
            throw new MultiplyRootException("There are more than 1 root in request. Roots are: " + rootList);
        }
        employees.put("ROOT", rootList);

        return generateHierarchy("ROOT", employees);
    }

    private Map<String, Object> generateHierarchy(String superviser, Map<String, Set<String>> employees) {
        Map<String, Object> answer = new HashMap<>();
        Set<String> emp = employees.get(superviser);
        if (emp != null) {
            emp.forEach(employee -> answer.put(employee, generateHierarchy(employee, employees)));
        }

        return answer;
    }
}
