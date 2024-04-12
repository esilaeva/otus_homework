package components;

import annotations.Component;
import com.google.inject.Inject;
import org.openqa.selenium.WebDriver;

@Component("xpath://section[.//*[text()='%s']]")
public class BlockWithItemComponent extends AnyComponentAbs<BlockWithItemComponent> {
  
  @Inject
  public BlockWithItemComponent(WebDriver driver) {
    super(driver);
  }
}
