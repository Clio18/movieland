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
class DbRiderGenreControllerTest extends AbstractBaseITest {

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
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[3].id").value(4))
                .andExpect(jsonPath("$[4].id").value(5))
                .andExpect(jsonPath("$[5].id").value(6))
                .andExpect(jsonPath("$[6].id").value(7))
                .andExpect(jsonPath("$[7].id").value(8))
                .andExpect(jsonPath("$[8].id").value(9))
                .andExpect(jsonPath("$[9].id").value(10))
                .andExpect(content().json(getResponseAsString("response/genres/get-all-genres.json")));
    }


}
