package com.study.jpa.chap04.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString(exclude = "employees")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name ="tbl_dept")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private Long id;

    @Column(name = "dept_name", nullable = false)
    private String name;
    
    // 양 방향 매핑에서는 실제 테이블의 List가 세팅되지 않음
    // 엔터티 안에서만 사용하는 가상의 컬럼
    // 상대방 엔터티의 갱신에 관여할 수 없음 단순의 읽기 전용(조회)으로만 사용하는 것을 권장

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;

}
