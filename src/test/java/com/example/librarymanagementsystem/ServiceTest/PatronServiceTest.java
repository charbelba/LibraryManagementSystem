package com.example.librarymanagementsystem.ServiceTest;

import com.example.librarymanagementsystem.DTO.PatronDTO;
import com.example.librarymanagementsystem.Entity.Patron;
import com.example.librarymanagementsystem.Exception.PatronNotFoundException;
import com.example.librarymanagementsystem.Repository.PatronRepository;
import com.example.librarymanagementsystem.Service.PatronService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PatronServiceTest {

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private PatronService patronService;

    @Test
    public void findAllPatrons_noPatronsInDatabase_shouldReturnEmptyList() {
        when(patronRepository.findAll()).thenReturn(new ArrayList<>());

        List<PatronDTO> result = patronService.findAllPatrons();

        assertTrue(result.isEmpty());
        verify(patronRepository, times(1)).findAll();
        verifyNoMoreInteractions(patronRepository);
    }

    @Test
    public void findAllPatrons_patronsInDatabase_shouldReturnList() {
        List<Patron> patronsInDatabase = new ArrayList<>();
        patronsInDatabase.add(new Patron(1L, "John Doe", "john.doe@example.com"));
        patronsInDatabase.add(new Patron(2L, "Jane Smith", "jane.smith@example.com"));
        when(patronRepository.findAll()).thenReturn(patronsInDatabase);

        List<PatronDTO> result = patronService.findAllPatrons();

        assertEquals(patronsInDatabase.size(), result.size());
        verify(patronRepository, times(1)).findAll();
        verifyNoMoreInteractions(patronRepository);
    }

    @Test
    public void findPatronById_existingPatron_shouldReturnPatronDTO() {
        Long patronId = 1L;
        Patron patron = new Patron(patronId, "John Doe", "john.doe@example.com");
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));

        Optional<PatronDTO> result = patronService.findPatronById(patronId);

        assertTrue(result.isPresent());
        assertEquals(patron.getId(), result.get().getId());
        verify(patronRepository, times(1)).findById(patronId);
        verifyNoMoreInteractions(patronRepository);
    }

    @Test
    public void findPatronById_nonExistingPatron_shouldThrowException() {
        Long patronId = 1L;
        when(patronRepository.findById(patronId)).thenReturn(Optional.empty());

        assertThrows(PatronNotFoundException.class, () -> patronService.findPatronById(patronId));
        verify(patronRepository, times(1)).findById(patronId);
        verifyNoMoreInteractions(patronRepository);
    }

    @Test
    public void addPatron_validPatronDTO_shouldReturnPatronDTO() {
        PatronDTO patronDTO = new PatronDTO(null, "John Doe", "john.doe@example.com");
        Patron savedPatron = new Patron(1L, "John Doe", "john.doe@example.com");
        when(patronRepository.save(any(Patron.class))).thenReturn(savedPatron);

        PatronDTO result = patronService.addPatron(patronDTO);

        assertNotNull(result.getId());
        assertEquals(patronDTO.getName(), result.getName());
        assertEquals(patronDTO.getContactInformation(), result.getContactInformation());
        verify(patronRepository, times(1)).save(any(Patron.class));
        verifyNoMoreInteractions(patronRepository);
    }

    @Test
    public void updatePatron_existingPatron_shouldReturnUpdatedPatronDTO() {
        Long patronId = 1L;
        PatronDTO updatedPatronDTO = new PatronDTO(patronId, "Updated Name", "updated.email@example.com");
        Patron existingPatron = new Patron(patronId, "Original Name", "original.email@example.com");
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(existingPatron));
        when(patronRepository.save(any(Patron.class))).thenReturn(existingPatron);

        Optional<PatronDTO> result = patronService.updatePatron(patronId, updatedPatronDTO);

        assertTrue(result.isPresent());
        assertEquals(updatedPatronDTO.getId(), result.get().getId());
        assertEquals(updatedPatronDTO.getName(), result.get().getName());
        assertEquals(updatedPatronDTO.getContactInformation(), result.get().getContactInformation());
        verify(patronRepository, times(1)).findById(patronId);
        verify(patronRepository, times(1)).save(any(Patron.class));
        verifyNoMoreInteractions(patronRepository);
    }

    @Test
    public void updatePatron_nonExistingPatron_shouldThrowException() {
        Long patronId = 1L;
        PatronDTO updatedPatronDTO = new PatronDTO(patronId, "Updated Name", "updated.email@example.com");
        when(patronRepository.findById(patronId)).thenReturn(Optional.empty());

        assertThrows(PatronNotFoundException.class, () -> patronService.updatePatron(patronId, updatedPatronDTO));
        verify(patronRepository, times(1)).findById(patronId);
        verifyNoMoreInteractions(patronRepository);
    }

    @Test
    public void deletePatron_existingPatron_shouldReturnTrue() {
        Long patronId = 1L;
        when(patronRepository.existsById(patronId)).thenReturn(true);

        boolean result = patronService.deletePatron(patronId);

        assertTrue(result);
        verify(patronRepository, times(1)).existsById(patronId);
        verify(patronRepository, times(1)).deleteById(patronId);
        verifyNoMoreInteractions(patronRepository);
    }

    @Test
    public void deletePatron_nonExistingPatron_shouldThrowException() {
        Long patronId = 1L;
        when(patronRepository.existsById(patronId)).thenReturn(false);

        assertThrows(PatronNotFoundException.class, () -> patronService.deletePatron(patronId));
        verify(patronRepository, times(1)).existsById(patronId);
        verifyNoMoreInteractions(patronRepository);
    }

}
