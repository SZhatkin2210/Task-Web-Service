package org.example.spring_boot_2.Task;

import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public Task toTask(
            TaskEntity task
    ){
        return new Task(
                task.getId(),
                task.getCreatorId(),
                task.getAssignedUserId(),
                task.getStatus(),
                task.getCreateDateTime(),
                task.getDeadlineDate(),
                task.getPriority(),
                task.getDoneDateTime()
        );
    }

    public TaskEntity toEntity(
            Task task
    ){
        return new TaskEntity(
                task.getId(),
                task.getCreatorId(),
                task.getAssignedUserId(),
                task.getStatus(),
                task.getCreateDateTime(),
                task.getDeadlineDate(),
                task.getPriority(),
                task.getDoneDateTime()
        );
    }
}
