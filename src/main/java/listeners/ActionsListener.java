package listeners;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.events.WebDriverListener;

public class ActionsListener implements WebDriverListener {
  
  private static final String SCRIPT_HIGHLIGHT_ELEMENT = "arguments[0].setAttribute("
      + "\"onmouseover\", \"style='border:5px solid red';\");"
      + "arguments[0].setAttribute("
      + "\"onclick\", \"style=null;\");";
  
  @Override
  public void beforePerform(WebDriver driver, Collection<Sequence> actions) {
    actions.stream()
        .flatMap(action -> getOrigins(action).stream())
        .forEach(origin -> ((JavascriptExecutor) driver)
            .executeScript(SCRIPT_HIGHLIGHT_ELEMENT, origin));
  }
  @SuppressWarnings("unchecked")
  private List<WebElement> getOrigins(Sequence sequence) {
    Map<String, Object> encodedSequence = sequence.encode();
    if (!encodedSequence.containsKey("actions")) {
      return Collections.emptyList();
    }
    return ((List<Map<String, Object>>) encodedSequence.get("actions")).stream()
        .filter(action -> action.get("origin") instanceof WebElement)
        .map(action -> (WebElement) action.get("origin"))
        .toList();
  }
}
