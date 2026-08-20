package com.notes.backend.service;

import com.notes.backend.dto.NoteRequestDto;
import com.notes.backend.dto.NoteResponseDto;
import com.notes.backend.entity.Note;
import com.notes.backend.exception.DuplicateResourceException;
import com.notes.backend.exception.ResourceNotFoundException;
import com.notes.backend.repository.NoteRepository;
import com.notes.backend.service.implementation.NoteServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceImplTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteServiceImpl noteService;

    private NoteRequestDto validRequestDto;
    private Note existingNote;

    @BeforeEach
    void setUp() {
        validRequestDto = new NoteRequestDto("Test Title", "Test Content", "Author");
        
        existingNote = new Note();
        existingNote.setId(1L);
        existingNote.setTitle("Test Title");
        existingNote.setContent("Test Content");
        existingNote.setAuthor("Author");
    }

    // ==========================================
    // CREATE NOTE
    // ==========================================

    @Test
    void createNote_Success() {
        when(noteRepository.existsByTitleIgnoreCase("Test Title")).thenReturn(false);
        when(noteRepository.save(any(Note.class))).thenReturn(existingNote);

        NoteResponseDto result = noteService.createNote(validRequestDto);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    void createNote_ThrowsDuplicateResourceException_WhenTitleExists() {
        when(noteRepository.existsByTitleIgnoreCase("Test Title")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> noteService.createNote(validRequestDto));
    }

    @Test
    void createNote_ThrowsIllegalArgumentException_WhenDtoIsNull() {
        assertThrows(IllegalArgumentException.class, () -> noteService.createNote(null));
    }

    @Test
    void createNote_ThrowsIllegalArgumentException_WhenTitleIsNull() {
        validRequestDto.setTitle(null);
        assertThrows(IllegalArgumentException.class, () -> noteService.createNote(validRequestDto));
    }

    @Test
    void createNote_ThrowsIllegalArgumentException_WhenTitleIsBlank() {
        validRequestDto.setTitle("   ");
        assertThrows(IllegalArgumentException.class, () -> noteService.createNote(validRequestDto));
    }

    // ==========================================
    // GET NOTE BY ID
    // ==========================================

    @Test
    void getNoteById_Success() {
        when(noteRepository.findById(1L)).thenReturn(Optional.of(existingNote));
        NoteResponseDto result = noteService.getNoteById(1L);
        assertEquals("Test Title", result.getTitle());
    }

    @Test
    void getNoteById_ThrowsResourceNotFoundException_WhenNotFound() {
        when(noteRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> noteService.getNoteById(99L));
    }

    // ==========================================
    // UPDATE NOTE
    // ==========================================

    @Test
    void updateNote_Success_SameTitle() {
        when(noteRepository.findById(1L)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(any(Note.class))).thenReturn(existingNote);

        NoteResponseDto result = noteService.updateNote(1L, validRequestDto);

        assertNotNull(result);
        verify(noteRepository, never()).existsByTitleIgnoreCase(anyString()); 
    }

    @Test
    void updateNote_Success_NewTitle() {
        validRequestDto.setTitle("New Title");
        when(noteRepository.findById(1L)).thenReturn(Optional.of(existingNote));
        when(noteRepository.existsByTitleIgnoreCase("New Title")).thenReturn(false);
        
        Note updatedNote = new Note();
        updatedNote.setId(1L);
        updatedNote.setTitle("New Title");
        when(noteRepository.save(any(Note.class))).thenReturn(updatedNote);

        NoteResponseDto result = noteService.updateNote(1L, validRequestDto);
        assertEquals("New Title", result.getTitle());
    }

    @Test
    void updateNote_ThrowsDuplicateResourceException_WhenNewTitleExists() {
        validRequestDto.setTitle("Existing Other Title");
        when(noteRepository.findById(1L)).thenReturn(Optional.of(existingNote));
        when(noteRepository.existsByTitleIgnoreCase("Existing Other Title")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> noteService.updateNote(1L, validRequestDto));
    }

    @Test
    void updateNote_ThrowsResourceNotFoundException_WhenIdDoesNotExist() {
        when(noteRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> noteService.updateNote(99L, validRequestDto));
    }

    // ==========================================
    // DELETE NOTE
    // ==========================================

    @Test
    void deleteNote_Success() {
        when(noteRepository.findById(1L)).thenReturn(Optional.of(existingNote));
        noteService.deleteNote(1L);
        verify(noteRepository, times(1)).delete(existingNote);
    }

    @Test
    void deleteNote_ThrowsResourceNotFoundException_WhenNotFound() {
        when(noteRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> noteService.deleteNote(99L));
        verify(noteRepository, never()).delete(any());
    }

    // ==========================================
    // GET ALL / SEARCH NOTES
    // ==========================================

    @Test
    void getAllNotes_Success() {
        when(noteRepository.findAll()).thenReturn(List.of(existingNote));
        List<NoteResponseDto> result = noteService.getAllNotes();
        assertEquals(1, result.size());
    }

    @Test
    void getNotesByAuthor_Success() {
        when(noteRepository.findByAuthorIgnoreCase("Author")).thenReturn(List.of(existingNote));
        List<NoteResponseDto> result = noteService.getNotesByAuthor("Author");
        assertEquals(1, result.size());
    }

    @Test
    void getNoteByTitle_Success() {
        when(noteRepository.findByTitleIgnoreCase("Test Title")).thenReturn(existingNote);
        NoteResponseDto result = noteService.getNoteByTitle("Test Title");
        assertEquals("Test Title", result.getTitle());
    }

    @Test
    void getNoteByTitle_ThrowsResourceNotFoundException_WhenNotFound() {
        when(noteRepository.findByTitleIgnoreCase("Unknown Title")).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> noteService.getNoteByTitle("Unknown Title"));
    }

    // ==========================================
    // DATE RANGE SEARCH
    // ==========================================

    @Test
    void getNotesByDateRange_Success() {
        LocalDate start = LocalDate.now().minusDays(5);
        LocalDate end = LocalDate.now();
        when(noteRepository.findNotesByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(existingNote));

        List<NoteResponseDto> result = noteService.getNotesByDateRange(start, end);
        assertEquals(1, result.size());
    }

    @Test
    void getNotesByDateRange_ThrowsIllegalArgumentException_WhenDatesAreNull() {
        assertThrows(IllegalArgumentException.class, () -> noteService.getNotesByDateRange(null, LocalDate.now()));
        assertThrows(IllegalArgumentException.class, () -> noteService.getNotesByDateRange(LocalDate.now(), null));
    }

    @Test
    void getNotesByDateRange_ThrowsIllegalArgumentException_WhenStartIsAfterEnd() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.minusDays(1);
        assertThrows(IllegalArgumentException.class, () -> noteService.getNotesByDateRange(start, end));
    }

    // ==========================================
    // KEYWORD SEARCHES
    // ==========================================

    @Test
    void getNotesByKeywordInContent_Success() {
        when(noteRepository.findByContentContainingIgnoreCase("Content")).thenReturn(List.of(existingNote));
        List<NoteResponseDto> result = noteService.getNotesByKeywordInContent("Content");
        assertEquals(1, result.size());
    }

    @Test
    void getNotesByKeywordInContent_ThrowsIllegalArgumentException_WhenKeywordInvalid() {
        assertThrows(IllegalArgumentException.class, () -> noteService.getNotesByKeywordInContent(null));
        assertThrows(IllegalArgumentException.class, () -> noteService.getNotesByKeywordInContent("   "));
    }

    @Test
    void getNotesByKeywordInTitle_Success() {
        when(noteRepository.findByTitleContainingIgnoreCase("Title")).thenReturn(List.of(existingNote));
        List<NoteResponseDto> result = noteService.getNotesByKeywordInTitle("Title");
        assertEquals(1, result.size());
    }

    @Test
    void getNotesByKeywordInTitle_ThrowsIllegalArgumentException_WhenKeywordInvalid() {
        assertThrows(IllegalArgumentException.class, () -> noteService.getNotesByKeywordInTitle(null));
        assertThrows(IllegalArgumentException.class, () -> noteService.getNotesByKeywordInTitle("   "));
    }
}