package com.example.socar.admin;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
/*
* @Entity: JPA 엔티티임을 나타냅니다.
@Id: 기본 키를 지정합니다.
@GeneratedValue(strategy = GenerationType.IDENTITY): 기본 키의 값을 자동으로 증가시키도록 설정합니다.
@Data: Lombok 애노테이션으로, getter, setter, toString, equals, hashCode 메서드를 자동으로 생성합니다.
@NoArgsConstructor: 기본 생성자를 생성합니다.
@AllArgsConstructor: 모든 필드를 인자로 받는 생성자를 생성합니다.
* */
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String admin_id;

    private String password;

    private String nickname;
}
