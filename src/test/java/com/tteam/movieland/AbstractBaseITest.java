package com.tteam.movieland;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@SpringBootTest
@Testcontainers
public class AbstractBaseITest {

	@Container
	private static final PostgreSQLContainer<?> container;

	static {
		container = new PostgreSQLContainer<>("postgres:latest");
		container.start();
	}

	@DynamicPropertySource
	public static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", container::getJdbcUrl);
		registry.add("spring.datasource.username", () -> "test");
		registry.add("spring.datasource.password", () -> "test");
	}

	protected String getResponseAsString(String jsonPath) {
		URL resource = getClass().getClassLoader().getResource(jsonPath);
		try {
			File file = new File(Objects.requireNonNull(resource).toURI());
			return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException("Unable to find file: " + jsonPath);
		}
	}


}
