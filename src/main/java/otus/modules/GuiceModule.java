//package otus.modules;
//
//import com.google.inject.AbstractModule;
//import com.google.inject.Provides;
//import org.aeonbits.owner.ConfigFactory;
//import org.openqa.selenium.WebDriver;
//import otus.config.WebConfig;
//import otus.driver.DriverFactory;
//
//public class GuiceModule extends AbstractModule {
//
//  private final WebConfig config = ConfigFactory.create(WebConfig.class, System.getProperties());
//  private final WebDriver driver = new DriverFactory(config).getDriver();
//
//  @Provides
//  public WebDriver getDriver() {
//    return driver;
//  }
//}
