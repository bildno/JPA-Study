package com.study.jpa.chap02.repository;

import com.study.jpa.chap02.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {

    List<Student> findByName(String name);

    List<Student> findByCityAndMajor(String city, String major);

    //where major like %major%
    List<Student> findByMajorContaining(String major);


    //where major like major%
    List<Student> findByMajorStartingWith(String major);


    //where major like %major
    List<Student> findByMajorEndingWith(String major);

    //where age <= ?
    //List<Student> findByAgeLessThanEqual(int age);
    //where age <= ?
    //List<Student> findByAgeGreaterThanEqual(int age);
    //where age < ?
    //List<Student> findByAgeGreaterThan(int age);

    // 이름만 조회
    //List<Student> findNameByAgeGreaterThan(int age);

    @Query(value = "SELECT * FROM tbl_student WHERE stu_name = :nm OR city = :city", nativeQuery = true)
    List<Student> getStudentByNameOrCity(@Param("nm") String name, @Param("city") String city);

    // native-sql
    // SELECT 컬럼명 FROM 테이블명
    // WHERE 컬럼 = ?

    // JPQL
    // SELECT 별칭 FROM 엔터티클래스명 AS 별칭
    // WHERE 별칭.필드명 = ?

    @Query(value = "SELECT st from Student st WHERE st.name = ?1 OR st.city = ?2" )
    List<Student> getStudentByNameOrCity2(String name, String city);


    // 특정 이름이 포함된 학생 리스트 조회하기
    @Query("select stu from Student stu where stu.name Like %?1%")
    List<Student> searchByNameWithJPQL(String name);

    // 도시명으로 학생 1명을 단일 조회
    @Query("select stu from Student stu where stu.city = ?1 ")
    Optional<Student> getByCityWithJPQL(String city);

    @Modifying // 수정은 얘 붙혀줘야댐
    @Query("delete from Student stu where stu.name = ?1 and stu.city = ?2")
    void deleteByNameAndCityWithJPQL(String name, String city);



}
