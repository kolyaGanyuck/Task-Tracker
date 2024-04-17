package task.tracker.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import task.tracker.store.entities.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
