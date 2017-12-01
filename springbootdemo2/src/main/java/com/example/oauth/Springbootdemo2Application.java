package com.example.oauth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.example.oauth.dao")
public class Springbootdemo2Application {

	public static void main(String[] args) {

		SpringApplication.run(Springbootdemo2Application.class, args);
	}
}
