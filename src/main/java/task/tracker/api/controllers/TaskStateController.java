package task.tracker.api.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import task.tracker.api.controllers.helpers.ControllerHelper;
import task.tracker.api.dto.ProjectDto;
import task.tracker.api.dto.TaskStateDto;
import task.tracker.api.exception.BadRequestException;
import task.tracker.api.factories.TaskStateDtoFactory;
import task.tracker.store.entities.ProjectEntity;
import task.tracker.store.entities.TaskStateEntity;
import task.tracker.store.repositories.TaskStateRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
public class TaskStateController {
    ControllerHelper controllerHelper;
    TaskStateRepository taskStateRepository;
    TaskStateDtoFactory taskStateDtoFactory;
    public static final String FETCH_TASK_STATES = "/api/projects/{project_id}/task-states";
    public static final String CREATE_TASK_STATE = "/api/projects/{project_id}/task-states";
//    public static final String CREATE_TASK_STATE = "/api/projects";
    public static final String DELETE_PROJECT = "/api/projects/delete/{project_id}";

    @GetMapping(FETCH_TASK_STATES)
    public List<TaskStateDto> getTaskStates(
            @PathVariable(value = "project_id", required = false) Long projectId) {
        ProjectEntity project = controllerHelper.getProjectOrThrowException(projectId);
        return project.getTaskStates().stream()
                .map(taskStateDtoFactory::makeTaskStateDto)
                .collect(Collectors.toList());
    }

    @PostMapping(CREATE_TASK_STATE)
    public TaskStateDto createTaskSate(
            @PathVariable(name = "project_id") Long projectId,
            @RequestParam(name = "task_state_name") String taskStateName) {

        if (taskStateName.trim().isEmpty()) {
            throw new BadRequestException("Task state name can't be empty.");
        }

        ProjectEntity project = controllerHelper.getProjectOrThrowException(projectId);

        Optional<TaskStateEntity> optionalAnotherTaskState = Optional.empty();

        for (TaskStateEntity taskState : project.getTaskStates()) {

            if (taskState.getName().equalsIgnoreCase(taskStateName)) {
                throw new BadRequestException(String.format("Task state \"%s\" already exists.", taskStateName));
            }

            if (!taskState.getRightTaskState().isPresent()) {
                optionalAnotherTaskState = Optional.of(taskState);
                break;
            }
        }

        TaskStateEntity taskState = taskStateRepository.saveAndFlush(
                TaskStateEntity.builder()
                        .name(taskStateName)
                        .project(project)
                        .build()
        );

        optionalAnotherTaskState
                .ifPresent(anotherTaskState -> {

                    taskState.setLeftTaskState(anotherTaskState);

                    anotherTaskState.setRightTaskState(taskState);

                    taskStateRepository.saveAndFlush(anotherTaskState);
                });

        final TaskStateEntity savedTaskState = taskStateRepository.saveAndFlush(taskState);

        return taskStateDtoFactory.makeTaskStateDto(savedTaskState);
    }
}