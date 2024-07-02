package services;

import org.apache.commons.lang3.StringUtils;

public class ServiceApiAbs {
  
  public String getBaseApiUrl() {
    return StringUtils.stripEnd(System.getProperty("baseApi.url", "https://reqres.in"), "/");
  }
  
  public String getBaseSoapUrl() {
    return StringUtils.stripEnd(System.getProperty("baseSoap.url", "http://www.dataaccess.com/webservicesserver"), "/");
  }
}
