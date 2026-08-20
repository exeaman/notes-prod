package com.notes.backend.mapper;

import com.notes.backend.dto.NoteRequestDto;
import com.notes.backend.dto.NoteResponseDto;
import com.notes.backend.entity.Note;

public class NoteMapper {

    public static Note mapToEntity(NoteRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        Note note = new Note();
        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());
        note.setAuthor(dto.getAuthor());
        
        return note;
    }


    public static NoteResponseDto mapToResponseDto(Note entity) {
        if (entity == null) {
            return null;
        }
        
        NoteResponseDto dto = new NoteResponseDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setAuthor(entity.getAuthor());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        return dto;
    }

    public static void updateEntityFromDto(NoteRequestDto dto, Note entity) {
        if (dto == null || entity == null) {
            return;
        }
        
        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        if (dto.getContent() != null) {
            entity.setContent(dto.getContent());
        }
        if (dto.getAuthor() != null) {
            entity.setAuthor(dto.getAuthor());
        }
    }
}