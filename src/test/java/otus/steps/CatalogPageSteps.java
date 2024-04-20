package otus.steps;

import com.google.inject.Inject;
import io.cucumber.java.ru.Если;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import otus.annotations.UrlPrefix;
import otus.pages.CatalogPage;
import otus.support.GuiceScoped;

@UrlPrefix("/catalog/courses")
public class CatalogPageSteps {
  
  @Inject
  private CatalogPage catalogPage;
  
  @Inject
  private GuiceScoped guiceScoped;
  
  @Пусть("Открыта страница каталога курсов")
  public void openCatalogPage() {
    catalogPage.open();
  }
  
  @Если("Кликнуть на плитку раннего курса")
  public void clickEarlierCourseTileByName() {
    List<Map<String, Object>> listCourses = catalogPage.findAllCourses();
    guiceScoped.courseName = (String) listCourses.get(0).get("title");
    guiceScoped.courseDate = (LocalDate) listCourses.get(0).get("date");
    
    catalogPage.clickCourseByName(guiceScoped.courseName);
  }
  
  @Если("Кликнуть на плитку позднего курса")
  public void clickLastestCourseTileByName() {
    List<Map<String, Object>> listCourses = catalogPage.findAllCourses();
    guiceScoped.courseName = (String) listCourses.get(listCourses.size() - 1).get("title");
    guiceScoped.courseDate = (LocalDate) listCourses.get(listCourses.size() - 1).get("date");
    
    catalogPage.clickCourseByName(guiceScoped.courseName);
  }
  
  @Тогда("Откроется каталог курсов с выбранной категорией")
  public void catalogPageShouldBeOpenWithCheckCategory() {
    catalogPage.categoryCheck(guiceScoped.categoryName);
  }
}


