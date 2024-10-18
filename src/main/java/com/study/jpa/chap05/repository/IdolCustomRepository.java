package com.study.jpa.chap05.repository;

import com.study.jpa.chap05.entity.Idol;

import java.util.List;
import java.util.Optional;

// 쿼리DSL 레파지토리로 사용 (JPA 상속 X)
public interface IdolCustomRepository {

    // 이름으로 오름차해서 전체 조회(쿼리 메서드 x)
    List<Idol> findAllSortedByName();

    // 그룹명으로 아이돌 조회
    Optional<List<Idol>> findByGroupName(String groupName);


}
