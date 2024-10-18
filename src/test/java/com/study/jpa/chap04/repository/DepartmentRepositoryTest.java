package com.study.jpa.chap04.repository;

import com.study.jpa.chap04.entity.Department;
import com.study.jpa.chap04.entity.Employee;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class DepartmentRepositoryTest {


    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Test
    @DisplayName("부서 정보 조회하면 해당 부서원들도 함꼐 조회되어야한다")
    void findDept() {
        // given
        Long id = 1L;
        // when
        Department department = departmentRepository.findById(id).orElseThrow();
        // then
        System.out.println("\n\n\n\n\n\n\n");
        System.out.println("department = " + department);
        System.out.println("department.getEmployees() = " + department.getEmployees());
        System.out.println("\n\n\n\n\n\n\n");


    }


    @Test
    @DisplayName(".")
    void testMethod() {
        // 3번 사원을 조회할거임 부서정보는 필요 없음
        // given
        Long id = 3L;
        // when
        Employee employee = employeeRepository.findById(id).orElseThrow();
        // then
        System.out.println("\n\n\n\n\n");
        System.out.println("employee = " + employee);
        System.out.println("\n\n\n\n\n");

    }

}