package com.mka.demos.localexplorer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LocalexplorerApplicationTests {

	@Test
	void testApplicationCreation() {
		LocalexplorerApplication application = new LocalexplorerApplication();
		Assertions.assertThat(application).isNotNull();
	}

	@Test
	void testMain() {
		try (MockedStatic<SpringApplication> mockedStatic =
					 Mockito.mockStatic(SpringApplication.class)) {
			LocalexplorerApplication.main(new String[] {});
			mockedStatic.verify(
					() -> SpringApplication.run(Mockito.any(Class.class), Mockito.any(String[].class)));
		}
	}

}
