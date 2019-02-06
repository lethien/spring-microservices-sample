package microservices.book.socialmultiplication.service;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class RandomGeneratorServiceImpl implements RandomGeneratorService {

	private final int MINIMUM_FACTOR = 11;
	private final int MAXIMUM_FACTOR = 99;
	
	private Random rand = new Random();
	
	@Override
	public int generateRandomFactor() {		
		return rand.nextInt(MAXIMUM_FACTOR - MINIMUM_FACTOR + 1) + MINIMUM_FACTOR;
	}
	
}
