package com.ddmtchr.vktestrestapi.controllers;

import com.ddmtchr.vktestrestapi.model.PostDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WithMockUser(roles = "ADMIN")
class PostsControllerTest extends AbstractControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getPosts_ReturnsList() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/posts")).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        String content = result.getResponse().getContentAsString();
        PostDTO[] postList = mapFromJson(content, PostDTO[].class);
        assertTrue(postList.length > 0);
    }

    @Test
    void getPostById_ReturnsPost() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/posts/3")).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        String content = result.getResponse().getContentAsString();
        PostDTO post = mapFromJson(content, PostDTO.class);
        assertEquals(3L, (long) post.getId());
    }

    @Test
    void getPostById_NoSuchId_ReturnsNotFound() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/posts/333")).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    void getPostById_WrongPath_ReturnsBadRequest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/posts/aaa")).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void addPost_ReturnsCreated() throws Exception {
        PostDTO post = new PostDTO();
        post.setUserId(42L);
        post.setTitle("Test title");
        post.setBody("Test body");

        String inputJson = mapToJson(post);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        PostDTO content = mapFromJson(result.getResponse().getContentAsString(), PostDTO.class);
        assertEquals(42L, content.getUserId());
        assertEquals("Test title", content.getTitle());
        assertEquals("Test body", content.getBody());
    }

    @Test
    void getPostById_CachedPost_ReturnsPost() throws Exception {
        PostDTO post = new PostDTO();
        post.setUserId(42L);
        post.setTitle("Test title");
        post.setBody("Test body");

        String inputJson = mapToJson(post);
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        assertEquals(HttpStatus.CREATED.value(), result1.getResponse().getStatus());
        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.get("/posts/101")).andReturn();
        PostDTO content = mapFromJson(result2.getResponse().getContentAsString(), PostDTO.class);

        assertEquals(HttpStatus.OK.value(), result2.getResponse().getStatus());
        assertEquals(42L, content.getUserId());
        assertEquals("Test title", content.getTitle());
        assertEquals("Test body", content.getBody());
    }

    @Test
    void updatePost_ReturnsPost() throws Exception {
        PostDTO post = new PostDTO();
        post.setUserId(42L);
        post.setTitle("Test title title");
        post.setBody("Test body");

        String inputJson = mapToJson(post);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/posts/3")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        PostDTO content = mapFromJson(result.getResponse().getContentAsString(), PostDTO.class);
        assertEquals(42L, content.getUserId());
        assertEquals("Test title title", content.getTitle());
        assertEquals("Test body", content.getBody());
    }

    @Test
    void deletePost_ReturnsOk() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/posts/3"))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }
}