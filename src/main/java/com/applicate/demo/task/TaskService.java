package com.applicate.demo.task;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    // constructor injection
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAllTasks(){
        return taskRepository.findAll();
    }

    public List<Task> findDueTasks(){
        return taskRepository.findByEndDateGreaterThanEqualAndStatusNot(new Date(), "COMPLETED");
    }

    public List<Task> findTasksBasedOnOwner(String owner){
        return taskRepository.findByOwner(owner);
    }

    public List<Task> findTasksBasedOnAssignee(String assignee){
        return taskRepository.findByAssignee(assignee);
    }

    public Task saveTask(Task task){
        return taskRepository.save(task);
    }

    public Optional<Task> findTaskById(long taskId){
        return taskRepository.findById(taskId);
    }

    public void deleteTask(long taskId){
        taskRepository.deleteById(taskId);
    }
}
