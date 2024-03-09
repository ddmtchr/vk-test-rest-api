package com.ddmtchr.vktestrestapi.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class ControllersSecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ALBUMS")
    public void testAlbumsRoleCanAccessAlbumsEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/albums"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminRoleCanAccessAlbumsEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/albums").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = {"POSTS", "USERS"})
    public void testNotAlbumsRoleCanNotAccessAlbumsEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/albums").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USERS")
    public void testUsersRoleCanAccessUsersEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminRoleCanAccessUsersEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = {"POSTS", "ALBUMS"})
    public void testNotUsersRoleCanNotAccessUsersEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "POSTS")
    public void testPostsRoleCanAccessPostsEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminRoleCanAccessPostsEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USERS", "ALBUMS"})
    public void testNotPostsRoleCanNotAccessPostsEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
