package otus.components;

import otus.annotations.Component;
import com.google.inject.Inject;
import otus.support.GuiceScoped;

@Component("xpath://section[.//*[text()='%s']]")
public class BlockWithItemComponent extends AnyComponentAbs<BlockWithItemComponent> {
  
  @Inject
  public BlockWithItemComponent(GuiceScoped guiceScoped) {
    super(guiceScoped);
  }
}
