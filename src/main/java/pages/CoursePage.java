package pages;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CoursePage extends AnyPageAbs<CoursePage> {
  
  @Inject
  public CoursePage(WebDriver driver) {
    super(driver);
  }
  
  public void pageShouldBeOpened(String name) {
    String locator = String.format("//h1[normalize-space(text())='%s']", name);
    
    assertThat(standartWaiter.waitForElementVisible($(By.xpath(locator))))
        .as("Error").isTrue();
  }
}
