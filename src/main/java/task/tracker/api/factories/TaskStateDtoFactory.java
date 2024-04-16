package task.tracker.api.factories;


import task.tracker.api.dto.TaskStateDto;
import task.tracker.store.entities.TaskStateEntity;

public class TaskStateDtoFactory {
    public TaskStateDto makeTaskStateDto(TaskStateEntity entity) {
        return TaskStateDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .ordinal(entity.getOrdinal())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
