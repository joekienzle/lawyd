package net.lawyd.server.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, String> {
    // Nothing to do here
}
