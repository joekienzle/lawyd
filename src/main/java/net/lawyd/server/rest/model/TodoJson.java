package net.lawyd.server.rest.model;

import com.google.common.base.Strings;
import net.lawyd.server.persistence.Todo;

public class TodoJson {

    private String id;

    private String name;

    private String description;

    private Integer priority;

    public TodoJson() {

    }

    public TodoJson(String id, String name, String description, Integer priority) {
        if (!Strings.isNullOrEmpty(id)) {
            this.id = id;
        }
        if (!Strings.isNullOrEmpty(name)) {
            this.name = name;
        }
        if (!Strings.isNullOrEmpty(description)) {
            this.description = description;
        }
        if (priority != null) {
            this.priority = priority;
        }
    }

    public TodoJson(Todo source) {
        this(source.getId(), source.getName(), source.getDescription(), source.getPriority());
    }

    public Todo convertToDbEntity() {
        Todo todoTransient = new Todo();
        if (!Strings.isNullOrEmpty(id)) {
            todoTransient.setId(id);
        }
        if (!Strings.isNullOrEmpty(name)) {
            todoTransient.setName(name);
        }
        if (!Strings.isNullOrEmpty(description)) {
            todoTransient.setDescription(description);
        }
        if (priority != null) {
            todoTransient.setPriority(priority);
        }
        return todoTransient;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
