package com.example.librarymanagementsystem.Repository;

import com.example.librarymanagementsystem.Entity.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord,Long> {
}
