package com.example.numbergenerator.resource;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.example.numbergenerator.common.Constants;
import com.example.numbergenerator.dtos.TaskDTO;
import com.example.numbergenerator.exception.InternalServerException;
import com.example.numbergenerator.exception.InvalidRequestException;
import com.example.numbergenerator.exception.ResourceNotFoundException;
import com.example.numbergenerator.service.TaskService;

/**
 * Resource layer. It handles request mapping for the APIs and validation of input.
 * @author vivek
 *
 */
@RestController
@RequestMapping("/api")
public class TaskResource {

	private Logger logger = LoggerFactory.getLogger(TaskResource.class);
	
	private static final String INTERNAL_SERVER_EXCEPTION_MSG = "Exception while processing the request. Please try again.";

	@Autowired
	private TaskService taskService;
	
	/**
	 * Generic welcome message method
	 * @return welcome message
	 */
	@GetMapping("/")
    public String index() {
        return "Welcome to Number Generator";
    }

	/**
	 * Creates one number generating task.
	 * @param taskDTO - Input from the user
	 * @return id for the task.
	 * @throws InternalServerException
	 * @throws InvalidRequestException
	 */
	@PostMapping("generate")
	public ResponseEntity<String> generate(@RequestBody TaskDTO taskDTO)
			throws InternalServerException, InvalidRequestException {
		return generateTasks(Arrays.asList(taskDTO));
	}

	/**
	 * Creates number generating tasks for all input.
	 * @param taskDTOs - Bulk tasks from the user.
	 * @return id for the tasks
	 * @throws InternalServerException
	 * @throws InvalidRequestException
	 */
	@PostMapping("bulkGenerate")
	public ResponseEntity<String> bulkGenerate(@RequestBody List<TaskDTO> taskDTOs)
			throws InternalServerException, InvalidRequestException {
		return generateTasks(taskDTOs);
	}

	private ResponseEntity<String> generateTasks(List<TaskDTO> taskDTOs)
			throws InvalidRequestException, InternalServerException {
		logger.info("Generate tasks called for {}", taskDTOs);
		validate(taskDTOs);
		String uuid = null;
		try {
			uuid = taskService.createTask(taskDTOs);
		} catch (Exception e) {
			logger.error("Internal error for {}", taskDTOs, e);
			throw new InternalServerException(INTERNAL_SERVER_EXCEPTION_MSG);
		}
		logger.info("id created for the tasks: {} is {}", taskDTOs, uuid);
		return new ResponseEntity<>(uuid, HttpStatus.OK);
	}

	/**
	 * Returns list of status for the tasks with the provided id.
	 * @param uuid - id of the tasks for which status is queried.
	 * @return list of statuses.
	 * @throws InternalServerException
	 * @throws ResourceNotFoundException
	 */
	@GetMapping("tasks/{uuid}/status")
	public ResponseEntity<List<String>> getStatus(@PathVariable("uuid") String uuid)
			throws InternalServerException, ResourceNotFoundException {
		try {
			logger.info("Status fetch api called for id: {}", uuid);
			List<String> statuses = taskService.getTaskStatusForUUID(uuid);
			if (statuses.isEmpty()) {
				throw new ResourceNotFoundException("No resource available with id " + uuid);
			}
			return new ResponseEntity<>(statuses, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			logger.error("Internal error fetching status for {}", uuid, e);
			throw new InternalServerException(INTERNAL_SERVER_EXCEPTION_MSG);
		}
	}

	/**
	 * Returns list of numbers generated for the tasks with the given id.
	 * @param id - id of the tasks
	 * @param action - get_numlist indicates that number list is required.
	 * @return list of number list.
	 * @throws InternalServerException
	 * @throws ResourceNotFoundException
	 * @throws InvalidRequestException
	 */
	@GetMapping("tasks/{uuid}")
	public ResponseEntity<List<String>> getNumList(@PathVariable("uuid") String id, @RequestParam String action)
			throws InternalServerException, ResourceNotFoundException, InvalidRequestException {
		try {
			logger.info("Number list fetch api called for id: {}", id);
			if (!Constants.GET_NUMLIST.equals(action)) {
				throw new InvalidRequestException("Unsupported Operation: " + action);
			}
			List<String> numList = taskService.getNumListForUUID(id);
			if (numList.isEmpty()) {
				throw new ResourceNotFoundException("No resource available with id " + id);
			}
			return new ResponseEntity<>(numList, HttpStatus.OK);
		} catch (ResourceNotFoundException | InvalidRequestException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			logger.error("Internal error fetching number list for {}", id, e);
			throw new InternalServerException(INTERNAL_SERVER_EXCEPTION_MSG);
		}
	}

	private void validate(List<TaskDTO> taskDTOs) throws InvalidRequestException {
		for (int i = 0; i < taskDTOs.size(); i++) {
			TaskDTO taskDTO = taskDTOs.get(i);
			if (taskDTO.getGoal() == null || taskDTO.getStep() == null) {
				logger.error("Invalid request. Goal or step is empty: {}", taskDTOs);
				throw new InvalidRequestException("Neither goal nor step can be empty");
			}
			if (taskDTO.getGoal() <= 0 || taskDTO.getStep() <= 0) {
				logger.error("Invalid request. Goal or step is negative: {}", taskDTOs);
				throw new InvalidRequestException("Neither goal nor step can be negative");
			}
		}
	}
}
