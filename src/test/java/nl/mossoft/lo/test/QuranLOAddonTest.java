package nl.mossoft.lo.test;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class QuranLOAddonTest {

  @BeforeMethod (groups = { "unitTests"})
  public void beforeMethod() {
    System.out.println("Starting Unit tests");
  }

  @Test (groups = { "unitTests"})
  public void SmokeTest() {
    System.out.println("I am add Unit tests");
  }

  @AfterMethod (groups = { "unitTests"})
  public void afterMethod() {
     System.out.println("Finished Unit tests");
  }
}
