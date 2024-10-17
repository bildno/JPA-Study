package com.study.jpa.chap01.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Getter @Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_product")
public class Product {

    @Id // 기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    @Column(name = "prod_id") //테이블 이름
    private Long id;

    @Column(name = "prod_nm", length = 30, nullable = false)
    private String name;

    @Column(name = "prod_price")
    private int price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // ENUM 타입 테이블 설정
    private Category category;

    @CreationTimestamp // insert시에 서버시간으로 저장
    @Column(updatable = false) // 수정 불가
    private LocalDateTime createdAt;
    
    @UpdateTimestamp // update문 실행 시 시간 저장
    private  LocalDateTime updatedAt;

    @Transient // 데이터베이스에는 저장 x 클래스 내부에서만 사용할 데이터
    private  String nickname;

    public enum Category {
        FOOD, FASHION, ELECTRONIC
    }


}
