  /*
  *      Ma-Moulinette
  *  -=track-logger-method=-
  *  -------------------------
  *  Copyright (c) 2015-2024.
  *  Laurent HADJADJ <laurent_h@me.com>.
  *  Licensed Creative Common  CC-BY-NC-SA 4.0.
  *  ---
  *  Vous pouvez obtenir une copie de la licence à l'adresse suivante :
  *  http://creativecommons.org/licenses/by-nc-sa/4.0/
  */
  package fr.ma.moulinette.java;

  import org.junit.jupiter.api.BeforeEach;
  import org.junit.jupiter.api.Test;
  import org.mockito.Mock;
  import org.mockito.MockitoAnnotations;
  import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
  import org.sonar.plugins.java.Java;

  import static org.mockito.Mockito.*;

  /**
   * Unit test for MyJavaQualityProfile.
   */
  class MyJavaQualityProfileTest {

  @Mock
  private BuiltInQualityProfilesDefinition.Context context;

  @Mock
  private BuiltInQualityProfilesDefinition.NewBuiltInQualityProfile newProfile;

  /**
   * Setup method to initialize mocks before each test.
   */
  @BeforeEach
  void setUp() {
  // Initialize mocks
  MockitoAnnotations.openMocks(this);
  }

  /**
   * Test method to verify the behavior of define() in MyJavaQualityProfile.
   */
  @Test
  void testDefine() {
  // Mocking context.createBuiltInQualityProfile() to return a mock newProfile
  when(context.createBuiltInQualityProfile(
    MyJavaQualityProfile.QUALITY_PROFILE_NAME,
    Java.KEY))
    .thenReturn(newProfile);

  MyJavaQualityProfile profile = new MyJavaQualityProfile();

  // Call the define() method under test
  profile.define(context);

  // Verify that activateRule() is called with the correct arguments
  verify(newProfile).activateRule(
    MyJavaRulesDefinition.REPOSITORY_KEY,
    "track-info-method");

  verify(newProfile).activateRule(
    MyJavaRulesDefinition.REPOSITORY_KEY,
    "track-error-method");

  verify(newProfile).activateRule(
    MyJavaRulesDefinition.REPOSITORY_KEY,
    "track-warn-method");

  verify(newProfile).activateRule(
    MyJavaRulesDefinition.REPOSITORY_KEY,
    "track-debug-method");
  }
}
