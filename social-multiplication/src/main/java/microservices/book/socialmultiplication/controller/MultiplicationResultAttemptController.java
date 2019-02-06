package microservices.book.socialmultiplication.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;
import microservices.book.socialmultiplication.service.MultiplicationService;

@RestController
@RequestMapping("/results")
public class MultiplicationResultAttemptController {

	private final MultiplicationService multiplicationService;
	
	@Autowired
	MultiplicationResultAttemptController(MultiplicationService multiplicationService) {
		this.multiplicationService = multiplicationService;
	}
	
	@PostMapping(produces = "application/json")
	ResponseEntity<MultiplicationResultAttempt> postResult(@RequestBody MultiplicationResultAttempt multiplicationResultAttempt) {
		boolean isCorrect = multiplicationService.checkAttempt(multiplicationResultAttempt);
		
		MultiplicationResultAttempt attemptCopy = new
				MultiplicationResultAttempt(
				multiplicationResultAttempt.getUser(),
				multiplicationResultAttempt.getMultiplication(),
				multiplicationResultAttempt.getResultAttempt(),
				isCorrect
				);
		
		return ResponseEntity.ok(attemptCopy);
	}
	
	@GetMapping(produces = "application/json")
	ResponseEntity<List<MultiplicationResultAttempt>> getStatistics(@RequestParam("alias") String alias) {
		return ResponseEntity.ok(
				multiplicationService.getStatsForUser(alias)
		);
	}
	
	@GetMapping(value = "/{resultId}", produces = "application/json")
	ResponseEntity<MultiplicationResultAttempt> getResultById(@PathVariable("resultId") Long resultId) {
		Optional<MultiplicationResultAttempt> attempt = multiplicationService.getResultAttemptById(resultId);
		
		return ResponseEntity.of(attempt);
	}
}
