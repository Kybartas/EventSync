package org.kybartas.eventsync.repository;

import org.kybartas.eventsync.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
