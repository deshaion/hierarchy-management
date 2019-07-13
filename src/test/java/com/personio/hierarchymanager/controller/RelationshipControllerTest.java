package com.personio.hierarchymanager.controller;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RelationshipControllerTest {

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
    public void basicTest() throws Exception {
        Map<String, String> rawRelationships = new HashMap<>();
        rawRelationships.put("Pete", "Nick");
        rawRelationships.put("Barbara", "Nick");
        rawRelationships.put("Nick", "Sophie");
        rawRelationships.put("Sophie", "Jonas");

        checkRelationships(rawRelationships, "{\"Jonas\": {\"Sophie\": {\"Nick\":{\"Pete\":{}, \"Barbara\":{}}}}}");
    }

    @Test
    public void oneRecord() throws Exception {
        Map<String, String> rawRelationships = new HashMap<>();
        rawRelationships.put("Pete", "Nick");

        checkRelationships(rawRelationships, "{\"Nick\": {\"Pete\": {}}}");
    }

    @Test
    public void testLoop() throws Exception {
        Map<String, String> rawRelationships = new HashMap<>();
        rawRelationships.put("Pete", "Nick");
        rawRelationships.put("Nick", "Pete");

        String requestJson = objectMapper.writeValueAsString(rawRelationships);

        mockMvc.perform(post("/relationships")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.message", is("No root was found. The loop in requested data can be the reason.")));
    }

    @Test
    public void testComplicatedLoop() throws Exception {
        Map<String, String> rawRelationships = new HashMap<>();
        rawRelationships.put("Pete", "Nick");
        rawRelationships.put("Nick", "Steve");
        rawRelationships.put("Steve", "Victor");
        rawRelationships.put("Oleg", "Victor");
        rawRelationships.put("Ivan", "Oleg");
        rawRelationships.put("Victor", "Pete");

        String requestJson = objectMapper.writeValueAsString(rawRelationships);

        mockMvc.perform(post("/relationships")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.message", is("No root was found. The loop in requested data can be the reason.")));
    }

    @Test
    public void doubleKeys() throws Exception {
        checkRelationships("{\"Pete\":\"Nick\", \"Pete\":\"Jonas\"}", "{\"Jonas\":{\"Pete\":{}}}");
    }

    @Test
    public void testMultiplyRoots() throws Exception {
        Map<String, String> rawRelationships = new HashMap<>();
        rawRelationships.put("Pete", "Nick");
        rawRelationships.put("Nick", "Steve");
        rawRelationships.put("Oleg", "Victor");

        String requestJson = objectMapper.writeValueAsString(rawRelationships);

        mockMvc.perform(post("/relationships")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.message", is("There are more than 1 root in request. Roots are: [Victor, Steve]")));
    }

    private void checkRelationships(Map<String, String> rawRelationships, String expectedJson) throws Exception {
        checkRelationships(objectMapper.writeValueAsString(rawRelationships), expectedJson);
    }

    private void checkRelationships(String requestJson, String expectedJson) throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/relationships")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
        Map<String, Object> actual = objectMapper.readValue(contentAsString, typeRef);

        Map<String, Object> expected = objectMapper.readValue(expectedJson, typeRef);

        assertEquals(expected, actual);
    }

}