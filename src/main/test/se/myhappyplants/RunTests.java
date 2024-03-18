package se.myhappyplants;

import org.junit.Test;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


@Suite
@SelectClasses({LoginPaneControllerTest.class, MainPaneControllerTest.class, MyPlantsTabPaneControllerTest.class, SearchTabPaneControllerTest.class, SettingsTabPaneControllerTest.class, UserPlantRepositoryTest.class, UserTest.class, VerifierTest.class})
public class RunTests {
}
