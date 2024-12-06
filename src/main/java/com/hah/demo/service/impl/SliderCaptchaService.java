package com.hah.demo.service.impl;

import cn.hutool.core.img.ImgUtil;
import com.hah.demo.Solution;
import com.hah.demo.service.ISliderCaptchaService;
import com.hah.demo.utils.DrawCaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

@Service
public class SliderCaptchaService implements ISliderCaptchaService {

    private Map<Long, Integer> idX = new HashMap<>();

    @Override
    public Map<String, Object> getSliderImage() {
        String imageFilePath = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
        File bgImageFile = new File(imageFilePath + "/images/");
        File slideImageFile = new File(imageFilePath + "/slide/slide1.png");// 若要无边框选择slide.png
        if (bgImageFile.isDirectory()) {
            File[] files = bgImageFile.listFiles();
            Random random = new Random();
            int i = random.nextInt(files.length);
            bgImageFile = files[i];
        }
        BufferedImage read = ImgUtil.read(bgImageFile);
        int width = read.getWidth();
        int height = read.getHeight();
        int[] point = DrawCaptchaUtil.randomAnchorPoint(width, height);
        long id = System.currentTimeMillis();
        idX.put(id, point[0]);
        String bgImageBase64 = DrawCaptchaUtil.getBgImageBase64(bgImageFile, slideImageFile, point, width, height);
        String slideImageBase64 = DrawCaptchaUtil.getSlideImageBase64(bgImageFile, slideImageFile, point, width, height);
        Map<String, Object> images = new HashMap<>();
        images.put("id", id);
        images.put("bgImage", bgImageBase64);
        images.put("slideImage", slideImageBase64);
        images.put("y", point[1]);
        return images;
    }

    @Override
    public boolean verifyCode(Solution solution) {
        try {
            Long startTime = solution.getId();
            // 校验时间
            long dif = solution.getEndTime() - startTime;
            if (dif > 10000L) {// 只允许拖10s
                return false;
            }
            // 校验最后落点
            Integer offset = idX.get(startTime);
            // 获取offset后立即删除，防止重复验证
            Integer padding = offset - solution.getX();
            if (Math.abs(padding) > 5) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
