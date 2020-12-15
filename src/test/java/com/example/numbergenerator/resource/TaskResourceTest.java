package com.example.numbergenerator.resource;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.numbergenerator.common.Constants;
import com.example.numbergenerator.dtos.TaskDTO;
import com.example.numbergenerator.entity.TaskStatus;
import com.example.numbergenerator.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TaskResource.class)
class TaskResourceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TaskService taskService;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void testGenerateValid() throws Exception {
		UUID uuid = UUID.randomUUID();
		when(taskService.createTask(Mockito.anyList())).thenReturn(uuid.toString());

		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setGoal(10);
		taskDTO.setStep(2);
		this.mockMvc
				.perform(post("/api/generate").content(objectMapper.writeValueAsString(taskDTO))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(uuid.toString())));
	}

	@Test
	void testGenerateInternalServerErro() throws Exception {
		when(taskService.createTask(Mockito.anyList())).thenThrow(new RuntimeException());

		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setGoal(10);
		taskDTO.setStep(2);
		this.mockMvc
				.perform(post("/api/generate").content(objectMapper.writeValueAsString(taskDTO))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().is5xxServerError());
	}

	@Test
	void testGenerateInvalidRequestEmptyGoal() throws Exception {
		UUID uuid = UUID.randomUUID();
		when(taskService.createTask(Mockito.anyList())).thenReturn(uuid.toString());

		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setStep(2);
		this.mockMvc
				.perform(post("/api/generate").content(objectMapper.writeValueAsString(taskDTO))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	void testGenerateInvalidRequestNegStep() throws Exception {
		UUID uuid = UUID.randomUUID();
		when(taskService.createTask(Mockito.anyList())).thenReturn(uuid.toString());

		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setGoal(10);
		taskDTO.setStep(-2);
		this.mockMvc
				.perform(post("/api/generate").content(objectMapper.writeValueAsString(taskDTO))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	void testBulkGenerateValid() throws Exception {
		UUID uuid = UUID.randomUUID();
		when(taskService.createTask(Mockito.anyList())).thenReturn(uuid.toString());

		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setGoal(10);
		taskDTO.setStep(2);

		TaskDTO taskDTO1 = new TaskDTO();
		taskDTO1.setGoal(100);
		taskDTO1.setStep(3);

		this.mockMvc
				.perform(
						post("/api/bulkGenerate").content(objectMapper.writeValueAsString(Arrays.asList(taskDTO, taskDTO1)))
								.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(uuid.toString())));
	}
	
	@Test
	void testGetStatusValid() throws Exception {
		List<String> statuses = Arrays.asList(TaskStatus.IN_PROGRESS.getValue(), TaskStatus.ERROR.getValue(), TaskStatus.SUCCESS.getValue());
		when(taskService.getTaskStatusForUUID(Mockito.any())).thenReturn(statuses);
		
		this.mockMvc.perform(get("/api/tasks/random-uuid-placeholder/status")).andDo(print()).andExpect(status().isOk()).andExpect(content().string(objectMapper.writeValueAsString(statuses)));
	}
	
	@Test
	void testGetStatusInvalidEmptyStatuses() throws Exception {
		List<String> statuses = new ArrayList<>();
		when(taskService.getTaskStatusForUUID(Mockito.any())).thenReturn(statuses);
		
		this.mockMvc.perform(get("/api/tasks/random-uuid-placeholder/status")).andDo(print()).andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}

	@Test
	void testGetStatusInvalidInternalServerException() throws Exception {
		when(taskService.getTaskStatusForUUID(Mockito.any())).thenThrow(new RuntimeException());
		
		this.mockMvc.perform(get("/api/tasks/random-uuid-placeholder/status")).andDo(print()).andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}
	
	@Test
	void testGetNumListValid() throws Exception {
		List<String> numList = Arrays.asList("10,8,6,4,2,0", "10,5,0");
		when(taskService.getNumListForUUID(Mockito.any())).thenReturn(numList);
		
		this.mockMvc.perform(get("/api/tasks/random-uuid-placeholder").param("action", Constants.GET_NUMLIST)).andDo(print())
			.andExpect(status().isOk()).andExpect(content().string(objectMapper.writeValueAsString(numList)));
	}
	
	@Test
	void testGetNumListInvalidWrongAction() throws Exception {
		List<String> numList = Arrays.asList("10,8,6,4,2,0", "10,5,0");
		when(taskService.getNumListForUUID(Mockito.any())).thenReturn(numList);
	
		this.mockMvc.perform(get("/api/tasks/random-uuid-placeholder").param("action", "get_status")).andDo(print())
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void testGetNumListInvalidEmptyNumList() throws Exception {
		List<String> numList = new ArrayList<>();
		when(taskService.getNumListForUUID(Mockito.any())).thenReturn(numList);
	
		this.mockMvc.perform(get("/api/tasks/random-uuid-placeholder").param("action", Constants.GET_NUMLIST)).andDo(print())
			.andExpect(status().isNotFound());
	}
	
	@Test
	void testGetNumListInvalidInternalServerErro() throws Exception {
		when(taskService.getNumListForUUID(Mockito.any())).thenThrow(new RuntimeException());
	
		this.mockMvc.perform(get("/api/tasks/random-uuid-placeholder").param("action", Constants.GET_NUMLIST)).andDo(print())
			.andExpect(status().isInternalServerError());
	}
	
}
