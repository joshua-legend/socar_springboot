package com.example.socar.car;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/*
요약
Controller (쿠키 가게): 웹 페이지를 만들어서 화면에 보여주는 역할.
RestController (케이크 배달 가게): 필요한 데이터를 직접 전달해주는 역할. 화면은 보여주지 않아요.
 * @ResponseBody 없이: 요청을 받으면 웹 페이지를 만들어서 보여줌.
 * @ResponseBody 사용: 요청을 받으면 데이터를 그대로 돌려줌.
 * */

@RestController
@RequestMapping("/api/car")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")

public class CarController {

    @Autowired
    private CarService carService;
    @GetMapping("/all")
    public ResponseEntity<List<Car>> getAllCars() {
        try {
            List<Car> cars = carService.getAllCars();
            return ResponseEntity.ok(cars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/add")
    public ResponseEntity<String> addCar(@RequestBody Car car) {
        try {
            carService.saveCar(car);
            return ResponseEntity.status(HttpStatus.CREATED).body("Car added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding car");
        }
    }

    @PostMapping("/set-cookie")
    public ResponseEntity<String> setCookie(@RequestBody String data, HttpServletResponse response) {
        // 쿠키 생성
        ResponseCookie cookie = ResponseCookie.from("myCookie", "cookieValue")
                .httpOnly(false)
                .secure(false)
                .path("/")
                .maxAge(60 * 60) // 쿠키 유효시간 설정 (초 단위, 여기는 1시간)
                .build();

        // 응답에 쿠키 추가
        response.addHeader("Set-Cookie", cookie.toString());

        // 응답 본문 반환
        return ResponseEntity.ok("Cookie is set");
    }


}
