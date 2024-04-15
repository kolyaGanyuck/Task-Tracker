package task.tracker.store.entities;

import jakarta.persistence.*;

import java.time.Instant;


@Entity
@Table(name = "task")
public class TaskEntity {
    @Id
    Long id;
    @Column(unique = true)
    String name;
    Instant createdAt = Instant.now();
    String description;

}
