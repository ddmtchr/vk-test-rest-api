package com.ddmtchr.vktestrestapi.controllers;

import com.ddmtchr.vktestrestapi.database.entities.AuditLog;
import com.ddmtchr.vktestrestapi.database.repository.AuditRepository;
import com.ddmtchr.vktestrestapi.model.PostDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

class AuditAspectTest extends AbstractControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuditRepository auditRepository;

    @Test
    @WithAnonymousUser
    void testAudit_Unauthorized_ReturnsAuditLog() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        AuditLog log = auditRepository.findFirstByOrderByTimestampDesc();

        assertFalse(log.getHasAccess());
        assertNull(log.getUsername());
        assertEquals("/posts", log.getRequestUrl());
        assertEquals("GET", log.getRequestMethod());
        assertEquals(401, log.getResponseStatus());
    }

    @Test
    @WithMockUser(roles = "USERS")
    void testAudit_NoSuchRole_ReturnsAuditLog() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
        AuditLog log = auditRepository.findFirstByOrderByTimestampDesc();

        assertFalse(log.getHasAccess());
        assertEquals("user", log.getUsername());
        assertEquals("/posts", log.getRequestUrl());
        assertEquals("GET", log.getRequestMethod());
        assertEquals(403, log.getResponseStatus());
    }

    @Test
    @WithMockUser(roles = "POSTS")
    void testAudit_AuthorizedGet_ReturnsAuditLog() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        AuditLog log = auditRepository.findFirstByOrderByTimestampDesc();

        assertTrue(log.getHasAccess());
        assertEquals("user", log.getUsername());
        assertEquals("/posts", log.getRequestUrl());
        assertEquals("GET", log.getRequestMethod());
        assertEquals(200, log.getResponseStatus());
    }

    @Test
    @WithMockUser(roles = "POSTS")
    void testAudit_AuthorizedPost_ReturnsAuditLog() throws Exception {
        PostDTO post = new PostDTO();
        post.setUserId(42L);
        post.setTitle("Test title");
        post.setBody("Test body");
        String inputJson = mapToJson(post);
        mockMvc.perform(MockMvcRequestBuilders.post("/posts").contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        AuditLog log = auditRepository.findFirstByOrderByTimestampDesc();

        assertTrue(log.getHasAccess());
        assertEquals("user", log.getUsername());
        assertEquals("/posts", log.getRequestUrl());
        assertEquals("POST", log.getRequestMethod());
        assertEquals(201, log.getResponseStatus());
    }

    @Test
    @WithMockUser(roles = "POSTS")
    void testAudit_NotFoundPost_ReturnsAuditLog() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/1000"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        AuditLog log = auditRepository.findFirstByOrderByTimestampDesc();

        assertTrue(log.getHasAccess());
        assertEquals("user", log.getUsername());
        assertEquals("/posts/1000", log.getRequestUrl());
        assertEquals("GET", log.getRequestMethod());
        assertEquals(404, log.getResponseStatus());
    }
}
