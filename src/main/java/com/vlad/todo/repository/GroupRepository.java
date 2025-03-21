package com.vlad.todo.repository;

import com.vlad.todo.model.GroupEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
    Optional<GroupEntity> findById(long id);

    void deleteById(long id);
}
