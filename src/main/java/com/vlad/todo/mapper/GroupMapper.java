package com.vlad.todo.mapper;

import com.vlad.todo.dto.*;
import com.vlad.todo.model.GroupEntity;
import com.vlad.todo.model.UserEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GroupMapper {
    private final UserMapper userMapper;

    public GroupEntity toEntity(GroupDtoRequest groupDtoRequest) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName(groupDtoRequest.getName());
        groupEntity.setDescription(groupDtoRequest.getDescription());
        return groupEntity;
    }

    public GroupDtoResponse toDto(GroupEntity groupEntity) {
        GroupDtoResponse groupDtoResponse = new GroupDtoResponse();
        groupDtoResponse.setName(groupEntity.getName());
        groupDtoResponse.setDescription(groupEntity.getDescription());
        groupDtoResponse.setId(groupEntity.getId());

        List<UserDtoResponse> usersDtoResponse = new ArrayList<>();
        for (UserEntity userEntity : groupEntity.getUsers()) {
            usersDtoResponse.add(userMapper.toDto(userEntity));
        }
        groupDtoResponse.setUsers(usersDtoResponse);
        return groupDtoResponse;
    }
}
