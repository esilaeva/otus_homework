package pages;

import static org.openqa.selenium.WebElement.*;

import annotations.UrlPrefix;
import com.google.inject.Inject;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@UrlPrefix("/catalog/courses")
public class CatalogPage extends AnyPageAbs<CatalogPage> {
  
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy", new Locale("ru", "RU"));
  Document page = Jsoup.connect(getUrl()).get();
  
  @Inject
  public CoursePage coursePage;
  
  @Inject
  public CatalogPage(WebDriver driver) throws IOException {
    super(driver);
  }
  
  public void clickCourseByName(String courseName) {
    driver.findElements(By.xpath("//section[2]//a[contains(@href, 'lessons')]//h6"))
        .stream()
        .filter(e -> e.getText().contains(courseName))
        .findFirst().ifPresent(WebElement::click);
  }
  
}
