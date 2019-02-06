package microservices.book.socialmultiplication.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import microservices.book.socialmultiplication.domain.Multiplication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MultiplicationServiceTest {
	@MockBean
	private RandomGeneratorService randomGeneratorService;
	
	@Autowired
	private MultiplicationService multiplicationService;
	
	@Test
	public void createRandomMultiplicationTest() {
		// given that random generator service will return first 50, then 30
		BDDMockito.given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);
		
		// when
		Multiplication multiplication = multiplicationService.createRandomMultiplication();
		
		// then
		Assertions.assertThat(multiplication.getFactorA()).isEqualTo(50);
		Assertions.assertThat(multiplication.getFactorB()).isEqualTo(30);
	}
}
