package microservices.book.socialmultiplication.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import microservices.book.socialmultiplication.domain.Multiplication;
import microservices.book.socialmultiplication.service.MultiplicationService;

@RunWith(SpringRunner.class)
@WebMvcTest(MultiplicationController.class)
public class MultiplicationControllerTest {

	@MockBean
	private MultiplicationService multiplicationService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private JacksonTester<Multiplication> json;
	
	@Before
	public void setUp() {
		JacksonTester.initFields(this, new ObjectMapper());
	}
	
	@Test
	public void getRandomMultiplicationTest() throws Exception {
		// given
		BDDMockito.given(multiplicationService.createRandomMultiplication())
				.willReturn(new Multiplication(70, 20));
		
		// when
		MockHttpServletResponse response = mockMvc.perform(get("/multiplications/random").accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		// then
		Assertions.assertThat(response.getContentAsString())
				.isEqualTo(json.write(new Multiplication(70, 20)).getJson());
	}
}
