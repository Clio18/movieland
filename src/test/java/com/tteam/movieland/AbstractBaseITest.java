package com.tteam.movieland;

import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
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

import static com.vladmihalcea.sql.SQLStatementCountValidator.reset;

@SpringBootTest
@Testcontainers
@DirtiesContext
@ActiveProfiles("test")
public class AbstractBaseITest {
	@Container
	private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");

	@DynamicPropertySource
	public static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", container::getJdbcUrl);
		registry.add("spring.datasource.username", container::getUsername);
		registry.add("spring.datasource.password", container::getPassword);
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

	@BeforeEach
	void init(){
		reset();
	}

}