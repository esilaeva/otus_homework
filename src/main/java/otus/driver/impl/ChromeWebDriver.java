package otus.driver.impl;

import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeWebDriver implements IDriver {
  
  @Override
  public ChromeOptions getDriverOptions() {
    ChromeOptions chromeOptions = new ChromeOptions();
    
    chromeOptions.addArguments("--no-sandbox");
    chromeOptions.addArguments("--no-first-run");
    chromeOptions.addArguments("--enable-extensions");
    chromeOptions.addArguments("--homepage=about:blank");
    chromeOptions.addArguments("--ignore-certificate-errors");
    chromeOptions.addArguments("--start-maximized");
    
    return chromeOptions;
  }
}
