package com.deloitte.todoapplication.controller;

import com.deloitte.todoapplication.util.ImageUtil;

import java.io.File;

public class Test {
    public static void main(String[] args){
        File inputFile = new File("b1.jpg");
        ImageUtil.jdkCompressImage(inputFile);

//        ImageUtil.openCVCompressImage("test.jpg");

//        ImageUtil.blurryDetect("b10.jpg");
    }
}
