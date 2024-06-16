package extensions;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ApiExtension implements AfterEachCallback {
  
  @Override
  public void afterEach(ExtensionContext context)  {
    Object testInstance = context.getRequiredTestInstance();
    if (testInstance instanceof RequiresCleanup) {
      ((RequiresCleanup) testInstance).cleanup();
    }
  }
}