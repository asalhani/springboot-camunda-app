package com.example.demo;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.models.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Calendar;

@SpringBootTest
@AutoConfigureMockMvc
class StudentTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllTestApi() throws Exception {
        this.mockMvc
                .perform(get("/api/student"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void addStudentTestApi() throws Exception {
        var s = new Student();
        s.setName("adib - " + Calendar.getInstance().getTime().toString());

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/student")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(s)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}