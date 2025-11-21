package com.vlad.todo.service;

import com.vlad.todo.dto.GroupTaskDtoRequest;
import com.vlad.todo.dto.GroupTaskDtoResponse;
import com.vlad.todo.model.Group;
import com.vlad.todo.model.GroupTask;
import com.vlad.todo.model.User;
import com.vlad.todo.exception.NotFoundException;
import com.vlad.todo.mapper.GroupTaskMapper;
import com.vlad.todo.repository.GroupRepository;
import com.vlad.todo.repository.GroupTaskRepository;
import com.vlad.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupTaskService {

    private final GroupTaskRepository groupTaskRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupTaskMapper mapper;

    public GroupTaskDtoResponse create(GroupTaskDtoRequest dto) {

        Group group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new NotFoundException("Группа не найдена"));

        User assignedUser = null;
        if (dto.getAssignedUserId() != null) {
            assignedUser = userRepository.findById(dto.getAssignedUserId())
                    .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

            if (!group.getUsers().contains(assignedUser)) {
                throw new NotFoundException("Пользователь не состоит в этой группе");
            }
        }

        GroupTask task = new GroupTask();
        task.setTitle(dto.getTitle());
        task.setContent(dto.getContent());
        task.setIsCompleted(dto.getIsCompleted());
        task.setIsImportant(dto.getIsImportant());
        task.setDeadlineDate(dto.getDeadlineDate());
        task.setGroup(group);
        task.setAssignedUser(assignedUser);

        return mapper.toDto(groupTaskRepository.save(task));
    }

    public GroupTaskDtoResponse update(Long id, GroupTaskDtoRequest dto) {
        GroupTask task = groupTaskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        if (task.getGroup() == null) {
            throw new NotFoundException("Группа для задачи не найдена");
        }

        task.setTitle(dto.getTitle());
        task.setContent(dto.getContent());
        task.setIsCompleted(dto.getIsCompleted());
        task.setIsImportant(dto.getIsImportant());
        task.setDeadlineDate(dto.getDeadlineDate());

        return mapper.toDto(groupTaskRepository.save(task));
    }

    public GroupTaskDtoResponse findById(Long id) {
        GroupTask task = groupTaskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Задача с id " + id + " не найдена"));

        if (task.getGroup() == null) {
            throw new NotFoundException("Группа для задачи с id " + id + " не найдена");
        }

        return mapper.toDto(task);
    }

    public List<GroupTaskDtoResponse> findByGroup(Long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new NotFoundException("Группа с id " + groupId + " не найдена");
        }
        return groupTaskRepository.findByGroupId(groupId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<GroupTaskDtoResponse> findByAssignedUser(Long userId) {
        return groupTaskRepository.findByAssignedUserId(userId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public void delete(Long id) {
        if (!groupTaskRepository.existsById(id)) {
            throw new NotFoundException("Задача с id " + id + " не найдена");
        }
        groupTaskRepository.deleteById(id);
    }

    public void assignUser(Long taskId, Long userId) {
        GroupTask task = groupTaskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        if (task.getGroup() == null) {
            throw new NotFoundException("Группа для этой задачи не найдена");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (!task.getGroup().getUsers().contains(user)) {
            throw new NotFoundException("Пользователь не состоит в этой группе");
        }

        task.setAssignedUser(user);
        groupTaskRepository.save(task);
    }

    public void unassignUser(Long taskId) {
        GroupTask task = groupTaskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        if (task.getGroup() == null) {
            throw new NotFoundException("Группа для этой задачи не найдена");
        }

        task.setAssignedUser(null);
        groupTaskRepository.save(task);
    }
}
