package driver;

import driver.impl.ChromeWebDriver;
import driver.impl.FirefoxWebDriver;
import driver.impl.IDriver;
import exceptions.DriverTypeNotSupported;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.Locale;
import listeners.ActionsListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;

public class DriverFactory {
  
  private static final String BROWSER_CHROME = "chrome";
  private static final String BROWSER_FIREFOX = "firefox";
  private String browserType = System.getProperty("browser", BROWSER_CHROME).toLowerCase(Locale.ROOT);
  
  public WebDriver getDriver() {
    switch (this.browserType) {
      case BROWSER_CHROME: {
        WebDriverManager.chromiumdriver().setup();
        IDriver<ChromeOptions> browserSettings = new ChromeWebDriver();
        
        return new EventFiringDecorator<>(new ActionsListener())
            .decorate(new ChromeDriver(browserSettings.getDriverOptions()));
      }
      case BROWSER_FIREFOX: {
        WebDriverManager.firefoxdriver().setup();
        IDriver<FirefoxOptions> browserSettings = new FirefoxWebDriver();
        
        return new EventFiringDecorator<>(new ActionsListener())
            .decorate(new FirefoxDriver(browserSettings.getDriverOptions()));
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
