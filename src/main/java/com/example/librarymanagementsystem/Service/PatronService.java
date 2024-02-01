package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.DTO.PatronDTO;
import com.example.librarymanagementsystem.Entity.Patron;
import com.example.librarymanagementsystem.Exception.PatronNotFoundException;
import com.example.librarymanagementsystem.Repository.PatronRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class PatronService {

    @Autowired
    private PatronRepository patronRepository;

    @Transactional(readOnly = true)
    @Cacheable("patrons")
    public List<PatronDTO> findAllPatrons() {
        log.info("Fetching all patrons");
        return patronRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "patrons", key = "#id")
    public Optional<PatronDTO> findPatronById(Long id) {
        log.info("Fetching patron with id: {}", id);
        return patronRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    @CachePut(value = "patrons", key = "#result.id")
    public PatronDTO addPatron(PatronDTO patronDTO) {
        Patron patron = convertToEntity(patronDTO);
        log.info("Adding new patron: {}", patronDTO);
        Patron savedPatron = patronRepository.save(patron);
        return convertToDTO(savedPatron);
    }

    @Transactional
    @CachePut(value = "patrons", key = "#id")
    public Optional<PatronDTO> updatePatron(Long id, PatronDTO patronDTO) {
        log.info("Updating patron with id: {}", id);
        return Optional.ofNullable(patronRepository.findById(id).map(patron -> {
            patron.setName(patronDTO.getName());
            patron.setContactInformation(patronDTO.getContactInformation());
            return convertToDTO(patronRepository.save(patron));
        }).orElseThrow(() -> new PatronNotFoundException("Patron not found with id: " + id)));
    }

    @Transactional
    @CacheEvict(value = "patrons", key = "#id")
    public boolean deletePatron(Long id) {
        log.info("Deleting patron with id: {}", id);
        if (patronRepository.existsById(id)) {
            patronRepository.deleteById(id);
            return true;
        } else {
            throw new PatronNotFoundException("Patron not found with id: " + id);
        }
    }

    private PatronDTO convertToDTO(Patron patron) {
        return new PatronDTO(patron.getId(), patron.getName(), patron.getContactInformation());
    }

    private Patron convertToEntity(PatronDTO dto) {
        return new Patron(dto.getId(), dto.getName(), dto.getContactInformation());
    }
}
