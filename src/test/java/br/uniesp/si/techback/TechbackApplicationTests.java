package br.uniesp.si.techback;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "app.jwt.secret=test-secret-test-secret-test-secret-123456")
class TechbackApplicationTests {

	@Test
	void contextLoads() {
	}

}
