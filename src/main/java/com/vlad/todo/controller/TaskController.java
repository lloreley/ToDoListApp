package com.vlad.todo.controller;


import com.vlad.todo.model.Task;
import com.vlad.todo.service.TaskService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> tasksByFilter(@RequestParam(required = false) Boolean completed) {
        if (completed != null) {
            return taskService.findAllTasks().stream()
                    .filter(todo -> todo.getCompleted() == completed)
                    .toList();
        }
        return taskService.findAllTasks();
    }


    @PostMapping("saveTask")
    public Task saveTask(@RequestBody Task task) {
        return taskService.saveTask(task);
    }

    @GetMapping("/{id}")
    public Task findTaskById(@PathVariable int id) {
        return taskService.findTaskById(id);
    }

    @DeleteMapping("deleteTask/{id}")
    public void deleteTaskById(@PathVariable int id) {
        taskService.deleteTaskById(id);
    }

    @PutMapping("updateTask")
    public Task updateTaskById(Task task) {
        return taskService.updateTask(task);
    }

}
