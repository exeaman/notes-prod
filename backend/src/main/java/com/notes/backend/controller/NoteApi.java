package com.notes.backend.controller;

import com.notes.backend.dto.ErrorResponseDto;
import com.notes.backend.dto.NoteRequestDto;
import com.notes.backend.dto.NoteResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Notes API", description = "CRUD operations and advanced search endpoints for managing Notes")
public interface NoteApi {

    @Operation(summary = "Create a new note", description = "Creates a new note. The title must be unique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Note created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed (e.g., missing title)", 
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Note with this title already exists", 
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<NoteResponseDto> createNote(NoteRequestDto noteRequestDto);


    @Operation(summary = "Get all notes", description = "Retrieves a list of all notes in the database.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of notes")
    ResponseEntity<List<NoteResponseDto>> getAllNotes();


    @Operation(summary = "Get a note by ID", description = "Retrieves a single note by its unique database ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the note"),
            @ApiResponse(responseCode = "404", description = "Note not found", 
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<NoteResponseDto> getNoteById(
            @Parameter(description = "ID of the note to retrieve", example = "1") Long id);


    @Operation(summary = "Update an existing note", description = "Updates a note's title, content, or author by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed for request payload", 
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Note not found", 
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Another note already uses the new title", 
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<NoteResponseDto> updateNote(
            @Parameter(description = "ID of the note to update", example = "1") Long id,
            NoteRequestDto noteRequestDto);


    @Operation(summary = "Delete a note", description = "Deletes a note permanently from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Note deleted successfully (No Content)"),
            @ApiResponse(responseCode = "404", description = "Note not found", 
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<Void> deleteNote(
            @Parameter(description = "ID of the note to delete", example = "1") Long id);


    @Operation(summary = "Get notes by author", description = "Retrieves all notes written by a specific author (case-insensitive).")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the author's notes")
    ResponseEntity<List<NoteResponseDto>> getNotesByAuthor(
            @Parameter(description = "The author's name", example = "John Doe") String author);


    @Operation(summary = "Get note by exact title", description = "Retrieves a single note matching the exact title (case-insensitive).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the note"),
            @ApiResponse(responseCode = "404", description = "Note not found", 
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<NoteResponseDto> getNoteByExactTitle(
            @Parameter(description = "The exact title of the note", example = "My First Note") String title);


    @Operation(summary = "Search notes by title", description = "Retrieves a list of notes where the title contains the provided keyword.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved matching notes"),
            @ApiResponse(responseCode = "400", description = "Keyword is null or empty", 
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<List<NoteResponseDto>> searchNotesByTitle(
            @Parameter(description = "Keyword to search within titles", example = "Spring") String keyword);


    @Operation(summary = "Search notes by content", description = "Retrieves a list of notes where the content body contains the provided keyword.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved matching notes"),
            @ApiResponse(responseCode = "400", description = "Keyword is null or empty", 
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<List<NoteResponseDto>> searchNotesByContent(
            @Parameter(description = "Keyword to search within note content", example = "Java") String keyword);


    @Operation(summary = "Get notes by date range", description = "Retrieves a list of notes created between a specific start and end date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved notes within the date range"),
            @ApiResponse(responseCode = "400", description = "Invalid date range or parameters", 
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<List<NoteResponseDto>> getNotesByDateRange(
            @Parameter(description = "Start date (YYYY-MM-DD)", example = "2026-08-01") LocalDate startDate,
            @Parameter(description = "End date (YYYY-MM-DD)", example = "2026-08-31") LocalDate endDate);
}