package com.study.jpa.chap04.repository;

import com.study.jpa.chap04.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("select d from Department d join fetch d.employees")
    List<Department> findAllIncludeEmployees();

    
}


