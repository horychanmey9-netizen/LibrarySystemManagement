package com.example.LibraryBack.repository;

import com.example.LibraryBack.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowerRepository extends JpaRepository<Borrower,Long> {

}
