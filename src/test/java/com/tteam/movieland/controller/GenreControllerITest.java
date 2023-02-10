package com.tteam.movieland.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.tteam.movieland.AbstractBaseITest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@DBRider
@AutoConfigureMockMvc(addFilters = false)
class GenreControllerITest extends AbstractBaseITest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @DisplayName("Test GetAll Genres")
    void testGetAllGenres() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/genres")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString("response/genres/get-all-genres.json")));
    }


}
