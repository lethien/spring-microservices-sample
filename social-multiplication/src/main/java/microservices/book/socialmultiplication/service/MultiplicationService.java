package microservices.book.socialmultiplication.service;

import java.util.List;
import java.util.Optional;

import microservices.book.socialmultiplication.domain.Multiplication;
import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;

public interface MultiplicationService {
	/**
	 * Create a Multiplication object with two random generated factors
	 * between 11 and 99
	 * 
	 * @return a {@link Multiplication} object with two random factors
	 */
	Multiplication createRandomMultiplication();
	
	/**
	 * 
	 * @return true if the attempt matches the result of the multiplication, false otherwise
	 */
	boolean checkAttempt(final MultiplicationResultAttempt multiplicationResultAttempt);
	
	/**
	 * 
	 * @return top 5 attempt of a user
	 */
	List<MultiplicationResultAttempt> getStatsForUser(String userAlias);
	
	/**
	 * Get result attempt by id
	 */
	Optional<MultiplicationResultAttempt> getResultAttemptById(Long id);
}
