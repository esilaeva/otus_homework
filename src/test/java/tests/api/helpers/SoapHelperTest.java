package tests.api.helpers;

import static org.citrusframework.ws.actions.SoapActionBuilder.soap;

import features.PojoToXML;
import io.qameta.allure.Step;
import java.math.BigDecimal;
import org.citrusframework.TestActionRunner;
import org.citrusframework.annotations.CitrusResource;
import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.junit.jupiter.CitrusSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import services.ServiceApiAbs;
import webservicesserver.NumberToDollars;
import webservicesserver.NumberToDollarsResponse;

@CitrusSupport
@ContextConfiguration(locations = "classpath:citrus-context.xml")
public class SoapHelperTest extends ServiceApiAbs {
  
  PojoToXML<Class<NumberToDollars>> ptxRq = new PojoToXML<>();
  PojoToXML<Class<NumberToDollarsResponse>> ptxRs = new PojoToXML<>();
  
  @Test
  @CitrusTest
  @DisplayName("Soap Helper")
  public void getTestActions(@CitrusResource TestActionRunner runner) {
    
    runner.$(soap().client("soapClient")
        .send()
        .message()
        .body(ptxRq.convert(
            NumberToDollars.class,
            getNumberToDollarsRequest(),
            getBaseSoapUrl() + "/",
            "NumberToDollars"))
    );
    
    runner.$(soap().client("soapClient")
        .receive()
        .message()
        .body(ptxRs.convert(
            NumberToDollarsResponse.class,
            getNumberToDollarsResponse(),
            getBaseSoapUrl() + "/",
            "NumberToDollarsResponse"))
    
    );
  }
  
  public NumberToDollars getNumberToDollarsRequest() {
    NumberToDollars numberToDollars = new NumberToDollars();
    numberToDollars.setDNum(new BigDecimal("15"));
    return numberToDollars;
  }
  
  public NumberToDollarsResponse getNumberToDollarsResponse() {
    NumberToDollarsResponse numberToDollarsResponse = new NumberToDollarsResponse();
    numberToDollarsResponse.setNumberToDollarsResult("fifteen dollars");
    return numberToDollarsResponse;
  }
}
