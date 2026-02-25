package com.itis.oris.di;

import com.itis.oris.di.config.PathScan;

import java.util.List;

public class TestPathScan {
    public static void main(String[] args) {

        List<Class<?>> classes = PathScan.find("com.itis.oris.di");
        classes.forEach(System.out::println);
    }
}