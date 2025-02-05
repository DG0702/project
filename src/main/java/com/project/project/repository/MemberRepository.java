package com.project.project.repository;

import com.project.project.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Override
    ArrayList<Member> findAll();

    // findByUserIdAndPassword 는 userId와 password가 일치하는 Member 객체를 찾는 JPA 메서드입니다.
    // _(메서드 이름에 언더바 사용시 오류발생) >> user_no,user_id >> 사용불가
    // Optional<Member> 대신 Member 사용 가능하지만 Optional<Member>를 더 선호 >> 예외처리 오류를 방지 가능 하기 때문

    // 아이디, 비밀번호 조회
    Optional<Member> findByUserIdAndPassword(String userId, String password);

    // 이름, 전화번호 조회
    Optional<Member> findByUserNameAndPhoneNumber(String userName, String phoneNumber);

    // 이름, 아이디 조회
    Optional<Member> findByUserNameAndUserId(String userName, String userId);


    // 회원가입시 userId 조회 중복검사
    boolean existsByUserId(String userId);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByNickName(String nickName);

    // 회원정보 조회
    Optional<Member> findByUserId (String userId);


}
