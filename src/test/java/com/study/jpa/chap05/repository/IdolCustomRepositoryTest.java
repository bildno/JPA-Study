package com.study.jpa.chap05.repository;

import com.study.jpa.chap05.entity.Idol;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // jpa 쓸때는 얘 무조건 있어야댐
class IdolCustomRepositoryTest {


    @Autowired
    IdolCustomRepository customRepository;
    
    
    @Test
    @DisplayName("커스텀 레파지토리에 선언한 메서드 호출")
    void testCustomCall() {
        // given
        List<Idol> sorted = customRepository.findAllSortedByName();
        List<Idol> ive = customRepository.findByGroupName("아이브").orElseThrow();
        // when
        sorted.forEach(System.out::println);
        System.out.println("\n\n\n\n");
        ive.forEach(System.out::println);
        
        // then
    }
    

}