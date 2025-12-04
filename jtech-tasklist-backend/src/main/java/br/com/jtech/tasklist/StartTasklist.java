package br.com.jtech.tasklist;

import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.dto.task.TaskOutput;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StartTasklist {

	public static void main(String[] args) {
		SpringApplication.run(StartTasklist.class, args);
	}

}
