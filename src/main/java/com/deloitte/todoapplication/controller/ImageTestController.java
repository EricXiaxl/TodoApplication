package com.deloitte.todoapplication.controller;


import com.deloitte.todoapplication.util.ImageUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

public class ImageTestController {

    @PostMapping(value = "/testCompressImage")
    public @ResponseBody void testCompressImage(){
        File inputFile = new File("test.jpg");
        ImageUtil.jdkCompressImage(inputFile);
//        ImageUtil.openCVCompressImage("test.jpg");
    }
}
