package com.example.demo;

import com.example.demo.dto.CompleteUserTaskInputDto;
import com.example.demo.models.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.spring.boot.starter.test.helper.AbstractProcessEngineRuleTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;
import java.util.Calendar;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CamundaServicesTest  /*extends AbstractProcessEngineRuleTest */{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RuntimeService runtimeService;


    @Test
    void startNewProcessTest() throws Exception {
        var inst = runtimeService.createProcessInstanceByKey("SBootWF-01").businessKey("123").execute();
        Assert.notNull(inst, "Process instance is null");
    }

    @Test
    void completeUserTaskTest() throws Exception {
        var taskId = "";

        var taskDto = new CompleteUserTaskInputDto();
        taskDto.setTaskId(taskId);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/camunda-services/complete-user-task")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(taskDto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void TestWf(){
        var defKey = "SBootWF-01";
        var bussKey = "junit-" + Calendar.getInstance().getTime();
        var pInst = runtimeService.createProcessInstanceByKey(defKey).businessKey(bussKey).execute();
        Assert.notNull(pInst, "Process instance is null");

        //assertThat(pInst).isActive().isWaitingAt("Activity_0c5qady");

    }


    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}