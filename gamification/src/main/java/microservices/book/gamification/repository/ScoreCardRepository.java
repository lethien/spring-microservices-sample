package microservices.book.gamification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import microservices.book.gamification.domain.LeaderBoardRow;
import microservices.book.gamification.domain.ScoreCard;

/**
* Handles CRUD operations with ScoreCards
*/
public interface ScoreCardRepository extends CrudRepository<ScoreCard, Long> {

	/**
	* Gets the total score for a given user, being the sum of the scores of all his ScoreCards.
	* @param userId the id of the user for which the total score should be retrieved
	* @return the total score for the given user
	*/
	@Query("Select SUM(s.score) From microservices.book.gamification.domain.ScoreCard s " + 
				"Where s.userId = :userId Group By s.userId")
	int getTotalScoreForUser(@Param("userId") final Long userId);
	
	/**
	* Retrieves a list of {@link LeaderBoardRow}s representing the Leader Board of users and their total score.
	* @return the leader board, sorted by highest score first.
	*/
	@Query("Select New microservices.book.gamification.domain.LeaderBoardRow(s.userId, SUM(s.score)) " +
				"From microservices.book.gamification.domain.ScoreCard s " +
				"Group By s.userId Order By SUM(s.score) DESC")
	List<LeaderBoardRow> findFirst10();
	
	/**
	* Retrieves all the ScoreCards for a given user, identified by his user id.
	* @param userId the id of the user
	* @return a list containing all the ScoreCards for the given user, sorted by most recent.
	*/
	List<ScoreCard> findByUserIdOrderByScoreTimestampDesc(final Long userId);
}
