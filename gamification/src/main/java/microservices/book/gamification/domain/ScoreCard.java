package microservices.book.gamification.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
* This class represents the Score linked to an attempt in the game,
* with an associated user and the timestamp in which the score
* is registered.
*/
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Entity
public final class ScoreCard {

	public static final int DEFAULT_SCORE = 10; 
	
	@Id
	@GeneratedValue
	@Column(name = "CARD_ID")
	private final Long cardId;
	
	private final Long userId;
	
	private final Long attemptId;
	
	private final long scoreTimestamp;
	
	private final int score;
	
	// Empty constructor for JPA/JSON
	public ScoreCard() {
		this(null, null, null, 0, 0);		
	}
	
	public ScoreCard(final Long userId, final Long attemptId) {
		this(null, userId, attemptId, System.currentTimeMillis(), DEFAULT_SCORE);
	}
}
