package com.example.ordering.ordering.controller;

import com.example.ordering.ordering.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {

    private final String MEMBER_API = "http://member-service/";
    private final RestTemplate restTemplate;

    @Autowired
    public TestController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("test/resttemplate")
    public void restTemplateTest() {
        String url = MEMBER_API + "member/1";
        MemberDto members = restTemplate.getForObject(url, MemberDto.class);
        System.out.println(members);
    }
}
