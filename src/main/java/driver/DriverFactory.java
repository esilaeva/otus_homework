package driver;

import config.WebConfig;
import driver.impl.ChromeWebDriver;
import driver.impl.FirefoxWebDriver;
import driver.impl.IDriver;
import exceptions.DriverTypeNotSupported;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import listeners.ActionsListener;
import lombok.AllArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;

@AllArgsConstructor
public class DriverFactory {
  
  private static final String BROWSER_CHROME = "chrome";
  private static final String BROWSER_FIREFOX = "firefox";
  private final WebConfig config;
  
  public WebDriver getDriver() {
    WebDriver driver;
    switch (config.getBrowserName()) {
      case BROWSER_CHROME -> {
        WebDriverManager.chromiumdriver().setup();
        IDriver<ChromeOptions> browserSettings = new ChromeWebDriver();
        if (config.isRemote()) {
          DesiredCapabilities capabilities = new DesiredCapabilities();
          capabilities.setCapability(ChromeOptions.CAPABILITY, browserSettings.getDriverOptions());
          capabilities.setCapability("selenoid:options", Map.of("enableVNC", true));
          capabilities.setCapability(CapabilityType.BROWSER_NAME, config.getBrowserName());
          capabilities.setCapability(CapabilityType.BROWSER_VERSION, config.getBrowserVersion());
          try {
            return new RemoteWebDriver(new URL(config.getRemoteUrl()), capabilities);
          } catch (MalformedURLException e) {
            throw new RuntimeException(e);
          }
        } else {
          return new EventFiringDecorator<>(new ActionsListener())
              .decorate(new ChromeDriver(browserSettings.getDriverOptions()));
        }
      }
      case BROWSER_FIREFOX -> {
        WebDriverManager.firefoxdriver().setup();
        IDriver<FirefoxOptions> browserSettings = new FirefoxWebDriver();
        if (config.isRemote()) {
          DesiredCapabilities capabilities = new DesiredCapabilities();
          capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, browserSettings.getDriverOptions());
          capabilities.setCapability("selenoid:options", Map.of("enableVNC", true));
          capabilities.setCapability(CapabilityType.BROWSER_NAME, config.getBrowserName());
          capabilities.setCapability(CapabilityType.BROWSER_VERSION, config.getBrowserVersion());
          try {
            return new RemoteWebDriver(new URL(config.getRemoteUrl()), capabilities);
          } catch (MalformedURLException e) {
            throw new RuntimeException(e);
          }
        } else {
          return new EventFiringDecorator<>(new ActionsListener())
              .decorate(new FirefoxDriver(browserSettings.getDriverOptions()));
        }
      }
      default -> {
        try {
          throw new DriverTypeNotSupported(config.getBrowserName());
        } catch (DriverTypeNotSupported ex) {
          ex.printStackTrace();
          return null;
        }
      }
    }
  }
}
