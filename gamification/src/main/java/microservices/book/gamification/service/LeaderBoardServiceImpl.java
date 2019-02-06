package microservices.book.gamification.service;

import java.util.List;

import org.springframework.stereotype.Service;

import microservices.book.gamification.domain.LeaderBoardRow;
import microservices.book.gamification.repository.ScoreCardRepository;

@Service
public class LeaderBoardServiceImpl implements LeaderBoardService {

	private ScoreCardRepository scoreCardRepository;
	
	public LeaderBoardServiceImpl(ScoreCardRepository scoreCardRepository) {
		this.scoreCardRepository = scoreCardRepository;
	}

	@Override
	public List<LeaderBoardRow> getCurrentLeaderBoard() {
		List<LeaderBoardRow> leaderBoardRows = scoreCardRepository.findFirst10();
		return leaderBoardRows;
	}

}
