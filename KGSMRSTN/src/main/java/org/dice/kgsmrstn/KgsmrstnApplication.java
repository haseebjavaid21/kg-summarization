package org.dice.kgsmrstn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages="org.dice.kgsmrstn.*")
public class KgsmrstnApplication {

	public static void main(String[] args) {
		SpringApplication.run(KgsmrstnApplication.class, args);
	}

}
