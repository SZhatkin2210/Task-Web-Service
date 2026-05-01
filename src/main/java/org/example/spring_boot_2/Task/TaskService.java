package org.example.spring_boot_2.Task;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {
    private final TaskMapper mapper;

    private final TaskRepository taskRepository;

    public TaskService(TaskMapper mapper, TaskRepository taskRepository) {
        this.mapper = mapper;
        this.taskRepository = taskRepository;
    }

    public Task getTaskById(long id) {
        var task = taskRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Not found task id:" + id));
        return mapper.toTask(task);
    }

    public List<Task> searchAllTaskByFilter(TaskSearchFilter filter) {
        int pageSize = filter.pageSize() != null ? filter.pageSize() : 10;
        int pageNumber = filter.pageNumber() != null ? filter.pageNumber() : 0;
        var pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
        List<TaskEntity> allEntites = taskRepository.searchAllTaskByFilter(
                filter.creatorId(),
                filter.assignedUserId(),
                filter.status(),
                filter.priority(),
                pageable
        );
        List<Task> allTasks = allEntites.stream()
                .map(e ->
                        mapper.toTask(e)).toList();
        return allTasks;
    }

    public Task createTask(Task taskToCreate) {
        if (taskToCreate.status != null){
            throw  new IllegalArgumentException("Task status is null");
        }
        taskToCreate.setStatus(Status.CREATED);
        TaskEntity taskEntity = mapper.toEntity(taskToCreate);
        var savedEntity = taskRepository.save(taskEntity);
        return mapper.toTask(savedEntity);
    }

    public void deleteTaskById(long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Not found task id:" + id);
        }
        taskRepository.deleteById(id);
    }

    public Task updateTask(long id, Task newTask) {
        TaskEntity oldTask = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found task id:" + id));
        if (oldTask.getStatus() == Status.DONE){
            throw new IllegalStateException("Status is DONE");
        }
        newTask.setId(oldTask.getId());
        TaskEntity newEntity = mapper.toEntity(newTask);
        taskRepository.save(newEntity);
        return mapper.toTask(newEntity);
    }


    public Task activateTask(long id) {
        TaskEntity taskEntity =  taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found task id:" + id));
        if (taskEntity.getAssignedUserId() == null) {
            throw  new IllegalArgumentException("Not found AssignedUserId in task id:" + id);
        }
        if (taskRepository.findByAssignedUserIdAndStatus(taskEntity.getAssignedUserId(), Status.IN_PROGRESS).size() > 4) {
            throw new IllegalStateException("У пользователя с id:" + id + " в данный момент более 4 задач");
        }
        taskEntity.setStatus(Status.IN_PROGRESS);
        taskRepository.save(taskEntity);
        return mapper.toTask(taskEntity);
    }

    public Task finishedTask(long id) {
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found task id:" + id));
        if  (taskEntity.getAssignedUserId() == null || taskEntity.getDeadlineDate() == null) {
            throw  new IllegalArgumentException("Not found AssignedUserId or DeadlineDate in task id:" + id);
        }
        if (taskEntity.getStatus() == Status.DONE) {
            throw new IllegalStateException("Status is DONE");
        }
        taskEntity.setDoneDateTime(LocalDateTime.now());
        taskEntity.setStatus(Status.DONE);
        taskRepository.save(taskEntity);
        return mapper.toTask(taskEntity);
    }
}
