package com.open;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DemoApplicationTests {

	TestRestTemplate testRestTemplate = new TestRestTemplate();

	String testUrl = "http://localhost:8080/alert";
	ResponseEntity<String> response;

	@Before
	public void setUp() {
		BasicAuthorizationInterceptor bai = new BasicAuthorizationInterceptor("admin", "test");
		testRestTemplate.getRestTemplate().getInterceptors().add(bai);

//		testRestTemplate.getRestTemplate().setInterceptors(
//				Collections.singletonList((request, body, execution) -> {
//					request.getHeaders()
//							.add("TOKEN", "QWE123");
//					return execution.execute(request, body);
//				}));
	}


	@Test
	public void contextLoads() {
	}

	@Test
	public void testApi() {
		String url = "http://localhost:8080/flowable-task/process-api/runtime/process-instances";
		url = "http://localhost:8080/flowable-task/process-api/repository/process-definitions";

		//url = "http://localhost:8080/flowable-task/process-api/history/historic-process-instances";
		response = testRestTemplate.getForEntity(url, String.class);
		log.info("response={}", response.getBody());
	}

	@Test
	public void testJson() {
		String name = "open1";
		JSONObject var = new JSONObject();

		var.put("name", name);

		JSONArray ja = new JSONArray();
		ja.add(var);

		JSONObject o = new JSONObject();
		o.put("key", name);
		o.put("var", ja.toJSONString());
		log.info("ja={}", o.toJSONString());
		o.put("var", ja.toString());
		log.info("o={}", o.toString());
	}


	@Test
	public void testStartProcess() {
		String url = "http://localhost:8080/flowable-task/process-api/runtime/process-instances";
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);



		JSONObject var = new JSONObject();
		var.put("name", "addr");
		var.put("type", "string");
		var.put("value", "BIT");
		JSONArray ja = new JSONArray();
		ja.add(var);

		String processKey = "demo-http";
		JSONObject o = new JSONObject();
		o.put("processDefinitionKey", processKey);
		o.put("businessKey", processKey);

		o.put("variables", ja);
		//o.put("variables", lj);

		HttpEntity<String> request = new HttpEntity<String>(o.toJSONString(), headers);
		log.debug("request={}", request);
		String result = testRestTemplate.postForObject(url, request, String.class);
		log.info("result={}", result);
	}

	@Test
	public void testQueryTask() {
		//ProcessInstance instance = mRuntimeService.startProcessInstanceByKey("basicprocess", variables);
		//Task userTask = mTaskService.createTaskQuery().processInstanceId(instance.getId()).taskDefinitionKey("getInput").singleResult();
		//mTaskService.complete(userTask.getId());
		String url = "http://localhost:8080/flowable-task/process-api/runtime/tasks/";



		String result = testRestTemplate.getForObject(url + "?taskDefinitionKey=entry-form", String.class);
		log.info("result={}", result);
		DocumentContext jsonContext = JsonPath.parse(result);
		String id = jsonContext.read("$.data[0].id");
		log.info("task={}", id);
        if ( null != id ) {
			url += id;
			final HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);


			String name = "open1";
			JSONObject var = new JSONObject();
			var.put("name", "name");
			var.put("type", "string");
			var.put("value", name);
			JSONArray ja = new JSONArray();
			ja.add(var);
			JSONObject o = new JSONObject();
			o.put("action", "complete");
			o.put("variables", ja);

			HttpEntity<String> request = new HttpEntity<String>(o.toJSONString(), headers);
			log.debug("request={}", request);
			result = testRestTemplate.postForObject(url, request, String.class);
			log.info("result={}", result);
		}
	}


}