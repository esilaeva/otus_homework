package pages;

import static org.assertj.core.api.Assertions.assertThat;

import annotations.UrlPrefix;
import com.google.inject.Inject;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@UrlPrefix("/catalog/courses")
public class CatalogPage extends AnyPageAbs<CatalogPage> {

  @Inject
  public CatalogPage(WebDriver driver) throws IOException {
    super(driver);
  }
  
  public void clickCourseByName(String courseTitle) {
    driver.findElements(By.xpath("//section[2]//a[contains(@href, 'lessons')]//h6"))
        .stream()
        .filter(e -> e.getText().contains(courseTitle))
        .findFirst().ifPresent(WebElement::click);
  }
  
  public List<Map<String, Object>> findAllCourses() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy", new Locale("ru", "RU"));
    Pattern datePattern = Pattern.compile("(\\d{1,2}\\s[а-яА-Я]+,\\s\\d{4})");
    
    return driver.findElements(By.xpath("//section[2]//a[contains(@href, 'lessons')]")).stream()
        .map(e -> {
          Map<String, Object> courseInfo = new HashMap<>();
          courseInfo.put("title", e.findElement(By.cssSelector("h6")).getText());
          
          String dateString = e.findElement(By.cssSelector("h6+div>div:last-child")).getText();
          LocalDate date = LocalDate.parse(Objects.requireNonNull(extractDate(dateString, datePattern, formatter)));
          
          courseInfo.put("date", date);
          
          return courseInfo;
        })
        .filter(map -> map.containsKey("date"))
        .sorted(Comparator.comparing(map -> (LocalDate) map.get("date")))
        .collect(Collectors.toList());
  }
  
  private String extractDate(String text, Pattern datePattern, DateTimeFormatter formatter) {
    Matcher matcher = datePattern.matcher(text);
    LocalDate localDate = matcher.find() ? LocalDate.parse(matcher.group(1), formatter) : null;
    
    return localDate != null ? localDate.toString() : null;
  }
  
  public void categoryCheck(String categoryName) {
    String clearCategoryName = categoryName.replaceAll("\\s*\\(\\d+\\)$", "");
    WebElement label = driver.findElement(By.xpath("//label[contains(text(), '" + clearCategoryName + "')]"));
    String checkboxId = label.getAttribute("for");
    WebElement checkbox = driver.findElement(By.id(checkboxId));
    
    assertThat(checkbox.isSelected())
        .as("Checkbox with id '%s' should be selected", checkboxId)
        .isTrue();
  }
}
