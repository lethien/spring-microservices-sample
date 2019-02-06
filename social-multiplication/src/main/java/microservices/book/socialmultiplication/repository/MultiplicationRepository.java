package microservices.book.socialmultiplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import microservices.book.socialmultiplication.domain.Multiplication;

public interface MultiplicationRepository extends CrudRepository<Multiplication, Long> {

	/**
	 * Find a Multiplication by the two factors, regardless of order
	 */
	@Query("select m from Multiplication m where (m.factorA = :a AND m.factorB = :b) OR (m.factorA = :b AND m.factorB = :a)")
	Optional<Multiplication> findByFactors(int a, int b);
}
