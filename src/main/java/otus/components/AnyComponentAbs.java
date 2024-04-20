package otus.components;

import otus.actions.CommonActions;
import otus.annotations.Component;
import com.google.inject.Inject;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import otus.support.GuiceScoped;

public class AnyComponentAbs<T> extends CommonActions<T> {
  
  private static final String REGEX_REMOVE_PREFIX = "^(\\w+):.*?";
  protected Actions actions;
  protected String title = "";
  
  @Inject
  public AnyComponentAbs(GuiceScoped guiceScoped) {
    super(guiceScoped);
    actions = new Actions(driver);
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
      String removePrefixSearchStrategy = value.replace(String.format("%s:", searchStrategy), "");
      
      Pattern pattern = Pattern.compile(REGEX_REMOVE_PREFIX);
      Matcher matcher = pattern.matcher(value);
      if (matcher.find()) {
        searchStrategy = matcher.group(1).toLowerCase(Locale.ROOT);
      }
      
      switch (searchStrategy) {
        case "xpath" -> {
          return By.xpath(removePrefixSearchStrategy);
        }
        case "id" -> {
          return By.id(removePrefixSearchStrategy);
        }
      }
    }
    
    return null;
  }
}
