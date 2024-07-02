package tests.api;

import static org.citrusframework.http.actions.HttpActionBuilder.http;

import org.citrusframework.TestActionRunner;
import org.citrusframework.annotations.CitrusEndpoint;
import org.citrusframework.annotations.CitrusResource;
import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.context.TestContext;
import org.citrusframework.http.client.HttpClient;
import org.citrusframework.http.server.HttpServer;
import org.citrusframework.junit.jupiter.CitrusSupport;
import org.citrusframework.spi.Resources;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

@CitrusSupport
@ContextConfiguration(locations = "classpath:citrus-context.xml")
public class MockTests {
  
  @CitrusEndpoint
  private HttpClient restClient;
  @CitrusEndpoint
  private HttpClient mockClient;
  @CitrusEndpoint
  private HttpServer restServer;
  @CitrusResource
  private TestContext context;
  private String getAllUsers = "/user/get/all";
  private String getAllCourses = "/cource/get/all";
  private String getScore = "/user/get/";
  
  @Test
  @CitrusTest
  public void getAllUsersMockTest(@CitrusResource TestActionRunner action) {
    action.$(http()
        .client(mockClient)
        .send()
        .get(getAllUsers)
        .fork(true)
    );
    
    action.$(http()
        .server(restServer)
        .receive()
        .get(getAllUsers)
    );
    
    action.$(http()
        .server(restServer)
        .send().response(HttpStatus.OK)
        .message()
        .type("application/json")
        .body("""
            {
              "name":"Test user",
              "cource":"QA",
              "email":"test@test.test",
              "age": 23
            }
        """)
    );
    
    action.$(http()
        .client(mockClient)
        .receive()
        .response(HttpStatus.OK)
        .message()
        .type("application/json")
        .body(new Resources.ClasspathResource("json/user.json"))
    );
  }
  
  @Test
  @CitrusTest
  public void getAllCoursesMockTest(@CitrusResource TestActionRunner action) {
    action.$(http()
        .client(mockClient)
        .send()
        .get(getAllCourses)
        .fork(true)
    );
    
    action.$(http()
        .server(restServer)
        .receive()
        .get(getAllCourses)
    );
    
    action.$(http()
        .server(restServer)
        .send().response(HttpStatus.OK)
        .message()
        .type("application/json")
        .body("""
            [
              {
                "name":"QA java",
                "price": 15000
              },
              {
                "name":"Java",
                "price": 12000
              }
            ]
        """)
    );
    
    action.$(http()
        .client(mockClient)
        .receive()
        .response(HttpStatus.OK)
        .message()
        .type("application/json")
        .body(new Resources.ClasspathResource("json/courses.json"))
    );
  }
  
  @Test
  @CitrusTest
  public void getScoreMockTest(@CitrusResource TestActionRunner action) {
    action.$(http()
        .client(mockClient)
        .send()
        .get(getScore + context.getVariable("userId"))
        .fork(true)
    );
    
    action.$(http()
        .server(restServer)
        .receive()
        .get(getScore + context.getVariable("userId"))
    );
    
    action.$(http()
        .server(restServer)
        .send().response(HttpStatus.OK)
        .message()
        .type("application/json")
        .body("""
                {
                   "name":"Test user",
                   "score": 78
                }
            """)
    );
    
    action.$(http()
        .client(mockClient)
        .receive()
        .response(HttpStatus.OK)
        .message()
        .type("application/json")
        .body(new Resources.ClasspathResource("json/score.json"))
    );
  }
}
