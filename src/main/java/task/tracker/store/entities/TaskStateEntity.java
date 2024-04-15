package task.tracker.store.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task_state")
public class TaskStateEntity {
    @Id
    Long id;
    @Column(unique = true)
    String name;
    int ordinal;
    Instant createdAt = Instant.now();
    @OneToMany
List<TaskEntity> taskEntityList = new ArrayList<>();
}
