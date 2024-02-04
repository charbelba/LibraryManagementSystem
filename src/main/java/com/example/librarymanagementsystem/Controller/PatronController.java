package com.example.librarymanagementsystem.Controller;

import com.example.librarymanagementsystem.DTO.PatronDTO;
import com.example.librarymanagementsystem.Service.PatronService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrons")
@Tag(name = "Patron Controller", description = "The Patron API provides operations for managing library patrons.")
public class PatronController {

    @Autowired
    private PatronService patronService;

    @Operation(summary = "Get all patrons", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatronDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<PatronDTO>> getAllPatrons() {
        return ResponseEntity.ok(patronService.findAllPatrons());
    }

    @Operation(summary = "Get a patron by ID", responses = {
            @ApiResponse(description = "Patron found",
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatronDTO.class))),
            @ApiResponse(description = "Patron not found", responseCode = "404")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PatronDTO> getPatronById(@PathVariable @NotNull Long id) {
        return patronService.findPatronById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Add a new patron", responses = {
            @ApiResponse(description = "Patron created", responseCode = "201",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatronDTO.class)))
    })
    @PostMapping
    public ResponseEntity<PatronDTO> addPatron(@Validated @RequestBody @Valid PatronDTO patronDTO) {
        PatronDTO savedPatron = patronService.addPatron(patronDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatron);
    }

    @Operation(summary = "Update an existing patron", responses = {
            @ApiResponse(description = "Patron updated", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatronDTO.class))),
            @ApiResponse(description = "Patron not found", responseCode = "404")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PatronDTO> updatePatron(@PathVariable @NotNull Long id, @Valid @RequestBody PatronDTO patronDTO) {
        return patronService.updatePatron(id, patronDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a patron", responses = {
            @ApiResponse(description = "Patron deleted", responseCode = "200"),
            @ApiResponse(description = "Patron not found", responseCode = "404")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable @NotNull Long id) {
        if (patronService.deletePatron(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
