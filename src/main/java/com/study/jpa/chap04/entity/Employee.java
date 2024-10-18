package com.study.jpa.chap04.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
// jpa 연관관계 맵핑에서 연관관계 필드는 toString에서 제외. 안하면 순환참조 발생
@ToString(exclude = "department") // toString에서 제외할 필드 여러개면 {department, id} 이러케
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name ="tbl_emp")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Long id;

    @Column(nullable = false, name = "emp_name")
    private String name;

    // fetch = FetchType.EAGER -> 무조건 데이터를 가져옴 
    // fetch = FetchType.LAZY -> 필요한 경우에만 데이터를 가져옴 -> 얘만 씀, 어지간하면 얘로 바꾸셈
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Department department;

    
    // 연관관계 편의 메서드 (양방향에서 연관 필드가 수정될 경우
    //                       실제 테이블과의 데이터를 맞춰주기 위해 메서드 선언)
    public void changeDepartment(Department department) {
        this.department = department;
        department.getEmployees().add(this);
    }
}
