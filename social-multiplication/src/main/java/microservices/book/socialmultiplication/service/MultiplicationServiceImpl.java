package microservices.book.socialmultiplication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import microservices.book.socialmultiplication.domain.Multiplication;
import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;
import microservices.book.socialmultiplication.domain.User;
import microservices.book.socialmultiplication.event.EventDispatcher;
import microservices.book.socialmultiplication.event.MultiplicationSolvedEvent;
import microservices.book.socialmultiplication.repository.MultiplicationRepository;
import microservices.book.socialmultiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.socialmultiplication.repository.UserRepository;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

	private RandomGeneratorService randomGeneratorService;
	
	private MultiplicationResultAttemptRepository attemptRepo;
	private UserRepository userRepo;
	private MultiplicationRepository multiplicationRepo;
	
	private EventDispatcher eventDispatcher;
	
	@Autowired
	public MultiplicationServiceImpl(final RandomGeneratorService randomGeneratorService,
									final MultiplicationResultAttemptRepository attemptRepo,
									final UserRepository userRepo,
									final MultiplicationRepository multiplicationRepo,
									final EventDispatcher eventDispatcher) {
		this.randomGeneratorService = randomGeneratorService;
		this.attemptRepo = attemptRepo;
		this.userRepo = userRepo;
		this.multiplicationRepo = multiplicationRepo;
		this.eventDispatcher = eventDispatcher;
	}
	
	@Override
	public Multiplication createRandomMultiplication() {
		int factorA = randomGeneratorService.generateRandomFactor();
		int factorB = randomGeneratorService.generateRandomFactor();
		
		return new Multiplication(factorA, factorB);
	}
	
	@Transactional
	@Override
	public boolean checkAttempt(final MultiplicationResultAttempt multiplicationResultAttempt) {
		boolean correct = multiplicationResultAttempt.getResultAttempt() ==
				multiplicationResultAttempt.getMultiplication().getFactorA()
				* multiplicationResultAttempt.getMultiplication().getFactorB();
		
		Assert.isTrue(!multiplicationResultAttempt.isCorrect(), "You can't send an attempt marked as correct!!");
		
		Optional<User> user = userRepo.findByAlias(multiplicationResultAttempt.getUser().getAlias());
		Optional<Multiplication> multiplication = multiplicationRepo.findByFactors(
				multiplicationResultAttempt.getMultiplication().getFactorA(), 
				multiplicationResultAttempt.getMultiplication().getFactorB());
		
		MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(
					user.orElse(multiplicationResultAttempt.getUser()), 
					multiplication.orElse(multiplicationResultAttempt.getMultiplication()), 
					multiplicationResultAttempt.getResultAttempt(), 
					correct);
		
		attemptRepo.save(checkedAttempt);
		
		// Dispatch an event
		eventDispatcher.send(new MultiplicationSolvedEvent(checkedAttempt.getId(), 
				checkedAttempt.getUser().getId(), 
				checkedAttempt.isCorrect()));
		
		return correct;
	}

	@Override
	public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) {
		return attemptRepo.findTop5ByUserAliasOrderByIdDesc(userAlias);
	}
	
	@Override
	public Optional<MultiplicationResultAttempt> getResultAttemptById(Long id) {
		return attemptRepo.findById(id);
	}
}
