package com.ddmtchr.vktestrestapi.controllers;

import com.ddmtchr.vktestrestapi.model.AlbumDTO;
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
class AlbumsControllerTest extends AbstractControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAlbums_ReturnsList() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/albums")).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        String content = result.getResponse().getContentAsString();
        AlbumDTO[] albumList = mapFromJson(content, AlbumDTO[].class);
        assertTrue(albumList.length > 0);
    }

    @Test
    void getAlbumById_ReturnsAlbum() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/albums/3")).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        String content = result.getResponse().getContentAsString();
        AlbumDTO album = mapFromJson(content, AlbumDTO.class);
        assertEquals(3L, (long) album.getId());
    }

    @Test
    void addAlbum_ReturnsCreated() throws Exception {
        AlbumDTO album = new AlbumDTO();
        album.setUserId(42L);
        album.setTitle("Test title");

        String inputJson = mapToJson(album);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/albums")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        AlbumDTO content = mapFromJson(result.getResponse().getContentAsString(), AlbumDTO.class);
        assertEquals(42L, content.getUserId());
        assertEquals("Test title", content.getTitle());
    }

    @Test
    void updateAlbum_ReturnsAlbum() throws Exception {
        AlbumDTO album = new AlbumDTO();
        album.setUserId(42L);
        album.setTitle("Test title title");

        String inputJson = mapToJson(album);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/albums/3")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        AlbumDTO content = mapFromJson(result.getResponse().getContentAsString(), AlbumDTO.class);
        assertEquals(42L, content.getUserId());
        assertEquals("Test title title", content.getTitle());
    }

    @Test
    void deleteAlbum_ReturnsOk() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/albums/3"))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }
}