package com.example.socar.car;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/*
요약
Controller (쿠키 가게): 웹 페이지를 만들어서 화면에 보여주는 역할.
RestController (케이크 배달 가게): 필요한 데이터를 직접 전달해주는 역할. 화면은 보여주지 않아요.
 * @ResponseBody 없이: 요청을 받으면 웹 페이지를 만들어서 보여줌.
 * @ResponseBody 사용: 요청을 받으면 데이터를 그대로 돌려줌.
 * */

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
    public void saveCar(Car car) {
        carRepository.save(car);
    }
}
