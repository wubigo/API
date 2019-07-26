package com.timon.web;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlertControllerTests {
    TestRestTemplate testRestTemplate = new TestRestTemplate();
    String testUrl = "http://localhost:8080/alert";
    ResponseEntity<String> response;

    @Test
    public void testUnread() {

        response = testRestTemplate
                .getForEntity(testUrl + "/unread", String.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
    }
}
