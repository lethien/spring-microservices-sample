package microservices.book.socialmultiplication.event;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Event that models the fact that a {@link microservices.book.multiplication.domain.Multiplication}
 * has been solved in the system. Provides some context information about the multiplication.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class MultiplicationSolvedEvent implements Serializable {

	private final Long multiplicationResultAttemptId;
	private final Long userId;
	private final boolean correct;
}
