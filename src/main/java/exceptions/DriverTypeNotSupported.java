package exceptions;

public class DriverTypeNotSupported extends Exception {
  
  public DriverTypeNotSupported(String driverType) {
    super(String.format("Browser type %s doesn't support", driverType));
  }
}