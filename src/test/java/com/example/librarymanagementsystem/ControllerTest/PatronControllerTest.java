package com.example.librarymanagementsystem.ControllerTest;

import com.example.librarymanagementsystem.Controller.PatronController;
import com.example.librarymanagementsystem.DTO.PatronDTO;
import com.example.librarymanagementsystem.Service.PatronService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PatronControllerTest {

    @Mock
    private PatronService patronService;

    @InjectMocks
    private PatronController patronController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(patronController).build();
    }

    @Test
    public void getAllPatrons_shouldReturnListOfPatrons() throws Exception {
        when(patronService.findAllPatrons()).thenReturn(Collections.singletonList(new PatronDTO()));

        mockMvc.perform(get("/api/patrons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(patronService, times(1)).findAllPatrons();
        verifyNoMoreInteractions(patronService);
    }

    @Test
    public void getPatronById_existingId_shouldReturnPatron() throws Exception {
        Long patronId = 1L;
        when(patronService.findPatronById(patronId)).thenReturn(java.util.Optional.of(new PatronDTO()));

        mockMvc.perform(get("/api/patrons/{id}", patronId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty());

        verify(patronService, times(1)).findPatronById(patronId);
        verifyNoMoreInteractions(patronService);
    }

    @Test
    public void getPatronById_nonExistingId_shouldReturnNotFound() throws Exception {
        Long patronId = 1L;
        when(patronService.findPatronById(patronId)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/patrons/{id}", patronId))
                .andExpect(status().isNotFound());

        verify(patronService, times(1)).findPatronById(patronId);
        verifyNoMoreInteractions(patronService);
    }

    @Test
    public void addPatron_validPatron_shouldReturnCreated() throws Exception {
        PatronDTO patronDTO = new PatronDTO();

        when(patronService.addPatron(any(PatronDTO.class))).thenReturn(patronDTO);

        mockMvc.perform(post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty());

        verify(patronService, times(1)).addPatron(any(PatronDTO.class));
        verifyNoMoreInteractions(patronService);
    }

    @Test
    public void updatePatron_existingId_shouldReturnUpdatedPatron() throws Exception {
        Long patronId = 1L;
        PatronDTO patronDTO = new PatronDTO();

        when(patronService.updatePatron(eq(patronId), any(PatronDTO.class))).thenReturn(java.util.Optional.of(patronDTO));

        mockMvc.perform(put("/api/patrons/{id}", patronId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty());

        verify(patronService, times(1)).updatePatron(eq(patronId), any(PatronDTO.class));
        verifyNoMoreInteractions(patronService);
    }

    @Test
    public void updatePatron_nonExistingId_shouldReturnNotFound() throws Exception {
        Long patronId = 1L;

        when(patronService.updatePatron(eq(patronId), any(PatronDTO.class))).thenReturn(java.util.Optional.empty());

        mockMvc.perform(put("/api/patrons/{id}", patronId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());

        verify(patronService, times(1)).updatePatron(eq(patronId), any(PatronDTO.class));
        verifyNoMoreInteractions(patronService);
    }

    @Test
    public void deletePatron_existingId_shouldReturnOk() throws Exception {
        Long patronId = 1L;

        when(patronService.deletePatron(patronId)).thenReturn(true);

        mockMvc.perform(delete("/api/patrons/{id}", patronId))
                .andExpect(status().isOk());

        verify(patronService, times(1)).deletePatron(patronId);
        verifyNoMoreInteractions(patronService);
    }

    @Test
    public void deletePatron_nonExistingId_shouldReturnNotFound() throws Exception {
        Long patronId = 1L;

        when(patronService.deletePatron(patronId)).thenReturn(false);

        mockMvc.perform(delete("/api/patrons/{id}", patronId))
                .andExpect(status().isNotFound());

        verify(patronService, times(1)).deletePatron(patronId);
        verifyNoMoreInteractions(patronService);
    }
}
