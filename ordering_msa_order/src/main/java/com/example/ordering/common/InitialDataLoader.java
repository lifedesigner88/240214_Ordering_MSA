package com.example.ordering.common;

import com.example.ordering.member.domain.Member;
import com.example.ordering.member.domain.Role;
import com.example.ordering.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements CommandLineRunner {
//    CommandLineRunner를 통해 스프링 빈으로 등록되는 시점에 run 메서드 실행

    private final MemberRepository memberRepo;
    private final PasswordEncoder passEnco;

    @Autowired
    public InitialDataLoader(MemberRepository memberRepo, PasswordEncoder passEnco) {
        this.memberRepo = memberRepo;
        this.passEnco = passEnco;
    }

    @Override
    public void run(String... args) throws Exception {
        if(memberRepo.findByEmail("root@admin").isEmpty()){
            Member admin =  Member.builder()
                    .name("root")
                    .email("root@admin")
                    .password(passEnco.encode("1234"))
                    .role(Role.ADMIN)
                    .build();
            memberRepo.save(admin);
        }
    }
}
