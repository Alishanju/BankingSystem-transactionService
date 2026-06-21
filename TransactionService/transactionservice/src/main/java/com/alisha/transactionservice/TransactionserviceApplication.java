package com.alisha.transactionservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableRabbit

public class TransactionserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionserviceApplication.class, args);
	}

}
