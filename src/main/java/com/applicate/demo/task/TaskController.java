package com.applicate.demo.task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8085")
@RestController
@RequestMapping("/v1/tasks")
public class TaskController {
    private final TaskService taskService;

    // constructor injection
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * api for creating a new task
     */
    @PostMapping()
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        try {
            Task _task = taskService.saveTask(task);
            return new ResponseEntity<>(_task, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * api for changing the status of a task
     */
    @PatchMapping("/status/{id}/{status}")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable("id") long id, @PathVariable String status) {
        try {
            Optional<Task> _task = taskService.findTaskById(id);
            if (_task.isPresent()) {
                Task task = _task.get();
                task.setStatus(status);
                return new ResponseEntity<>(taskService.saveTask(task), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * api for deleting a task
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable("id") long id) {
        try {
            taskService.deleteTask(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * api for extending the time of a task
     */
    @PatchMapping("/extendDays/{id}/{days}")
    public ResponseEntity<Task> extendTaskDate(@PathVariable("id") long id, @PathVariable int days) {
        try {
            Optional<Task> _task = taskService.findTaskById(id);
            if (_task.isPresent()) {
                Task task = _task.get();
                Date extendedDate = getExtendedDate(task.getEndDate(), days);
                task.setEndDate(extendedDate);
                return new ResponseEntity<>(taskService.saveTask(task), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * api for finding the due tasks
     */
    @GetMapping("/due")
    public ResponseEntity<List<Task>> getDueTasks() {
        try {
            List<Task> tasks = taskService.findDueTasks();
            if (tasks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * api for finding the tasks based on owner
     */
    @GetMapping("/owner/{owner}")
    public ResponseEntity<List<Task>> getTasksBasedOnOwner(@PathVariable String owner) {
        try {
            List<Task> tasks = taskService.findTasksBasedOnOwner(owner);
            if (tasks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * api for finding the tasks based on assignee
     */
    @GetMapping("/assignee/{assignee}")
    public ResponseEntity<List<Task>> getTasksBasedOnAssignee(@PathVariable String assignee) {
        try {
            List<Task> tasks = taskService.findTasksBasedOnAssignee(assignee);
            if (tasks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public Date getExtendedDate(Date currentDate, int daysToExtend) {
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, daysToExtend);
        return c.getTime();
    }
}
