package com.example.LibraryBack.repositoy;

import com.example.LibraryBack.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FineRepository extends JpaRepository<Fine,Long> {
    
}
