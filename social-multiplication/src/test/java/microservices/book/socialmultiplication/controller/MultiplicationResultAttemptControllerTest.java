package microservices.book.socialmultiplication.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.io.IOException;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import microservices.book.socialmultiplication.domain.Multiplication;
import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;
import microservices.book.socialmultiplication.domain.User;
import microservices.book.socialmultiplication.service.MultiplicationService;

@RunWith(SpringRunner.class)
@WebMvcTest(MultiplicationResultAttemptController.class)
public class MultiplicationResultAttemptControllerTest {

	@MockBean
	private MultiplicationService multiplicationService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private JacksonTester<MultiplicationResultAttempt> jsonResult;
	private JacksonTester<List<MultiplicationResultAttempt>> jsonResultList;
	
	@Before
	public void setUp() {
		JacksonTester.initFields(this, new ObjectMapper());
	}
	
	@Test
	public void postResultReturnCorrect() throws IOException, Exception {
		genericParameterizedTest(true);
	}
	
	@Test
	public void postResultReturnNotCorrect() throws IOException, Exception {
		genericParameterizedTest(false);
	}
	
	void genericParameterizedTest(final boolean correct) throws IOException, Exception {
		// given
		User user = new User("john doe");
		Multiplication multiplication = new Multiplication(50, 70);
		MultiplicationResultAttempt multiplicationResultAttempt = new MultiplicationResultAttempt(user, multiplication, 3500, correct);
		BDDMockito.given(multiplicationService.checkAttempt(ArgumentMatchers.any(MultiplicationResultAttempt.class)))
				.willReturn(correct);
				
		// when
		MockHttpServletResponse response = mockMvc.perform(post("/results")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonResult.write(multiplicationResultAttempt).getJson()))
				.andReturn().getResponse();
		
		// then
		Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		Assertions.assertThat(response.getContentAsString())
				.isEqualTo(jsonResult.write(new MultiplicationResultAttempt(
						multiplicationResultAttempt.getUser(),
						multiplicationResultAttempt.getMultiplication(),
						multiplicationResultAttempt.getResultAttempt(),
						correct)).getJson());
	}
	
	@Test
	public void getUserStats() throws Exception {
		// given
		User user = new User("john doe");
		Multiplication multiplication = new Multiplication(50, 70);
		MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
				user, multiplication, 3500, true);
		List<MultiplicationResultAttempt> recentAttempts = Lists.newArrayList(attempt, attempt);
		BDDMockito.given(multiplicationService
				.getStatsForUser("john_doe"))
				.willReturn(recentAttempts);
		
		// when
		MockHttpServletResponse response = mockMvc.perform(get("/results").param("alias", "john doe"))
				.andReturn().getResponse();
		
		// then
		Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		Assertions.assertThat(response.getContentAsString())
				.isEqualTo(jsonResultList.write(recentAttempts).getJson());
	}
}
