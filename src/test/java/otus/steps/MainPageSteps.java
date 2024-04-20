package otus.steps;

import com.google.inject.Inject;
import io.cucumber.java.ru.Если;
import io.cucumber.java.ru.Пусть;
import otus.pages.MainPage;

public class MainPageSteps {
  
  @Inject
  private MainPage mainPage;
  
  @Пусть("Открываем главную страницу")
  public void open() {
    mainPage.open();
  }
  
  @Если("Наводим мышкой на блок Обучение")
  public void hoverElement() {
    mainPage.hoverElement();
  }
  
  @Если("Кликнуть на рандомную категорию")
  public void clickRandomCategory() {
    mainPage.clickRandomCategory();
  }
}
