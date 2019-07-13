package com.personio.hierarchymanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personio.hierarchymanager.dao.RelationshipRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Before
    public void initMockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
        relationshipRepository.clearRelationships();
    }

    @Test
    public void testBasic() throws Exception {
        Map<String, String> rawRelationships = new HashMap<>();
        rawRelationships.put("Pete", "Nick");
        rawRelationships.put("Barbara", "Nick");
        rawRelationships.put("Nick", "Sophie");
        rawRelationships.put("Sophie", "Jonas");

        String requestJson = objectMapper.writeValueAsString(rawRelationships);

        mockMvc.perform(post("/relationships")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/employees/Barbara")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.supervisor", is("Nick")))
                .andExpect(jsonPath("$.supervisor2Level", is("Sophie")));
    }

    @Test
    public void testFirstLevel() throws Exception {
        Map<String, String> rawRelationships = new HashMap<>();
        rawRelationships.put("Pete", "Nick");
        rawRelationships.put("Barbara", "Nick");
        rawRelationships.put("Nick", "Sophie");
        rawRelationships.put("Sophie", "Jonas");

        String requestJson = objectMapper.writeValueAsString(rawRelationships);

        mockMvc.perform(post("/relationships")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/employees/Sophie")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.supervisor", is("Jonas")))
                .andExpect(jsonPath("$.supervisor2Level").doesNotExist());
    }

    @Test
    public void testRoot() throws Exception {
        Map<String, String> rawRelationships = new HashMap<>();
        rawRelationships.put("Pete", "Nick");
        rawRelationships.put("Barbara", "Nick");
        rawRelationships.put("Nick", "Sophie");
        rawRelationships.put("Sophie", "Jonas");

        String requestJson = objectMapper.writeValueAsString(rawRelationships);

        mockMvc.perform(post("/relationships")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/employees/Jonas")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.supervisor").doesNotExist())
                .andExpect(jsonPath("$.supervisor2Level").doesNotExist());
    }
}