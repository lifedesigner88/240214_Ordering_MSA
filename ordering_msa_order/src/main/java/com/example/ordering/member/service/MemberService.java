package com.example.ordering.member.service;

import com.example.ordering.member.domain.Member;
import com.example.ordering.member.dto.LoginReqDto;
import com.example.ordering.member.dto.MemberCreateReqDto;
import com.example.ordering.member.dto.MemberResponseDto;
import com.example.ordering.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepo;
    private final PasswordEncoder passEnco;

    @Autowired
    public MemberService(MemberRepository memberRepo,
                         PasswordEncoder passEnco) {
        this.memberRepo = memberRepo;
        this.passEnco = passEnco;
    }

    public MemberResponseDto findMyInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepo.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        return MemberResponseDto.toMemberResponseDto(member);
    }

    public List<MemberResponseDto> findAll(){
        List<Member> members = memberRepo.findAll();
        return members.stream()
                .map(MemberResponseDto::toMemberResponseDto)
                .collect(Collectors.toList());
    }

    public Member create(MemberCreateReqDto member) {
        Optional<Member> mailCheck = memberRepo.findByEmail(member.getEmail());
        if(mailCheck.isPresent()) throw new IllegalArgumentException("중복이메일 입니다.");
        return memberRepo.save (
                Member.toEntity(member, passEnco.encode(member.getPassword())));
    }

    public Member login(LoginReqDto reqDto) throws IllegalArgumentException {
        Member member = memberRepo.findByEmail(reqDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
        if (!passEnco.matches(reqDto.getPassword(), member.getPassword()))
            throw new IllegalArgumentException("비밀번호 불일치");
        return member;
    }



}
