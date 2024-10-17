package com.study.jpa.chap01.entity.repository;

import com.study.jpa.chap01.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// jpaRepository를 상속한 후 첫번째 제네릭에 엔터티클래스 타입
// 두번째에 pk의 타입을 작성
public interface ProductRepository extends JpaRepository<Product, Long> {

    void deleteByName(String mame);
    Optional<Product> findByName(String name);

}
