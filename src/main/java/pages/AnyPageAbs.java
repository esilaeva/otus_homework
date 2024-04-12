package pages;

import actions.CommonActions;
import annotations.PageValidation;
import annotations.UrlPrefix;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AnyPageAbs<T> extends CommonActions<T> {
  
  protected String markerLocator = "";
  
  public AnyPageAbs(WebDriver driver) {
    super(driver);
    markerLocator = pageValidation();
  }
  
  private static <T> T convertInstanceOfObject(Object o, Class<T> clazz) {
    try {
      return clazz.cast(o);
    } catch (ClassCastException e) {
      return null;
    }
  }
  
  private String pageValidation() {
    if (getClass().isAnnotationPresent(PageValidation.class)) {
      PageValidation pageValidation = getClass().getAnnotation(PageValidation.class);
      String markerElementLocator = pageValidation.value();
      if (markerElementLocator.startsWith("template:")) {
        return markerElementLocator.replace("template:", "");
      }
      
      By locator = null;
      if (markerElementLocator.startsWith("/")) {
        locator = By.xpath(markerElementLocator);
      } else {
        locator = By.cssSelector(markerElementLocator);
      }
      
      waiter.waitForElementVisible($(locator));
    }
    
    return "";
  }
  
  private String getBaseUrl() {
    return StringUtils.stripEnd(System.getProperty("webdriver.base.url", "https://otus.ru"), "/");
  }
  
  private String getUrlPrefix() {
    UrlPrefix urlAnnotation = getClass().getAnnotation(UrlPrefix.class);
    if (urlAnnotation != null) {
      return urlAnnotation.value();
    }
    
    return "";
  }
  
  public T open() {
    driver.get(getUrlPage());
    
    return (T) this;
  }
  
  public String getUrlPage() {
    
    return getBaseUrl() + getUrlPrefix();
  }
  
  public List<WebElement> getListWebElements(By by) {
    return driver.findElements(by);
  }
}
