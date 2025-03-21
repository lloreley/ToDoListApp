package com.vlad.todo.service;

import com.vlad.todo.dto.UserDtoRequest;
import com.vlad.todo.dto.UserDtoResponse;
import com.vlad.todo.exception.CreationException;
import com.vlad.todo.exception.NotFoundException;
import com.vlad.todo.exception.UpdateException;
import com.vlad.todo.mapper.UserMapper;
import com.vlad.todo.model.GroupEntity;
import com.vlad.todo.model.UserEntity;
import com.vlad.todo.repository.GroupRepository;
import com.vlad.todo.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public List<UserDtoResponse> findAll() {
        List<UserDtoResponse> usersDtoResponse = new ArrayList<>();
        userRepository.findAll().forEach(
                userEntity -> usersDtoResponse.add(userMapper.toDto(userEntity)));
        return usersDtoResponse;
    }

    public void addUserToGroup(long userId, long groupId) {
        GroupEntity groupEntity = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Group with id %d does not exist", groupId)));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User with id %d does not exist", userId)));
        groupEntity.addUser(user);
        groupRepository.save(groupEntity);
    }

    public void removeUserFromGroup(long userId, long groupId) {
        GroupEntity groupEntity = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Group with id %d does not exist", groupId)));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User with id %d does not exist", userId)));
        groupEntity.removeUser(user);
        groupRepository.save(groupEntity);
    }

    public UserDtoResponse findById(long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User with id %d not found", id)));
        return userMapper.toDto(userEntity);
    }

    public UserDtoResponse save(UserDtoRequest userDtoRequest) {
        if (userRepository.existsByEmail(userDtoRequest.getEmail())
                || userRepository.existsByPhone(userDtoRequest.getPhone())) {
            throw new CreationException("User with the same email/phone already exists");
        }
        UserEntity userEntity = userMapper.toEntity(userDtoRequest);
        userRepository.save(userEntity);
        return userMapper.toDto(userEntity);
    }

    public UserDtoResponse updateUser(long id, UserDtoRequest userDtoRequest) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User with id %d not found", id)));

        if (userDtoRequest.getEmail() != null) {
            userEntity.setEmail(userDtoRequest.getEmail());
        }
        if (userDtoRequest.getPhone() != null) {
            userEntity.setPhone(userDtoRequest.getPhone());
        }
        if (userDtoRequest.getLastName() != null) {
            userEntity.setLastName(userDtoRequest.getLastName());
        }
        if (userDtoRequest.getFirstName() != null) {
            userEntity.setFirstName(userDtoRequest.getFirstName());
        }
        if (userDtoRequest.getTasks() != null && !userDtoRequest.getTasks().isEmpty()) {
            userEntity.getTasks().addAll(userMapper.toEntity(userDtoRequest).getTasks());
            userEntity.getTasks().forEach(taskEntity -> taskEntity.setUser(userEntity));
        }
        try {
            userRepository.save(userEntity);
            return userMapper.toDto(userEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new UpdateException("Error updating user with id " + id);
        }
    }

    public void deleteUserById(long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User with id %d not found.", id)));
        userEntity.getGroups().forEach(groupEntity -> groupEntity.getUsers().remove(userEntity));

        userRepository.deleteById(id);
    }

}
