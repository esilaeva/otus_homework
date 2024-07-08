package waiters;

import io.qameta.allure.Step;
import java.time.Duration;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Waiter implements IWaiter {
  
  private WebDriver driver = null;
  
  public Waiter(WebDriver driver) {
    this.driver = driver;
  }
  
  @Override
  @Step("Ожидание для состояния")
  public boolean waitForCondition(ExpectedCondition condition) {
    WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
    try {
      webDriverWait.until(condition);
      return true;
    } catch (TimeoutException ex) {
      return false;
    }
  }
  
  @Step("Ожидание видимости элемента")
  public boolean waitForElementVisible(WebElement element) {
    return waitForCondition(ExpectedConditions.visibilityOf(element));
  }

}