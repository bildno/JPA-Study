package com.study.jpa.chap05.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.jpa.chap05.entity.Group;
import com.study.jpa.chap05.entity.Idol;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.study.jpa.chap05.entity.QIdol.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Rollback(false)
class QueryDSLBasicTest {
    @Autowired
    IdolRepository idolRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    JPAQueryFactory factory;
    @Autowired
    EntityManager em;
    @Test
    void setUp() {
        //given
        Group leSserafim = new Group("르세라핌");
        Group ive = new Group("아이브");
        groupRepository.save(leSserafim);
        groupRepository.save(ive);
        Idol idol1 = new Idol("김채원", 24, "female", leSserafim);
        Idol idol2 = new Idol("사쿠라", 26, "female", leSserafim);
        Idol idol3 = new Idol("가을", 22, "female", ive);
        Idol idol4 = new Idol("리즈", 20, "female", ive);
        idolRepository.save(idol1);
        idolRepository.save(idol2);
        idolRepository.save(idol3);
        idolRepository.save(idol4);
    }
    @Test
    @DisplayName("JPQL로 특정이름의 아이돌 조회하기")
    void jpqlTest() {
        //given
        String jpqlQuery = "SELECT i FROM Idol i WHERE i.idolName = ?1";
        //when
        Idol foundIdol = em.createQuery(jpqlQuery, Idol.class)
                .setParameter(1, "가을")
                .getSingleResult();
        //then
        assertEquals("아이브", foundIdol.getGroup().getGroupName());
        System.out.println("\n\n\n\n");
        System.out.println("foundIdol = " + foundIdol);
        System.out.println("foundIdol.getGroup() = " + foundIdol.getGroup());
        System.out.println("\n\n\n\n");
    }
    @Test
    @DisplayName("쿼리디에셀로 특정 이름의 아이돌 조회")
    void queryDslTest() {
        // given
        // when
        Idol foundIdol = factory
                .select(idol)
                .from(idol)
                .where(idol.idolName.eq("사쿠라"))
                .fetchOne();
        // then
        System.out.println("\n\n\n\n");
        System.out.println("foundIdol = " + foundIdol);
        System.out.println("foundIdol.getGroup() = " + foundIdol.getGroup());
        System.out.println("\n\n\n\n");

        
        //        idol.idolName.eq("리즈") // idolName = '리즈'
        //        idol.idolName.ne("리즈") // idolName != '리즈'
        //        idol.idolName.eq("리즈").not() // idolName != '리즈'
        //        idol.idolName.isNotNull() //이름이 is not null
        //        idol.age.in(10, 20) // age in (10,20)
        //        idol.age.notIn(10, 20) // age not in (10, 20)
        //        idol.age.between(10,30) //between 10, 30
        //        idol.age.goe(30) // age >= 30
        //        idol.age.gt(30) // age > 30
        //        idol.age.loe(30) // age <= 30
        //        idol.age.lt(30) // age < 30
        //        idol.idolName.like("_김%")  // like _김%
        //        idol.idolName.contains("김") // like %김%
        //        idol.idolName.startsWith("김") // like 김%
        //        idol.idolName.endsWith("김") // like %김


    }
    
    
    @Test
    @DisplayName("조회결과 가져오기")
    void fetchTest() {

        // 리스트 조회 (fetch)
        List<Idol> idolList = factory
                .select(idol)
                .from(idol)
                .fetch();
        System.out.println("\n\n==============fetch================");
        idolList.forEach(System.out::println);


        Idol foundIdol = factory
                .select(idol)
                .from(idol)
                .where(idol.age.lt(21))
                .fetchOne();

        System.out.println("\n\n==============fetchOne================");
        System.out.println("foundIdol = " + foundIdol);


        Optional<Idol> foundIdolOptional = Optional.ofNullable(
                factory
                        .select(idol)
                        .from(idol)
                        .where(idol.age.lt(21))
                        .fetchOne()
        );

        Idol foundIdol2 = foundIdolOptional.orElseThrow();

        System.out.println("\n\n==============fetchOne (Optional)================");
        System.out.println("foundIdol2 = " + foundIdol2);


        // 이름에 김 들어가면 다 조회
        List<Idol> kimList = factory.select(idol)
                .from(idol)
                .where(idol.idolName.like("%김%"))
                .fetch();

        System.out.println("\n\n==============fetch================");
        kimList.forEach(System.out::println);

        // 나이가 20 ~ 25세인 아이돌 중 맨처음 아이돌 조회
        Idol ageIdol = factory.select(idol)
                .from(idol)
                .where(idol.age.between(20,25))
                .fetchFirst();
        // fetchOne : 단일 건 조회, 여러 건 조회 시 예외
        // fetchFirst : 여러 건 중 먼 처음 값 반환
        // fetch : List 형태로 반환


        System.out.println("\n\n==============fetch================");
        System.out.println("ageIdol = " + ageIdol);
    }
    
    
}

