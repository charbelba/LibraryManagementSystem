package com.example.librarymanagementsystem.Controller;
import com.example.librarymanagementsystem.DTO.PatronDTO;
import com.example.librarymanagementsystem.Service.PatronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {

    @Autowired
    private PatronService patronService;

    @GetMapping
    public ResponseEntity<List<PatronDTO>> getAllPatrons() {
        return ResponseEntity.ok(patronService.findAllPatrons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatronDTO> getPatronById(@PathVariable Long id) {
        return patronService.findPatronById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PatronDTO> addPatron(@RequestBody PatronDTO patronDTO) {
        PatronDTO savedPatron = patronService.addPatron(patronDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatron);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatronDTO> updatePatron(@PathVariable Long id, @RequestBody PatronDTO patronDTO) {
        return patronService.updatePatron(id, patronDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) {
        if (patronService.deletePatron(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
