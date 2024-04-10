package driver;

import driver.impl.ChromeWebDriver;
import driver.impl.IDriver;
import exceptions.DriverTypeNotSupported;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.Locale;
import listeners.ActionsListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;

public class DriverFactory {
  
  private String browserType = System.getProperty("browser", "chrome").toLowerCase(Locale.ROOT);
  
  public WebDriver getDriver() {
    switch (this.browserType) {
      case "chrome": {
        WebDriverManager.chromiumdriver().setup();
        IDriver<ChromeOptions> browserSettings = new ChromeWebDriver();
        
        return new EventFiringDecorator<>(new ActionsListener())
            .decorate(new ChromeDriver(browserSettings.getDriverOptions()));
      }
      default:
        try {
          throw new DriverTypeNotSupported(this.browserType);
        } catch (DriverTypeNotSupported ex) {
          ex.printStackTrace();
          return null;
        }
    }
  }
}
