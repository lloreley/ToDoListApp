package com.vlad.todo.controller;

import com.vlad.todo.dto.GroupDtoRequest;
import com.vlad.todo.dto.GroupDtoResponse;
import com.vlad.todo.exception.CreationException;
import com.vlad.todo.exception.NotFoundException;
import com.vlad.todo.exception.UpdateException;
import com.vlad.todo.service.GroupService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public List<GroupDtoResponse> getAllGroups() {
        return groupService.findAll();
    }

    @PostMapping("/saveGroup")
    public GroupDtoResponse saveGroup(@RequestBody GroupDtoRequest groupDtoRequest) {
        return groupService.save(groupDtoRequest);
    }

    @PutMapping("/{id}")
    public GroupDtoResponse updateTask(
            @PathVariable long id,
            @RequestBody GroupDtoRequest groupDtoRequest) {
        return groupService.update(id, groupDtoRequest);
    }

    @GetMapping("/{id}")
    public GroupDtoResponse findTaskById(@PathVariable long id) {
        return groupService.findById(id);
    }

    @DeleteMapping("/deleteGroup/{id}")
    public void deleteTaskById(@PathVariable long id) {
        groupService.deleteById(id);
    }

    @ExceptionHandler(CreationException.class)
    public ResponseEntity<String> handleCreateException(CreationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(UpdateException.class)
    public ResponseEntity<String> handleUpdateException(UpdateException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
