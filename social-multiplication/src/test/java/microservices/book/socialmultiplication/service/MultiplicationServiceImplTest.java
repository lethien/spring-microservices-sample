package microservices.book.socialmultiplication.service;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import microservices.book.socialmultiplication.domain.Multiplication;
import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;
import microservices.book.socialmultiplication.domain.User;
import microservices.book.socialmultiplication.event.EventDispatcher;
import microservices.book.socialmultiplication.event.MultiplicationSolvedEvent;
import microservices.book.socialmultiplication.repository.MultiplicationRepository;
import microservices.book.socialmultiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.socialmultiplication.repository.UserRepository;

public class MultiplicationServiceImplTest {

	private MultiplicationServiceImpl multiplicationServiceImpl;
	
	@Mock
	private RandomGeneratorService randomGeneratorService;
	
	@Mock
	private MultiplicationResultAttemptRepository attemptRepo;
	
	@Mock
	private UserRepository userRepo;
	
	@Mock
	private MultiplicationRepository multiplicationRepo;
	
	@Mock
	private EventDispatcher eventDispatcher;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		multiplicationServiceImpl = new MultiplicationServiceImpl(
				randomGeneratorService, attemptRepo, 
				userRepo, multiplicationRepo, eventDispatcher
				);
	}
	
	@Test
	public void createRandomMultiplicationTest() {
		// given that random generator service will return first 50, then 30
		BDDMockito.given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);
		
		// when
		Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();
		
		// then
		Assertions.assertThat(multiplication.getFactorA()).isEqualTo(50);
		Assertions.assertThat(multiplication.getFactorB()).isEqualTo(30);
	}
	
	@Test
	public void checkCorrectAttemptTest() {
		// given
		Multiplication multiplication = new Multiplication(50, 60);
		User user = new User("john doe");
		MultiplicationResultAttempt multiplicationResultAttempt = new MultiplicationResultAttempt(user, multiplication, 3000, false);
		MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(user, multiplication, 3000, true);
		BDDMockito.given(userRepo.findByAlias("john doe")).willReturn(Optional.empty());
		BDDMockito.given(multiplicationRepo.findByFactors(50, 60)).willReturn(Optional.empty());
		
		MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(multiplicationResultAttempt.getId(), multiplicationResultAttempt.getUser().getId(), true);
		
		// when 
		boolean attemptResult = multiplicationServiceImpl.checkAttempt(multiplicationResultAttempt);
		
		// assert
		Assertions.assertThat(attemptResult).isTrue();
		Mockito.verify(attemptRepo).save(verifiedAttempt);
		Mockito.verify(eventDispatcher).send(ArgumentMatchers.eq(event));
	}
	
	@Test
	public void checkWrongAttemptTest() {
		// given
		Multiplication multiplication = new Multiplication(50, 60);
		User user = new User("john doe");
		MultiplicationResultAttempt multiplicationResultAttempt = new MultiplicationResultAttempt(user, multiplication, 3010, false);
		BDDMockito.given(userRepo.findByAlias("john doe")).willReturn(Optional.empty());
		BDDMockito.given(multiplicationRepo.findByFactors(50, 60)).willReturn(Optional.empty());

		MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(multiplicationResultAttempt.getId(), multiplicationResultAttempt.getUser().getId(), false);
		
		// when 
		boolean attemptResult = multiplicationServiceImpl.checkAttempt(multiplicationResultAttempt);
		
		// assert
		Assertions.assertThat(attemptResult).isFalse();
		Mockito.verify(attemptRepo).save(multiplicationResultAttempt);
		Mockito.verify(eventDispatcher).send(ArgumentMatchers.eq(event));
	}
	
	@Test
	public void retrieveStatsTest() {
		// given
		Multiplication multiplication = new Multiplication(50, 60);
		User user = new User("john doe");
		MultiplicationResultAttempt resultAttempt1 = new MultiplicationResultAttempt(user, multiplication, 3010, false);
		MultiplicationResultAttempt resultAttempt2 = new MultiplicationResultAttempt(user, multiplication, 3056, false);
		List<MultiplicationResultAttempt> attempts = Lists.newArrayList(resultAttempt1, resultAttempt2);
		BDDMockito.given(userRepo.findByAlias("john doe")).willReturn(Optional.empty());
		BDDMockito.given(attemptRepo.findTop5ByUserAliasOrderByIdDesc("john doe")).willReturn(attempts);
		
		// when
		List<MultiplicationResultAttempt> latestAttempts = multiplicationServiceImpl.getStatsForUser("john doe");
		
		// then 
		Assertions.assertThat(latestAttempts).isEqualTo(attempts);
	}
}
