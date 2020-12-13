package com.example.numbergenerator.resource;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.numbergenerator.dtos.TaskDTO;
import com.example.numbergenerator.exception.InternalServerException;
import com.example.numbergenerator.exception.InvalidRequestException;
import com.example.numbergenerator.exception.ResourceNotFoundException;
import com.example.numbergenerator.service.TaskService;

@RestController
@RequestMapping("/api")
public class TaskResource {

	private static final String INTERNAL_SERVER_EXCEPTION_MSG = "Exception while processing the request. Please try again.";

	@Autowired
	private TaskService taskService;

	@PostMapping("generate")
	public ResponseEntity<String> generate(@RequestBody TaskDTO taskDTO)
			throws InternalServerException, InvalidRequestException {
		return generateTasks(Arrays.asList(taskDTO));
	}

	@PostMapping("bulkGenerate")
	public ResponseEntity<String> bulkGenerate(@RequestBody List<TaskDTO> taskDTOs)
			throws InternalServerException, InvalidRequestException {
		return generateTasks(taskDTOs);
	}

	private ResponseEntity<String> generateTasks(List<TaskDTO> taskDTOs)
			throws InvalidRequestException, InternalServerException {
		validate(taskDTOs);
		String uuid = null;
		try {
			uuid = taskService.createTask(taskDTOs);
		} catch (Exception e) {
			throw new InternalServerException(INTERNAL_SERVER_EXCEPTION_MSG);
		}
		return new ResponseEntity<>(uuid, HttpStatus.OK);
	}

	@GetMapping("tasks/{uuid}/status")
	public ResponseEntity<List<String>> getStatus(String uuid)
			throws InternalServerException, ResourceNotFoundException, InvalidRequestException {
		try {
			if (StringUtils.isBlank(uuid)) {
				throw new InvalidRequestException("id can not be empty");
			}
			List<String> statuses = taskService.getTaskStatusForUUID(uuid);
			if (statuses.isEmpty()) {
				throw new ResourceNotFoundException("No resource available with id " + uuid);
			}
			return new ResponseEntity<>(statuses, HttpStatus.OK);
		} catch (ResourceNotFoundException | InvalidRequestException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalServerException(INTERNAL_SERVER_EXCEPTION_MSG);
		}
	}

	@GetMapping("tasks/{uuid}")
	public ResponseEntity<List<String>> getNumList(@PathVariable("uuid") String id, @RequestParam String action)
			throws InternalServerException, ResourceNotFoundException, InvalidRequestException {
		try {
			if (StringUtils.isBlank(id)) {
				throw new InvalidRequestException("id can not be empty");
			}
			List<String> numList = taskService.getNumListForUUID(id);
			if (numList.isEmpty()) {
				throw new ResourceNotFoundException("No resource available with id " + id);
			}
			return new ResponseEntity<>(numList, HttpStatus.OK);
		} catch (ResourceNotFoundException | InvalidRequestException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalServerException(INTERNAL_SERVER_EXCEPTION_MSG);
		}
	}

	private void validate(List<TaskDTO> taskDTOs) throws InvalidRequestException {
		for (int i = 0; i < taskDTOs.size(); i++) {
			TaskDTO taskDTO = taskDTOs.get(i);
			if (taskDTO.getGoal() == null || taskDTO.getStep() == null) {
				throw new InvalidRequestException("Neither goal nor step can be empty");
			}
			if (taskDTO.getGoal() <= 0 || taskDTO.getStep() <= 0) {
				throw new InvalidRequestException("Neither goal nor step can be negative");
			}
		}
	}
}
