package com.catpudding.android_back_stage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.catpudding.android_back_stage.*.mapper")
public class AndroidBackStageApplication {

    public static void main(String[] args) {
        SpringApplication.run(AndroidBackStageApplication.class, args);
    }

}
