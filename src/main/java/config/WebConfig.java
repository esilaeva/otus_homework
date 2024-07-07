package config;

import org.aeonbits.owner.Config;

@Config.Sources({
    "classpath:${env}.properties"
})
public interface WebConfig extends Config {
  
  @Key("baseUrl")
  @DefaultValue("https://www.otus.ru/")
  String getBaseUrl();
  
  @Key("browserName")
  @DefaultValue("chrome")
  String getBrowserName();
  
  @Key("browserVersion")
  @DefaultValue("126.0")
  String getBrowserVersion();
  
  @Key("remoteUrl")
  String getRemoteUrl();
  
  @Key("isRemote")
  @DefaultValue("false")
  Boolean isRemote();

}
