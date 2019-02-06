package microservices.book.gamification.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.client.MultiplicationResultAttemptClient;
import microservices.book.gamification.client.dto.MultiplicationResultAttempt;
import microservices.book.gamification.domain.Badge;
import microservices.book.gamification.domain.BadgeCard;
import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

	private static final int LUCKY_NUMBER = 42;
	
	private ScoreCardRepository scoreCardRepository;
	private BadgeCardRepository badgeCardRepository;
	
	private MultiplicationResultAttemptClient attemptClient;
	
	GameServiceImpl(ScoreCardRepository scoreCardRepository,
			BadgeCardRepository badgeCardRepository,
			MultiplicationResultAttemptClient attemptClient) {
		this.scoreCardRepository = scoreCardRepository;
		this.badgeCardRepository = badgeCardRepository;
		this.attemptClient = attemptClient;
	}
	
	@Override
	public GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct) {
		if(correct) {
			ScoreCard scoreCard = new ScoreCard(userId, attemptId);
			scoreCardRepository.save(scoreCard);
			
			log.info("User with id {} score {} points for attempt id {}", userId, scoreCard.getScore(), attemptId);
			
			List<BadgeCard> badgeCards = processForBadges(userId, attemptId);
			
			return new GameStats(userId, scoreCard.getScore(), 
					badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
		}
		
		return GameStats.emptyStats(userId);
	}

	/**
	* Checks the total score and the different score cards obtained
	* to give new badges in case their conditions are met.
	*/
	private List<BadgeCard> processForBadges(Long userId, Long attemptId) {
		List<BadgeCard> badgeCards = new ArrayList<>();
		
		int totalScore = scoreCardRepository.getTotalScoreForUser(userId);
		log.info("New score for user {} is {}", userId, totalScore);
		
		List<ScoreCard> scoreCardList = scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId);
		List<BadgeCard> badgeCardList = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);
		
		// Badges based on score
		checkAndGiveBadgeBasedOnScore(badgeCardList, Badge.BRONZE_MULTIPLICATOR, totalScore, 100, userId)
			.ifPresent(badgeCards::add);
		checkAndGiveBadgeBasedOnScore(badgeCardList, Badge.SILVER_MULTIPLICATOR, totalScore, 500, userId)
			.ifPresent(badgeCards::add);
		checkAndGiveBadgeBasedOnScore(badgeCardList, Badge.GOLD_MULTIPLICATOR, totalScore, 999, userId)
			.ifPresent(badgeCards::add);
		
		// First won badge
		if(scoreCardList.size() == 1 && !containsBadge(badgeCardList, Badge.FIRST_WON)) {
			BadgeCard firstWonBadge = giveBadgeToUser(Badge.FIRST_WON, userId);
			badgeCards.add(firstWonBadge);
		}
		
		// Lucky number badge
		MultiplicationResultAttempt attempt = attemptClient.retrieveMultiplicationResultAttemptbyId(attemptId);
		if(!containsBadge(badgeCardList, Badge.LUCKY_NUMBER) && 
				(LUCKY_NUMBER == attempt.getMultiplicationFactorA() || LUCKY_NUMBER == attempt.getMultiplicationFactorB())) {
			BadgeCard luckyNumberBadge = giveBadgeToUser(Badge.LUCKY_NUMBER, userId);
			badgeCards.add(luckyNumberBadge);
		}
		
		return badgeCards;
	}

	/**
	* Convenience method to check the current score against
	* the different thresholds to gain badges.
	* It also assigns badge to user if the conditions are met.
	*/
	private Optional<BadgeCard> checkAndGiveBadgeBasedOnScore(final List<BadgeCard> badgeCardList, 
			final Badge badge, final int score, final int scoreTreshold, final Long userId) {
		if(score > scoreTreshold && !containsBadge(badgeCardList, badge)) {
			return Optional.of(giveBadgeToUser(badge, userId));
		}
		return Optional.empty();
	}

	/**
	* Assigns a new badge to the given user
	*/
	private BadgeCard giveBadgeToUser(Badge badge, Long userId) {
		BadgeCard badgeCard = new BadgeCard(userId, badge);
		badgeCardRepository.save(badgeCard);
		log.info("User with id {} won a new badge: {}", userId, badge);
		
		return badgeCard;
	}

	/**
	* Checks if the passed list of badges includes the one being checked
	*/
	private boolean containsBadge(List<BadgeCard> badgeCardList, Badge badge) {
		boolean contains = badgeCardList.stream().anyMatch(badgeCard -> badgeCard.getBadge().equals(badge));
		return contains;
	}

	@Override
	public GameStats retrieveGameStatsForUser(Long userId) {
		int score = scoreCardRepository.getTotalScoreForUser(userId);
		List<BadgeCard> badgeCards = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);
		
		return new GameStats(userId, score, 
				badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
	}

}
