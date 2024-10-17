package com.study.jpa.chap01.entity.repository;

import com.study.jpa.chap01.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class ProductRepositoryTest {


    @Autowired
    EntityManager em;

    @Test
    @DisplayName("상품을 데이터베이스에 저장한다.")
    void saveTest() {
        // given
        Product p = Product.builder()
                .name("신발")
                .price(90000)
                .category(Product.Category.FASHION)
                .build();

        // when
        em.persist(p);
        // then
    }


    @Test
    @DisplayName("상품을 조회하기")
    void selectTest() {
        // given
        long prod_id = 1;
        // when
        Product product = em.find(Product.class, prod_id);
        // then
        assertEquals("신발", product.getName());
    }


    @Test
    @DisplayName("영속성 컨텍스트의 1차 캐시 사용")
    void persistTest() {
        // given
        Product p = Product.builder()
                .name("짜장면")
                .price(8000)
                .category(Product.Category.FOOD)
                .build();
        // when
        em.persist(p);
        Product findProd = em.find(Product.class, 2L);
        // then
        assertEquals(8000, findProd.getPrice());

    }


    @Test
    @DisplayName("상품의 가격을 수정")
    void updateTest() {
        // given
        Product shoes = em.find(Product.class, 1L);

        // when
        shoes.setPrice(50000);
        shoes.setName("할인하는 신발");

        em.persist(shoes);
        // then
        assertEquals(50000, em.find(Product.class, 1L).getPrice());
    }
}