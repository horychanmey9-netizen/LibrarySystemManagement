package com.example.LibraryBack.repository;

import com.example.LibraryBack.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
