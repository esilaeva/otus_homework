package pages;

import annotations.UrlPrefix;
import com.github.javafaker.Faker;
import com.google.inject.Inject;
import java.util.List;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@UrlPrefix("/")
public class MainPage extends AnyPageAbs<MainPage> {
  
  private By hoverElement = By.cssSelector("span[title='Обучение']");
  @Inject
  public MainPage(WebDriver driver) {
    super(driver);
  }
  
  public MainPage hoverElement() {
    moveToElement(hoverElement);
    
    return this;
  }
  
  public String clickRandomCategory() {
    String categoryName = "";
    
    List<WebElement> listCategories = getListWebElements(By.xpath("//nav//div[3]//div//a"));
    Pattern pattern = Pattern.compile("^.+\\s*\\(\\d+\\)$");
    List<WebElement> filteredList = listCategories.stream()
        .filter(element -> pattern.matcher(element.getText()).matches())
        .toList();
    
    Faker faker = new Faker();
    if (!filteredList.isEmpty()) {
      int index = faker.number().numberBetween(0, filteredList.size() - 1);
      WebElement category = filteredList.get(index);
      categoryName = category.getText();
      category.click();
    }
    return categoryName;
  }
}
