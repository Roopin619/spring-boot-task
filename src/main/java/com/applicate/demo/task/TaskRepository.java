package com.applicate.demo.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByEndDateGreaterThanEqualAndStatusNot(Date currentDate, String status);
    List<Task> findByOwner(String owner);
    List<Task> findByAssignee(String assignee);
}
