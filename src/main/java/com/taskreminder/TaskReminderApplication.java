package com.taskreminder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages={"com.taskreminder"})
@EnableJpaRepositories(basePackages="com.taskreminder.repository")
@EntityScan(basePackages="com.taskreminder.entities")
public class TaskReminderApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskReminderApplication.class, args);
	}

}
