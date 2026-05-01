package org.example.spring_boot_2.Task;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController()
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;

    private static final Logger log = Logger.getLogger(TaskController.class.getName());

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
            @PathVariable("id") long id) {
                log.info("Called method getTaskById id: " + id);
                return ResponseEntity.ok(taskService.getTaskById(id));
    }


    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(
            @RequestParam("creatorId") Long creatorId,
            @RequestParam("assignedUserId") Long assignedUserId,
            @RequestParam("status") Status status,
            @RequestParam("priority") Priority priority,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNumber") Integer pageNumber
    ){
        log.info("Called method getAllTasks");
        log.info("creatorId: {}" + creatorId);
        log.info("assignedUserId: {}" +  assignedUserId);
        log.info("status: {}" +  status);
        log.info("priority: {}" +  priority);
        log.info("pageSize: {}" + pageSize);
        log.info("pageNumber: {}" + pageNumber);
        var filter = new TaskSearchFilter(
                creatorId,
                assignedUserId,
                status,
                priority,
                pageSize,
                pageNumber
        );
        return ResponseEntity.ok(taskService.searchAllTaskByFilter(filter));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(
            @RequestBody @Valid Task newTask
    ){
        log.info("Called method createTask");
            return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(newTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskById(
            @PathVariable("id") long id
    ){
        log.info("Called method deleteTaskById  id: " + id);
            taskService.deleteTaskById(id);
            return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable("id") long id,
            @RequestBody @Valid Task newTask
    ){
        log.info("Called method updateTask id: " + id);
            return ResponseEntity.ok(taskService.updateTask(id, newTask));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Task> activateTask(
            @PathVariable("id") long id
    ){
        log.info("Called method activateTask  id: " + id);
            return ResponseEntity.ok(taskService.activateTask(id));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Task> finishedTask(
            @PathVariable("id") long id
    ){
        log.info("Called method finishedTask  id: " + id);
        return ResponseEntity.ok(taskService.finishedTask(id));
    }


}
