package com.study.jpa.chap01.entity.repository;

import com.study.jpa.chap01.entity.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static com.study.jpa.chap01.entity.Product.Category.FASHION;
import static com.study.jpa.chap01.entity.Product.Category.FOOD;
import static com.study.jpa.chap01.entity.Product.Category.ELECTRONIC;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class ProductRepositoryTest2 {

    @Autowired
    ProductRepository productRepository;


    @BeforeEach
    void insertBeforeTest() {
        Product p1 = Product.builder()
                .name("아이폰")
                .category(ELECTRONIC)
                .price(2000000)
                .build();
        Product p2 = Product.builder()
                .name("탕수육")
                .category(FOOD)
                .price(20000)
                .build();
        Product p3 = Product.builder()
                .name("구두")
                .category(FASHION)
                .price(300000)
                .build();
        Product p4 = Product.builder()
                .name("주먹밥")
                .category(FOOD)
                .price(1500)
                .build();
        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);
        productRepository.save(p4);

    }

    @Test
    @DisplayName("상품을 데이터베이스에 저장")
    void save() {
        // given
        Product p = Product.builder()
                .name("떡볶이")
                .build();
        // when
        // insert 후에 저장된 데이터의 객체를 반환
        Product saved = productRepository.save(p);
        // then
        assertNotNull(saved);

    }

    @Test
    @DisplayName("1번 상품을 삭제")
    void deleteTest() {
        // given
        long id = 1L;
        // when
        productRepository.deleteByName("탕수육");
        /*
            Optional: Java 8버전 이후에 사용이 가능
            객체의 null값을 검증할 수 있도록 여러가지 기능을 제공하는 타입. (NPE 방지)
         */
        Optional<Product> foundProduct = productRepository.findByName("탕수육");
        boolean present = foundProduct.isPresent(); // 객체가 실존하는지 논리값으로 리턴
        System.out.println("present = " + present);

        Product product = foundProduct.orElse(new Product()); // 만약 값이 없다면 other를 반환

        /*

        Product product1 = foundProduct.orElseThrow(() -> {
            throw new IllegalArgumentException("값이 없따");
        });// 값이 존재하면 원래대로 리턴하고, 값이 없의면 예외발생

        */
        
        foundProduct.ifPresent(product2 -> {
            System.out.println("product2 = " + product2);
        });// 값이 존재한다면


        // then
        assertFalse(present);

    }

    @Test
    @DisplayName("상품 전체 조회 하면 3개여야댐")
    void selectAllTest() {
        // given
        List<Product> all = productRepository.findAll();
        // when
        
        // then
        assertEquals(3, all.size());
    }
    
    @Test
    @DisplayName("2번 상품의 가격을 변경해야댐")
    void updateTest() {
        // given
        long id = 2L;
        String newName = "마라탕";
        int newPrice = 10000;
        // when
        productRepository.findById(id).ifPresent(p -> {
            p.setName(newName);
            p.setPrice(newPrice);

            // jpa는 따로 update메서드 제공 x
            // 조회한 객체의 필드를 setter로 변경 -> 자동으로 update

            productRepository.save(p);
        });

        // then
    }

}