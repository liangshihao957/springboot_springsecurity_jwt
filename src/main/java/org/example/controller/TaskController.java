package org.example.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @GetMapping("/listTasks")
    public String listTasks(){
        System.out.println("Get  listTasks");
        return "任务列表";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")//表明此方法只有拥有ADMIN权限才可访问
    public String newTasks(){
        System.out.println("Post   tasks");
        return "创建了一个新的任务";
    }

    @PutMapping("/{taskId}")
    public String updateTasks(@PathVariable("taskId")Integer id){
        System.out.println("Put  taskId");
        return "更新了一下id为:"+id+"的任务";
    }

    @DeleteMapping("/{taskId}")
    public String deleteTasks(@PathVariable("taskId")Integer id){
        System.out.println("Delete   taskId");
        return "删除了id为:"+id+"的任务";
    }
}