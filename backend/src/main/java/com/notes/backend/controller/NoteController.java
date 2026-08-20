package com.notes.backend.controller;

import com.notes.backend.dto.NoteRequestDto;
import com.notes.backend.dto.NoteResponseDto;
import com.notes.backend.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController implements NoteApi {

    private final NoteService noteService;


    @PostMapping
    public ResponseEntity<NoteResponseDto> createNote(@Valid @RequestBody NoteRequestDto noteRequestDto) {
        NoteResponseDto createdNote = noteService.createNote(noteRequestDto);
        return new ResponseEntity<>(createdNote, HttpStatus.CREATED); 
    }

    @GetMapping
    public ResponseEntity<List<NoteResponseDto>> getAllNotes() {
        return ResponseEntity.ok(noteService.getAllNotes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponseDto> getNoteById(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponseDto> updateNote(
            @PathVariable Long id, 
            @Valid @RequestBody NoteRequestDto noteRequestDto) {
        return ResponseEntity.ok(noteService.updateNote(id, noteRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build(); 
    }


    @GetMapping("/author/{author}")
    public ResponseEntity<List<NoteResponseDto>> getNotesByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(noteService.getNotesByAuthor(author));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<NoteResponseDto> getNoteByExactTitle(@PathVariable String title) {
        return ResponseEntity.ok(noteService.getNoteByTitle(title));
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<NoteResponseDto>> searchNotesByTitle(@RequestParam String keyword) {
        return ResponseEntity.ok(noteService.getNotesByKeywordInTitle(keyword));
    }

    @GetMapping("/search/content")
    public ResponseEntity<List<NoteResponseDto>> searchNotesByContent(@RequestParam String keyword) {
        return ResponseEntity.ok(noteService.getNotesByKeywordInContent(keyword));
    }

    @GetMapping("/search/date")
    public ResponseEntity<List<NoteResponseDto>> getNotesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        return ResponseEntity.ok(noteService.getNotesByDateRange(startDate, endDate));
    }
}