package com.study.jpa.chap05.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.jpa.chap05.dto.GroupAverageAgeDto;
import com.study.jpa.chap05.entity.Group;
import com.study.jpa.chap05.entity.Idol;
import com.study.jpa.chap05.entity.QIdol;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static com.study.jpa.chap05.entity.QIdol.idol;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // jpa 쓸때는 얘 무조건 있어야댐
@Rollback(false)
class IdolCustomRepositoryTest {


    @Autowired
    IdolCustomRepository customRepository;
    
    @Autowired
    IdolRepository idolRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    JPAQueryFactory factory;



    //@BeforeEach
    @Test
    void setUp() {
        //given
        Group leSserafim = new Group("르세라핌");
        Group ive = new Group("아이브");
        Group bts = new Group("방탄소년단");
        Group newjeans = new Group("뉴진스");
        groupRepository.save(leSserafim);
        groupRepository.save(ive);
        groupRepository.save(bts);
        groupRepository.save(newjeans);
        Idol idol1 = new Idol("김채원", 24, "여", leSserafim);
        Idol idol2 = new Idol("사쿠라", 26, "여", leSserafim);
        Idol idol3 = new Idol("가을", 22, "여", ive);
        Idol idol4 = new Idol("리즈", 20, "여", ive);
        Idol idol5 = new Idol("장원영", 20, "여", ive);
        Idol idol6 = new Idol("안유진", 21, "여", ive);
        Idol idol7 = new Idol("카즈하", 21, "여", leSserafim);
        Idol idol8 = new Idol("RM", 29, "남", bts);
        Idol idol9 = new Idol("정국", 26, "남", bts);
        Idol idol10 = new Idol("해린", 18, "여", newjeans);
        Idol idol11 = new Idol("혜인", 16, "여", newjeans);
        idolRepository.save(idol1);
        idolRepository.save(idol2);
        idolRepository.save(idol3);
        idolRepository.save(idol4);
        idolRepository.save(idol5);
        idolRepository.save(idol6);
        idolRepository.save(idol7);
        idolRepository.save(idol8);
        idolRepository.save(idol9);
        idolRepository.save(idol10);
        idolRepository.save(idol11);
    }


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

    @Test
    @DisplayName("페이징 처리 하기")
    void pagingTest() {
        // given
        int pageNo = 1;
        int amount = 2;
        // when
        List<Idol> pageIdols = factory
                .select(idol)
                .from(idol)
                .orderBy(idol.age.desc())
                .offset((pageNo - 1) * amount)
                .limit(amount)
                .fetch();
        // 총데이터 수
        Long totalCount = Optional.ofNullable(
                factory
                        .select(idol.count())
                        .from(idol)
                        .fetchOne()).orElse(0L);

        // then
        pageIdols.forEach(System.out::println);

        System.out.println("totalCount = " + totalCount);


    }

    @Test
    @DisplayName("성별별, 그룹별로 그룹화해 아이돌의 숫자가 3명 이하인 그룹만 조회")
    void groupByGenderTest() {
        // given
        // when
        List<Tuple> tuple = factory
                .select(idol.group,idol.gender, idol.count())
                .from(idol)
                .groupBy(idol.gender, idol.group)
                .fetch();
        // then
        for (Tuple tuple1 : tuple) {
            Group group = tuple1.get(idol.group);
            String gender = tuple1.get(idol.gender);
            Long count = tuple1.get(idol.count());

            System.out.println("group = " + group.getGroupName());
            System.out.println("gender = " + gender);
            System.out.println("count = " + count);

            System.out.println("=============================");
        }
    }

    @Test
    @DisplayName("그룹별 평균 나이 조회")
    void group() {
        /*
            SELECT G.group_name, AVG(I.age)
            FROM tbl_idol I
            JOIN tbl_group G
            ON I.group_id = G.group_id
            GROUP BY G.group_id
            HAVING AVG(I.age) BETWEEN 20 AND 25
         */
        List<Tuple> result = factory
                .select(idol.group.groupName, idol.age.avg())
                .from(idol)
                .groupBy(idol.group)
                .having(idol.age.avg().between(20, 25))
                .fetch();

        for (Tuple tuple : result) {
            String groupName = tuple.get(idol.group.groupName);
            Double averageAge = tuple.get(idol.age.avg());

            System.out.println("groupName = " + groupName);
            System.out.println("averageAge = " + averageAge);
            System.out.println("\n\n");

        }

        // when

        // then
    }


    @Test
    @DisplayName("그룹별 평균 나이 조회(dto 처리)")
    void groupdto() {
        /*
            SELECT G.group_name, AVG(I.age)
            FROM tbl_idol I
            JOIN tbl_group G
            ON I.group_id = G.group_id
            GROUP BY G.group_id
            HAVING AVG(I.age) BETWEEN 20 AND 25
         */
        
        // Projections : 커스텀 dto를 포장해주는 객체
        List<GroupAverageAgeDto> result = factory
                .select(
                        Projections.constructor(
                                GroupAverageAgeDto.class,
                                idol.group.groupName,
                                idol.age.avg()
                        )
                )
                .from(idol)
                .groupBy(idol.group)
                .having(idol.age.avg().between(20, 25))
                .fetch();

        for (GroupAverageAgeDto dto : result) {
            String groupName = dto.getGroupName();
            Double averageAge = dto.getAverageAge();

            System.out.println("groupName = " + groupName);
            System.out.println("averageAge = " + averageAge);
            System.out.println("\n\n");

        }

        // when

        // then
    }
}