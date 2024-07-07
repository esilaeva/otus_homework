package otus.support;

import io.cucumber.guice.ScenarioScoped;
import java.time.LocalDate;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.WebDriver;
import otus.config.WebConfig;
import otus.driver.DriverFactory;

@ScenarioScoped
public class GuiceScoped {
  
  private final WebConfig config = ConfigFactory.create(WebConfig.class, System.getProperties());
  
  public WebDriver driver = new DriverFactory(config).getDriver();
  
  public String courseName = "";
  public LocalDate courseDate = null;
  public String categoryName = "";
}
