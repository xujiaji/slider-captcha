package com.hah.demo.service;

import com.hah.demo.Solution;

import java.util.List;
import java.util.Map;

public interface ISliderCaptchaService {

    Map<String, Object> getSliderImage();

    boolean verifyCode(Solution solution);
}
