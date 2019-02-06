package microservices.book.socialmultiplication.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RandomGeneratorServiceTest {

	@Autowired
	private RandomGeneratorService randomGeneratorService;
	
	@Test
	public void generateRandomFactorBetweenLimits() {
		List<Integer> randomFactors = IntStream.range(0, 1000)
				.map(i -> randomGeneratorService.generateRandomFactor())
				.boxed().collect(Collectors.toList());
		
		Assertions.assertThat(randomFactors)
		.containsOnlyElementsOf(IntStream.range(11, 100).boxed().collect(Collectors.toList()));
	}
}
