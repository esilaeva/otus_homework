package otus.pages;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.inject.Inject;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import otus.support.GuiceScoped;

public class CoursePage extends AnyPageAbs<CoursePage> {
  
  @Inject
  public CoursePage(GuiceScoped guiceScoped) throws IOException {
    super(guiceScoped);
  }
  
  public void pageShouldBeOpened(String courseTitle) {
    String locator = String.format("//h1[normalize-space(text())='%s']", courseTitle);
    
    assertThat(waiter.waitForElementVisible($(By.xpath(locator))))
        .as("Error").isTrue();
  }
  
  public void validationCourseDate(LocalDate date) throws IOException {
    Document page = Jsoup.connect(driver.getCurrentUrl()).get();
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM", new Locale("ru", "RU"));
    String dateStr = date.format(formatter);
    
    Elements elements = page.select("main section div:nth-of-type(3) p");
    if (elements.isEmpty()) {
      elements = page.select(".course-header2-bottom__item-text")
          .stream()
          .filter(e -> e.text().contains(dateStr))
          .collect(Collectors.toCollection(Elements::new));
    }
    
    boolean isDatePresent = elements.stream()
        .anyMatch(element -> element.text().contains(dateStr));
    
    assertThat(isDatePresent)
        .as("Error: Course date doesn't match").isTrue();
  }
}
