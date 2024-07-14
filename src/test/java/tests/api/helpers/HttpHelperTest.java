package tests.api.helpers;

import static org.citrusframework.http.actions.HttpActionBuilder.http;

import org.citrusframework.TestActionRunner;
import org.citrusframework.annotations.CitrusEndpoint;
import org.citrusframework.annotations.CitrusResource;
import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.context.TestContext;
import org.citrusframework.http.client.HttpClient;
import org.citrusframework.junit.jupiter.CitrusSupport;
import org.citrusframework.message.builder.ObjectMappingPayloadBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pojo.Data;
import pojo.Support;
import pojo.User;
import services.ServiceApiAbs;

@CitrusSupport
public class HttpHelperTest extends ServiceApiAbs {
  
  @CitrusEndpoint
  private HttpClient restClient;
  @CitrusResource
  private TestContext context;
  
  @Test
  @CitrusTest
  @DisplayName("Http Helper")
  void restHttpHelperTest(@CitrusResource TestActionRunner actions) {
    User user = new User();
    
    Data data = new Data();
    data.setId(Integer.valueOf(context.getVariable("userId")));
    data.setEmail("janet.weaver@reqres.in");
    data.setFirstName("Janet2");
    data.setLastName("Weaver");
    data.setAvatar(getBaseApiUrl() + "/img/faces/2-image.jpg");
    user.setData(data);
    
    Support support = new Support();
    support.setUrl(getBaseApiUrl() + "/#support-heading");
    support.setText("To keep ReqRes free, contributions towards server costs are appreciated!");
    
    user.setSupport(support);
    
    actions.$(http()
        .client(restClient)
        .send()
        .get("users/" + context.getVariable("userId")));
    
    actions.$(http()
        .client(restClient)
        .receive()
        .response(HttpStatus.OK)
        .message()
        .type("application/json")
        .body(new ObjectMappingPayloadBuilder(user, "objectMapper"))
    );
  }
}
