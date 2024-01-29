package com.example.librarymanagementsystem.Repository;

import com.example.librarymanagementsystem.Entity.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronRepository extends JpaRepository<Patron,Long> {
}
