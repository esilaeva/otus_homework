package otus.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.openqa.selenium.WebDriver;
import otus.driver.DriverFactory;

public class GuiceModule extends AbstractModule {
  
  private final WebDriver driver = new DriverFactory().getDriver();
  
  @Provides
  public WebDriver getDriver() {
    return driver;
  }
}
