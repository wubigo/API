package com.timon.web;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlertWebApplicationTests {
	TestRestTemplate testRestTemplate = new TestRestTemplate();
	String testUrl = "http://localhost:8080/alert";
	ResponseEntity<String> response;


	@Before
	public void setUp() {
		testRestTemplate.getRestTemplate().setInterceptors(
				Collections.singletonList((request, body, execution) -> {
					request.getHeaders()
							.add("TOKEN", "QWE123");
					return execution.execute(request, body);
				}));
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testPublicAccess() {

//		HttpHeaders headers = new HttpHeaders();
//		headers.add("your_header", "its_value");
//		testRestTemplate.exchange(testUrl + "/overview", HttpMethod.GET, new HttpEntity<>(headers), String.class);
		response = testRestTemplate
				.getForEntity(testUrl + "/overview", String.class);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertNotNull(response.getBody());
	}






}
