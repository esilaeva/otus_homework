package otus.pages;

import com.google.inject.Inject;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import otus.actions.CommonActions;
import otus.annotations.PageValidation;
import otus.annotations.UrlPrefix;
import otus.support.GuiceScoped;

public class AnyPageAbs<T> extends CommonActions<T> {
  
  public static final String EMPTY = "";
  protected String markerLocator = EMPTY;
  
  @Inject
  public AnyPageAbs(GuiceScoped guiceScoped) {
    super(guiceScoped);
    markerLocator = pageValidation();
  }
  
  private String pageValidation() {
    if (getClass().isAnnotationPresent(PageValidation.class)) {
      PageValidation pageValidation = getClass().getAnnotation(PageValidation.class);
      String markerElementLocator = pageValidation.value();
      if (markerElementLocator.startsWith("template:")) {
        return markerElementLocator.replace("template:", EMPTY);
      }
      
      By locator = null;
      if (markerElementLocator.startsWith("/")) {
        locator = By.xpath(markerElementLocator);
      } else {
        locator = By.cssSelector(markerElementLocator);
      }
      
      waiter.waitForElementVisible($(locator));
    }
    
    return EMPTY;
  }
  
  private String getBaseUrl() {
    return StringUtils.stripEnd(System.getProperty("webdriver.base.url", "https://otus.ru"), "/");
  }
  
  private String getUrlPrefix() {
    UrlPrefix urlAnnotation = getClass().getAnnotation(UrlPrefix.class);
    if (urlAnnotation != null) {
      return urlAnnotation.value();
    }
    
    return EMPTY;
  }
  
  public void open() {
    driver.get(getUrlPage());
  }
  
  public String getUrlPage() {
    
    return getBaseUrl() + getUrlPrefix();
  }
  
  public List<WebElement> getListWebElements(By by) {
    return driver.findElements(by);
  }
}
