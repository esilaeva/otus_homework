package otus.support;

import io.cucumber.guice.ScenarioScoped;
import java.time.LocalDate;
import org.openqa.selenium.WebDriver;
import otus.driver.DriverFactory;

@ScenarioScoped
public class GuiceScoped {
  
  public WebDriver driver = new DriverFactory().getDriver();
  
  public String courseName = "";
  public LocalDate courseDate = null;
  public String categoryName = "";
}
