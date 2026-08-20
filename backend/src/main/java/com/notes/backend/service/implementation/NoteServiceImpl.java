package com.notes.backend.service.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.notes.backend.dto.NoteRequestDto;
import com.notes.backend.dto.NoteResponseDto;
import com.notes.backend.entity.Note;
import com.notes.backend.exception.DuplicateResourceException;
import com.notes.backend.exception.ResourceNotFoundException; 
import com.notes.backend.mapper.NoteMapper;
import com.notes.backend.repository.NoteRepository;
import com.notes.backend.service.NoteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    
    private final NoteRepository noteRepository;

    @Override
    public NoteResponseDto createNote(NoteRequestDto noteRequestDto) {
        validateNoteRequestDto(noteRequestDto);

        log.info("Attempting to create new note by author: {}", noteRequestDto.getAuthor());

        if (noteRepository.existsByTitleIgnoreCase(noteRequestDto.getTitle())) {
            log.warn("Failed to create note. Title already exists: {}", noteRequestDto.getTitle());
            throw new DuplicateResourceException("A note with this title already exists.");
        }

        Note note = NoteMapper.mapToEntity(noteRequestDto);
        Note savedNote = noteRepository.save(note);

        log.info("Successfully created note with ID: {}", savedNote.getId());

        return NoteMapper.mapToResponseDto(savedNote);
    }

    @Override
    public NoteResponseDto getNoteById(Long id) {
        return NoteMapper.mapToResponseDto(noteRepository.findById(id).orElseThrow(() -> {
            log.error("Note with ID {} not found", id);
            return new ResourceNotFoundException("Note not found with ID: " + id); 
        }));
    }

    @Override
    public NoteResponseDto updateNote(Long id, NoteRequestDto noteRequestDto) {
        validateNoteRequestDto(noteRequestDto);

        Note existingNote = noteRepository.findById(id).orElseThrow(() -> {
            log.error("Note with ID {} not found for update", id);
            return new ResourceNotFoundException("Note not found with ID: " + id); 
        });
        
        if (!existingNote.getTitle().equalsIgnoreCase(noteRequestDto.getTitle()) &&
                noteRepository.existsByTitleIgnoreCase(noteRequestDto.getTitle())) {
            throw new DuplicateResourceException("Another note with this title already exists.");
        }
        
        NoteMapper.updateEntityFromDto(noteRequestDto, existingNote);
        Note updatedNote = noteRepository.save(existingNote);
        return NoteMapper.mapToResponseDto(updatedNote);
    }

    @Override
    public void deleteNote(Long id) {
        Note existingNote = noteRepository.findById(id).orElseThrow(() -> {
            log.error("Note with ID {} not found for deletion", id);
            return new ResourceNotFoundException("Note not found with ID: " + id);
        });

        noteRepository.delete(existingNote);
        log.info("Successfully deleted note with ID: {}", id);
    }

    @Override
    public List<NoteResponseDto> getAllNotes() {
        List<Note> notes = noteRepository.findAll();
        return notes.stream()
                .map(NoteMapper::mapToResponseDto)
                .toList();
    }

    @Override
    public List<NoteResponseDto> getNotesByAuthor(String author) {
        List<Note> notes = noteRepository.findByAuthorIgnoreCase(author);
        return notes.stream()
                .map(NoteMapper::mapToResponseDto)
                .toList();
    }

    @Override
    public NoteResponseDto getNoteByTitle(String title) {
        Note note = noteRepository.findByTitleIgnoreCase(title);
        if (note == null) {
            log.error("Note with title '{}' not found", title);
            throw new ResourceNotFoundException("Note not found with title: " + title); 
        }
        return NoteMapper.mapToResponseDto(note);
    }

    @Override
    public List<NoteResponseDto> getNotesByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            log.error("Start date or end date is null");
            throw new IllegalArgumentException("Date range parameters cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            log.error("Start date {} is after end date {}", startDate, endDate);
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        log.info("Fetching notes created between {} and {}", startDate, endDate);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Note> notes = noteRepository.findNotesByDateRange(startDateTime, endDateTime);
        return notes.stream()
                .map(NoteMapper::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoteResponseDto> getNotesByKeywordInContent(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            log.error("Keyword for content search is null or empty");
            throw new IllegalArgumentException("Keyword cannot be null or empty");
        }
        List<Note> notes = noteRepository.findByContentContainingIgnoreCase(keyword);
        return notes.stream()
                .map(NoteMapper::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoteResponseDto> getNotesByKeywordInTitle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            log.error("Keyword for title search is null or empty");
            throw new IllegalArgumentException("Keyword cannot be null or empty");
        }
        List<Note> notes = noteRepository.findByTitleContainingIgnoreCase(keyword);
        return notes.stream()
                .map(NoteMapper::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public void validateNoteRequestDto(NoteRequestDto noteRequestDto) {
        if (noteRequestDto == null) {
            log.error("Note request data is null");
            throw new IllegalArgumentException("Note request data cannot be null");
        }
        if (noteRequestDto.getTitle() == null || noteRequestDto.getTitle().trim().isEmpty()) {
            log.error("Note title is null or empty");
            throw new IllegalArgumentException("Note title cannot be null or empty");
        }
    }
}