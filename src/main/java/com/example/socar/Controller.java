package com.example.socar;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

///*
//
// 요약
//Controller (쿠키 가게): 웹 페이지를 만들어서 화면에 보여주는 역할.
//RestController (케이크 배달 가게): 필요한 데이터를 직접 전달해주는 역할. 화면은 보여주지 않아요.
// */
@RestController
public class Controller {
    @GetMapping("/")
    @ResponseBody
    String start(){
        return "안녕하세요 피싱사이트임 ㅅㄱ";
    }
}
