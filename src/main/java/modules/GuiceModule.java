package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import driver.DriverFactory;
import org.openqa.selenium.WebDriver;

public class GuiceModule extends AbstractModule {
  
  private final WebDriver driver = new DriverFactory().getDriver();
  
  @Provides
  public WebDriver getDriver() {
    return driver;
  }
}
