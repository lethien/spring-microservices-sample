package microservices.book.socialmultiplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import microservices.book.socialmultiplication.domain.Multiplication;
import microservices.book.socialmultiplication.service.MultiplicationService;

@RestController
@RequestMapping("/multiplications")
final class MultiplicationController {

	private final MultiplicationService multiplicationService;
	
	@Autowired
	public MultiplicationController(MultiplicationService multiplicationService) {
		this.multiplicationService = multiplicationService;
	}
	
	@GetMapping(value = "/random", produces = "application/json")
	Multiplication getRandomMultiplication() {
		return multiplicationService.createRandomMultiplication();
	}
}
