package tests.ui.tests;

import com.google.inject.Inject;
import components.BlockWithItemComponent;
import extensions.UIExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.MainPage;

@ExtendWith(UIExtension.class)
public class TeachersBlockTest {
  
  @Inject
  private MainPage mainPage;
  
  @Inject
  private BlockWithItemComponent blockWithItemComponent;
  
  @Test
  public void openTeacherCardByClick() {
    mainPage.open();
    
    String name = blockWithItemComponent
        .setTitle("Преподаватели")
        .getItemName(1);
    blockWithItemComponent.clickItem(name)
        .pageShouldBeOpened(name);
  }
}
