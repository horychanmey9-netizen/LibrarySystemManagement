package com.example.LibraryBack.repositoy;

import com.example.LibraryBack.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
