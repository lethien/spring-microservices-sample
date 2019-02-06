package microservices.book.gamification.domain;

/**
* Enumeration with the different types of Badges that a user can win.
*/
public enum Badge {
	// Badges depending on scores
	BRONZE_MULTIPLICATOR,
	SILVER_MULTIPLICATOR,
	GOLD_MULTIPLICATOR,
	// Badges for winning different conditions
	FIRST_ATTEMPT,
	FIRST_WON,
	LUCKY_NUMBER
}
