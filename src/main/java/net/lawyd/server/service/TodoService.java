package net.lawyd.server.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.lawyd.server.persistence.Todo;
import net.lawyd.server.persistence.TodoRepository;
import net.lawyd.server.rest.model.TodoJson;
import net.lawyd.server.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Transactional
    public Todo createTodo(Todo todoTransient) {
        return todoRepository.save(todoTransient);
    }

    public Todo createTodo(TodoJson todoJson) {
        return createTodo(convertToDB(todoJson));
    }

    @Transactional
    public Todo updateTodo(Todo todoTransient) throws IllegalStateException, NotFoundException {
        if (todoTransient == null || Strings.isNullOrEmpty(todoTransient.getId())) {
            throw new IllegalStateException("No entity or id was passed to update");
        }
        if (!todoRepository.exists(todoTransient.getId())) {
            throw new NotFoundException(String.format("The todo with id '%s' could not be deleted because it was not be found", todoTransient.getId()));
        }
        return todoRepository.save(todoTransient);
    }

    public Todo updateTodo(TodoJson todoJson) {
        return updateTodo(convertToDB(todoJson));
    }

    public Todo findTodoById(String todoId) throws IllegalStateException, NotFoundException {
        if (Strings.isNullOrEmpty(todoId)) {
            throw new IllegalStateException("No id was passed to find");
        }
        if (!todoRepository.exists(todoId)) {
            throw new NotFoundException(String.format("The todo with id '%s' could not be found", todoId));
        }
        return todoRepository.findOne(todoId);
    }

    public List<Todo> findAllTodos() {
        return Lists.newArrayList(todoRepository.findAll());
    }

    public void deleteTodoById(String todoId) throws IllegalStateException, NotFoundException {
        if (Strings.isNullOrEmpty(todoId)) {
            throw new IllegalStateException("No id was passed to delete");
        }
        if (!todoRepository.exists(todoId)) {
            throw new NotFoundException(String.format("The todo with id '%s' could not be deleted because it was not be found", todoId));
        }
        todoRepository.delete(todoId);
    }

    public static Todo convertToDB(TodoJson todoJson) {
        if (todoJson == null) {
            throw new IllegalStateException("No entity was passed");
        }
        return todoJson.convertToDbEntity();
    }

    public static TodoJson convertToJson(Todo todo) {
        return new TodoJson(todo);
    }
}
