package com.vlad.todo.service;

import com.vlad.todo.dto.GroupDtoRequest;
import com.vlad.todo.dto.GroupDtoResponse;
import com.vlad.todo.exception.NotFoundException;
import com.vlad.todo.exception.UpdateException;
import com.vlad.todo.mapper.GroupMapper;
import com.vlad.todo.model.GroupEntity;
import com.vlad.todo.repository.GroupRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GroupService {
    private final GroupMapper groupMapper;
    private GroupRepository groupRepository;

    public List<GroupDtoResponse> findAll() {
        List<GroupDtoResponse> groupsDtoResponse = new ArrayList<>();
        groupRepository.findAll().forEach(
                groupEntity -> groupsDtoResponse.add(groupMapper.toDto(groupEntity)));
        return groupsDtoResponse;
    }

    public GroupDtoResponse findById(long id) {
        GroupEntity taskEntity = groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Group with id %d not found", id)));
        return groupMapper.toDto(taskEntity);
    }

    public GroupDtoResponse save(GroupDtoRequest groupDtoRequest) {
        GroupEntity groupEntity = groupMapper.toEntity(groupDtoRequest);
        groupRepository.save(groupEntity);
        return groupMapper.toDto(groupEntity);
    }

    public GroupDtoResponse update(long id, GroupDtoRequest groupDtoRequest) {
        GroupEntity groupEntity = groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Group with id %d not found", id)));

        if (groupDtoRequest.getName() != null) {
            groupEntity.setName(groupDtoRequest.getName());
        }
        if (groupDtoRequest.getDescription() != null) {
            groupEntity.setDescription(groupDtoRequest.getDescription());
        }
        try {
            groupRepository.save(groupEntity);
            return groupMapper.toDto(groupEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new UpdateException("Error updating Group with id: " + id);
        }
    }

    public void deleteById(long id) {
        GroupEntity groupEntity = groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Group with id %d not found.", id)));
        groupEntity.getUsers().forEach(userEntity -> userEntity.getGroups().remove(groupEntity));
        groupRepository.deleteById(id);
    }
}
