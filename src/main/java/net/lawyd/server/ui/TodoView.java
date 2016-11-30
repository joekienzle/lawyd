package net.lawyd.server.ui;

import com.google.common.base.Strings;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import net.lawyd.server.persistence.Todo;
import net.lawyd.server.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

@SpringView(name = "todos")
public class TodoView extends VerticalLayout implements View {

    @Autowired
    private TodoService todoService;

    private boolean initialized = false;

    private Grid todoGrid;
    private Button addButton;
    private TextField summaryNameField;

    private BeanContainer<String, Todo> todoContainer;
    private Button deleteButton;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (initialized) {
            return;
        }

        initComponents();
        layoutComponents();
        reloadData();
    }

    private void initComponents() {
        todoContainer = new BeanContainer<>(Todo.class);
        todoContainer.setBeanIdProperty("id");
        todoGrid = new Grid("Todos", todoContainer);
        todoGrid.removeAllColumns();
        todoGrid.addColumn("name");
        todoGrid.addColumn("description");
        todoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        todoGrid.addSelectionListener(event -> validateButtons());

        todoGrid.setEditorEnabled(true);

        todoGrid.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                // Nothing to do here
            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                Item itemDataSource = commitEvent.getFieldBinder().getItemDataSource();
                if (itemDataSource instanceof BeanItem) {
                    Object bean = ((BeanItem) itemDataSource).getBean();
                    if (bean instanceof Todo) {
                        todoService.updateTodo((Todo) bean);
                        reloadData();
                    }
                }
            }
        });

        summaryNameField = new TextField();
        addButton = new Button("Hinzu", event -> {
            String summary = summaryNameField.getValue();
            if (!Strings.isNullOrEmpty(summary)) {
                Todo todoTransient = new Todo();
                todoTransient.setName(summary);
                todoService.createTodo(todoTransient);
                reloadData();
                summaryNameField.clear();
            }
        });

        addButton.setIcon(FontAwesome.PLUS);

        ShortcutListener shortcut = new ShortcutListener("Default Key", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                addButton.click();
            }
        };
        summaryNameField.addFocusListener(event -> summaryNameField.addShortcutListener(shortcut));
        summaryNameField.addBlurListener(event -> summaryNameField.removeShortcutListener(shortcut));

        deleteButton = new Button("Löschen", event -> ConfirmDialog.show(UI.getCurrent(), "Löschen bestätigen", "Wollen sie den ausgewählten Todo wirklich löschen?", "Ja", "Nein", confirmDialog -> {
            if (confirmDialog.isConfirmed()) {
                String selectedId = (String) todoGrid.getSelectedRow();
                if (!Strings.isNullOrEmpty(selectedId)) {
                    todoService.deleteTodoById(selectedId);
                    todoGrid.deselect(selectedId);
                    reloadData();
                }
            }
        }));
        deleteButton.setIcon(FontAwesome.MINUS);
        deleteButton.setEnabled(false);
    }

    private void validateButtons() {
        deleteButton.setEnabled(todoGrid.getSelectedRow() != null);
    }

    private void layoutComponents() {
        HorizontalLayout buttonBar = new HorizontalLayout(summaryNameField, addButton, deleteButton);
        buttonBar.setSpacing(true);
        buttonBar.setWidth("100%");
        summaryNameField.setWidth("100%");
        buttonBar.setExpandRatio(summaryNameField, 1.0f);

        todoGrid.setSizeFull();

        addComponent(todoGrid);
        addComponent(buttonBar);
        setExpandRatio(todoGrid, 1.0f);
        setWidth("100%");
        setHeight("100%");
        setMargin(true);
        setSpacing(true);
    }

    private void reloadData() {
        todoContainer.removeAllItems();
        todoContainer.addAll(todoService.findAllTodos());
    }
}
