package task.tracker.api.factories;

import org.springframework.stereotype.Component;
import task.tracker.api.dto.TaskDto;
import task.tracker.store.entities.TaskEntity;
@Component
public class TaskDtoFactory {
    public TaskDto makeTaskDto(TaskEntity entity){
        return TaskDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
