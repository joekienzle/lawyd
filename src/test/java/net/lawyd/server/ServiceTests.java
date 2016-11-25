package net.lawyd.server;

import com.google.common.base.Strings;
import net.lawyd.server.persistence.Todo;
import net.lawyd.server.service.TodoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTests {

	@Autowired
	private TodoService todoService;

	@Test
	public void createReadUpdateDeleteTodo() {

		int initalNumberTodods = getNumberOfTodos();

		final String name = "My important Todo";
		final String description = "It is really important to get this done quickly";
		final int priority = 1;
		Todo todoTransient = new Todo();
		todoTransient.setName(name);
		todoTransient.setDescription(description);
		todoTransient.setPriority(priority);
		Todo todoTemp = todoService.saveTodo(todoTransient);

		assertEquals(initalNumberTodods + 1 , getNumberOfTodos());
		assertNotNull(todoTemp);
		String todoId = todoTemp.getId();
		assertFalse(Strings.isNullOrEmpty(todoId));

		Todo todoLoaded = todoService.findTodoById(todoId);

		assertEquals(name, todoLoaded.getName());
		assertEquals(description, todoLoaded.getDescription());
		assertEquals(priority, todoLoaded.getPriority());

		String updatedName = "My updated Todo";
		todoLoaded.setName(updatedName);
		Todo todoUpdated = todoService.saveTodo(todoLoaded);

		assertEquals(initalNumberTodods + 1 , getNumberOfTodos());
		assertNotNull(todoUpdated);
		assertEquals(todoUpdated.getId(), todoId);
		assertEquals(updatedName, todoUpdated.getName());
		assertEquals(description, todoUpdated.getDescription());
		assertEquals(priority, todoUpdated.getPriority());

		todoService.deleteTodoById(todoId);

		assertEquals(initalNumberTodods , getNumberOfTodos());
		assertNull(todoService.findTodoById(todoId));
	}

	private int getNumberOfTodos() {
		return todoService.findAllTodos().size();
	}

}
