package com.project.project.repository;

import com.project.project.entity.Member;
import com.project.project.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    // UserRole 필드 (member, role) -> role 안에 roleName 가지고오기
    boolean existsByMemberAndRole_RoleName(Member member, String roleName);

    // 회원 탈퇴 시
    void deleteByMember_UserNo(Long userNo);
}
