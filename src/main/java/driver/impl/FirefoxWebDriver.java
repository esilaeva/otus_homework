package driver.impl;

import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxWebDriver implements IDriver{
  
  @Override
  public Object getDriverOptions() {
    FirefoxOptions firefoxOptions = new FirefoxOptions();
    
    firefoxOptions.addArguments("--no-sandbox");
    firefoxOptions.addArguments("--disable-popup-blocking");
    firefoxOptions.addArguments("--ignore-certificate-errors");
    
    return firefoxOptions;
  }
}
