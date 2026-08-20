package com.notes.backend.service;

import java.time.LocalDate;
import java.util.List;

import com.notes.backend.dto.NoteRequestDto;
import com.notes.backend.dto.NoteResponseDto;

public interface NoteService {
    NoteResponseDto createNote(NoteRequestDto noteRequestDto);
    NoteResponseDto getNoteById(Long id);
    NoteResponseDto updateNote(Long id, NoteRequestDto noteRequestDto);
    void deleteNote(Long id);
    List<NoteResponseDto> getAllNotes();
    List<NoteResponseDto> getNotesByAuthor(String author);
    NoteResponseDto getNoteByTitle(String title);
    List<NoteResponseDto> getNotesByDateRange(LocalDate startDate, LocalDate endDate);
    List<NoteResponseDto> getNotesByKeywordInContent(String keyword);
    List<NoteResponseDto> getNotesByKeywordInTitle(String keyword);
}
