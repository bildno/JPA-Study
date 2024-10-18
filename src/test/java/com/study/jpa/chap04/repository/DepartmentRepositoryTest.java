package com.study.jpa.chap04.repository;

import com.study.jpa.chap04.entity.Department;
import com.study.jpa.chap04.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback(false)
class DepartmentRepositoryTest {


    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    EntityManager em;

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
    void testLazyAndEager() {
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
    
    
    @Test
    @DisplayName("양 방향 연관관계에서 연관 데이터의 수정")
    void testChangeDept() {
        // 1번 사원의 부서를 1-> 2번 부서로 변경해야댐
        // given
        Employee foundEmp = employeeRepository.findById(1L).orElseThrow();
        Department newDept = departmentRepository.findById(2L).orElseThrow();

        employeeRepository.save(foundEmp);



        /*
        em.flush(); // DB로 밀어내기
        em.clear(); // 영속성 컨텍스트 비우기(비우지 않으면 켄텍스트 내의 정보를 참조하려고 함)
        */
        // when
        // 연관관계 편의 메서드 호출 -> 데이터 수정 시 반대편 엔터티도 꼭 수정을 해주쟈
        foundEmp.changeDepartment(newDept);
        // then
        System.out.println("\n\n\n\n");
        newDept.getEmployees().forEach(System.out::println);
        System.out.println("\n\n\n\n");

    }
    @Test
    @DisplayName("N+1 문제발생 예시")
    void testNPlusOneEx() {
        // given
        List<Department> departments = departmentRepository.findAll();
        // when
        departments.forEach(dept -> {
            System.out.println("======사원 리스트======");
            List<Employee> empList = dept.getEmployees();
            System.out.println(empList);

            System.out.println("\n\n");

        });
        // then
    }


    @Test
    @DisplayName("N+1 문제발생 해결")
    void testNPlusOneSolution() {
        // given
        List<Department> departments = departmentRepository.findAllIncludeEmployees();
        // when
        departments.forEach(dept -> {
            System.out.println("======사원 리스트======");
            List<Employee> empList = dept.getEmployees();
            System.out.println(empList);

            System.out.println("\n\n");

        });
        // then
    }

}