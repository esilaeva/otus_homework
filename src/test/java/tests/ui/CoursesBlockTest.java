package tests.ui;

import com.google.inject.Inject;
import extensions.UIExtension;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.CatalogPage;
import pages.CoursePage;

@ExtendWith(UIExtension.class)
public class CoursesBlockTest {
  
  public static final String COURSE_TITLE = "Программист С";
  @Inject
  private CatalogPage catalogPage;
  
  @Inject
  private CoursePage coursePage;
  
  @Test
  public void openCourseCardByClick() {
    String courseTitle = COURSE_TITLE;
    
    catalogPage
        .open()
        .clickCourseByName(courseTitle);
    
    coursePage.pageShouldBeOpened(courseTitle);
  }
  
  @Test
  public void openEarlierCourse() throws IOException {
    List<Map<String, Object>> listCourses = catalogPage.open().findAllCourses();
    String earlierCourseTitle = (String) listCourses.get(0).get("title");
    LocalDate earlierCourseDate = (LocalDate) listCourses.get(0).get("date");
    
    catalogPage.clickCourseByName(earlierCourseTitle);
    
    coursePage
        .pageShouldBeOpened(earlierCourseTitle)
        .validationCourseDate(earlierCourseDate);
  }
  
  @Test
  public void openLatestCourse() throws IOException {
    List<Map<String, Object>> listCourses = catalogPage.open().findAllCourses();
    String latestCourseTitle = (String) listCourses.get(listCourses.size() - 1).get("title");
    LocalDate latestCourseDate = (LocalDate) listCourses.get(listCourses.size() - 1).get("date");
    
    catalogPage.clickCourseByName(latestCourseTitle);
    
    coursePage
        .pageShouldBeOpened(latestCourseTitle)
        .validationCourseDate(latestCourseDate);
  }
}
