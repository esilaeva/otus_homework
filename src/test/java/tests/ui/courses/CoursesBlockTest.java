package tests.ui.courses;

import com.google.inject.Inject;
import extensions.UIExtension;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;
import pages.CatalogPage;
import pages.CoursePage;

@ExtendWith(UIExtension.class)
public class CoursesBlockTest {
  
  @Inject
  private CatalogPage catalogPage;
  
  @Inject
  private CoursePage coursePage;
  
  @Test
  public void openCourseCardByClick() {
    String name = "Reinforcement Learning";
    
    catalogPage
        .open()
        .clickCourseByName(name);
    
    coursePage.pageShouldBeOpened(name);
  }
}
