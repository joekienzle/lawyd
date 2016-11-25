package net.lawyd.server.service;

import com.google.common.collect.Lists;
import net.lawyd.server.persistence.Todo;
import net.lawyd.server.persistence.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Transactional
    public Todo saveTodo(Todo todoTransient) {
        return todoRepository.save(todoTransient);
    }

    public Todo findTodoById(String todoId) {
        return todoRepository.findOne(todoId);
    }

    public List<Todo> findAllTodos() {
        return Lists.newArrayList(todoRepository.findAll());
    }

    public void deleteTodoById(String todoId) {
        todoRepository.delete(todoId);
    }
}
