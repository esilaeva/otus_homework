package components;

import actions.CommonActions;
import annotations.Component;
import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnyComponentAbs<T> extends CommonActions<T> {
  
  protected Actions actions;
  protected String title = "";
  
  @Inject
  public AnyComponentAbs(WebDriver driver) {
    super(driver);
    actions = new Actions(driver);
  }
  
  public T setTitle(String title) {
    this.title = title;
    
    return (T) this;
  }
  
  public WebElement getComponentEntity() {
    return $(getComponentLocator());
  }
  private By getComponentLocator() {
    Component component = getClass().getAnnotation(Component.class);
    
    if (component != null) {
      String value = component.value();
      
      if (!this.title.isEmpty()) {
        value = String.format(value, this.title);
      }
      
      String searchStrategy = "";
      
      Pattern pattern = Pattern.compile("^(\\w+):.*?");
      Matcher matcher = pattern.matcher(value);
      if (matcher.find()) {
        searchStrategy = matcher.group(1).toLowerCase(Locale.ROOT);
      }
      
      switch (searchStrategy) {
        case "xpath":
          return By.xpath(value.replace(String.format("%s:", searchStrategy), ""));
        case "id":
          return By.id(value.replace(String.format("%s:", searchStrategy), ""));
      }
    }
    
    return null;
  }
}
