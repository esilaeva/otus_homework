package pages;

import annotations.UrlPrefix;
import com.google.inject.Inject;
import org.openqa.selenium.WebDriver;

@UrlPrefix("/")
public class MainPage extends AnyPageAbs<MainPage> {
  
  @Inject
  public MainPage(WebDriver driver) {
    super(driver);
  }
}
