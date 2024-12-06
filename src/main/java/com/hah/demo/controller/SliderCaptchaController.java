package com.hah.demo.controller;

import com.hah.demo.Solution;
import com.hah.demo.service.ISliderCaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@Controller
public class SliderCaptchaController {

    @Autowired
    private ISliderCaptchaService sliderCaptchaService;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/getSliderCaptcha")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSliderCaptcha() {
        return ResponseEntity.ok(sliderCaptchaService.getSliderImage());
    }

    @PostMapping("/verifyCode")
    @ResponseBody
    public boolean verifyCode(@RequestBody Solution solution) {
        return sliderCaptchaService.verifyCode(solution);
    }
}