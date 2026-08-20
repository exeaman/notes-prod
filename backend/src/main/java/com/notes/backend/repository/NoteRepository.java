package com.notes.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.notes.backend.entity.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByAuthorIgnoreCase(String author);

    List<Note> findByTitleContainingIgnoreCase(String keyword);

    Note findByTitle(String title);

    Note findByTitleIgnoreCase(String title);

    boolean existsByTitleIgnoreCase(String title);

    @Query("SELECT n FROM Note n WHERE n.createdAt BETWEEN :startDate AND :endDate")
    List<Note> findNotesByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    List<Note> findByContentContainingIgnoreCase(String keyword);
}