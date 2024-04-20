package otus.steps;

import com.google.inject.Inject;
import io.cucumber.java.ru.Тогда;
import java.io.IOException;
import otus.pages.CoursePage;
import otus.support.GuiceScoped;

public class CoursePageSteps {
  
  @Inject
  private GuiceScoped guiceScoped;
  
  @Inject
  private CoursePage coursePage;
  
  @Тогда("Откроется страница карточки курса")
  public void coursePageShouldBeOpened() throws IOException {
    coursePage.pageShouldBeOpened(guiceScoped.courseName);
    coursePage.validationCourseDate(guiceScoped.courseDate);
  }
}
