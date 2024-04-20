package otus.actions;

import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import otus.support.GuiceScoped;
import otus.waiters.Waiter;

public class CommonActions<T> {
  
  protected WebDriver driver;
  protected Waiter waiter;
  
  @Inject
  public CommonActions(GuiceScoped guiceScoped) {
    this.driver = guiceScoped.driver;
    PageFactory.initElements(driver, this);
    
    waiter = new Waiter(driver);
  }
  
  public void moveToElement(By by) {
    Actions actions = new Actions(driver);
    actions.moveToElement($(by))
        .build()
        .perform();
  }
  
  public WebElement $(By by) {
    return driver.findElement(by);
  }
}
