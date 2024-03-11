package com.ddmtchr.vktestrestapi.controllers;

import com.ddmtchr.vktestrestapi.model.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WithMockUser(roles = "ADMIN")
class UsersControllerTest extends AbstractControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getUsers_ReturnsList() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users")).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        String content = result.getResponse().getContentAsString();
        UserDTO[] userList = mapFromJson(content, UserDTO[].class);
        assertTrue(userList.length > 0);
    }

    @Test
    void getUserById_ReturnsUser() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users/3")).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        String content = result.getResponse().getContentAsString();
        UserDTO user = mapFromJson(content, UserDTO.class);
        assertEquals(3L, (long) user.getId());
    }

    @Test
    void addUser_ReturnsCreated() throws Exception {
        UserDTO user = new UserDTO();
        user.setName("Test name");
        user.setUsername("Test username");
        user.setEmail("Test email");

        String inputJson = mapToJson(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        UserDTO content = mapFromJson(result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals("Test name", content.getName());
        assertEquals("Test username", content.getUsername());
        assertEquals("Test email", content.getEmail());
    }

    @Test
    void updateUser_ReturnsUser() throws Exception {
        UserDTO user = new UserDTO();
        user.setUsername("Test username");
        user.setEmail("Test email");

        String inputJson = mapToJson(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/users/3")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        UserDTO content = mapFromJson(result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals("Test username", content.getUsername());
        assertEquals("Test email", content.getEmail());
    }

    @Test
    void deleteUser_ReturnsOk() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/users/3"))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }
}