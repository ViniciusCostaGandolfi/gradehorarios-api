package br.com.gradehorarios.api;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) throws IOException, URISyntaxException {
		SpringApplication.run(ApiApplication.class, args);
		System.out.println("\n\n\n Aplica\u00e7\u00e3o dispon\u00edvel  em: \u001b[34mhttp://localhost:8080/swagger-ui.html\u001b[0m");
	}

}
