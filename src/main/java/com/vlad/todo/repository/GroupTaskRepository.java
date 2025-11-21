package com.vlad.todo.repository;

import com.vlad.todo.model.GroupTask;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupTaskRepository extends JpaRepository<GroupTask, Long> {

    List<GroupTask> findByGroupId(Long groupId);

    List<GroupTask> findByAssignedUserId(Long userId);
}