package tests.api.helpers;

import extensions.ApiExtension;
import extensions.RequiresCleanup;
import org.citrusframework.annotations.CitrusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class, ApiExtension.class})
@ContextConfiguration(locations = "classpath:citrus-jdbc-context.xml")
public class SqlHelperTests implements RequiresCleanup {
  @Autowired
  @Qualifier("jdbcTemplate")
  private JdbcTemplate jdbcTemplate;
  
  @Test
  @CitrusTest
  public void databaseTest() {
    try {
      // Creating a table if it does not exist
      String createTableSQL = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, name TEXT)";
      jdbcTemplate.execute(createTableSQL);
      
      // Insert data
      String insertSQL = "INSERT INTO users (name) VALUES (?)";
      jdbcTemplate.update(insertSQL, "John Doe");
      
      // Data validation
      String selectSQL = "SELECT name FROM users WHERE id = 1";
      String userName = jdbcTemplate.queryForObject(selectSQL, String.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Database operation failed", e);
    }
  }
  
  @Override
  public void cleanup() {
    try {
      String deleteSQL = "DELETE FROM users WHERE id = 1";
      jdbcTemplate.execute(deleteSQL);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Database operation failed", e);
    }
  }
}