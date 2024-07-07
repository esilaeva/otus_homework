package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import config.WebConfig;
import driver.DriverFactory;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.WebDriver;

public class GuiceModule extends AbstractModule {
  
  private final WebConfig config = ConfigFactory.create(WebConfig.class, System.getProperties());
  private final WebDriver driver = new DriverFactory(config).getDriver();
  
  @Provides
  public WebDriver getDriver() {
    return driver;
  }
}
