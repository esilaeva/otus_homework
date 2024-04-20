//package tests.ui;
//
//import com.google.inject.Inject;
//import otus.extensions.UIExtension;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import otus.pages.CatalogPage;
//import otus.pages.MainPage;
//
//@ExtendWith(UIExtension.class)
//public class MainPageTest {
//
//  @Inject
//  private MainPage mainPage;
//
//  @Inject
//  private CatalogPage catalogPage;
//
//  @Test
//  public void openCoursesCategory() {
//    String categoryName = mainPage
//        .open()
//        .hoverElement()
//        .clickRandomCategory();
//
//    catalogPage.categoryCheck(categoryName);
//  }
//}
