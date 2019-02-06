package microservices.book.socialmultiplication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;

public interface MultiplicationResultAttemptRepository 
		extends CrudRepository<MultiplicationResultAttempt, Long>{
	
	/**
	 * 
	 * @return the largest 5 attempts for a given user, identified by alias
	 */
	List<MultiplicationResultAttempt> findTop5ByUserAliasOrderByIdDesc(String userAlias);
	
	/**
	 * Find result attempt by id
	 */
	Optional<MultiplicationResultAttempt> findById(Long id);
}
