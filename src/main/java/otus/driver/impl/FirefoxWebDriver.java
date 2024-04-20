package otus.driver.impl;

import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;

public class FirefoxWebDriver implements IDriver{
  
  @Override
  public Object getDriverOptions() {
    FirefoxOptions firefoxOptions = new FirefoxOptions();
    
    firefoxOptions.addArguments("--no-sandbox");
    firefoxOptions.addArguments("--ignore-certificate-errors");
    firefoxOptions.addArguments("--start-maximized");
    
    firefoxOptions.setCapability(CapabilityType.BROWSER_NAME, System.getProperty("browser", "firefox"));
    
    return firefoxOptions;
  }
}
