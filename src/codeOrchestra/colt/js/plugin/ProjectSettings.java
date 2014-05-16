package codeOrchestra.colt.js.plugin;

import codeOrchestra.colt.core.rpc.model.ColtLauncherType;
import com.intellij.openapi.components.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Dima Kruk
 */
@State(
        name = "ColtProjectSettings",
        storages = {
                @Storage(
                        file = StoragePathMacros.PROJECT_CONFIG_DIR + "/colt_project_settings.xml")
        }
)
public class ProjectSettings implements PersistentStateComponent<ProjectSettings.State>, ProjectComponent {

    private static ProjectSettings instance = new ProjectSettings();

    public static ProjectSettings getInstance() {
        if (instance == null) {
            instance = new ProjectSettings();
        }
        return instance;
    }

    private State myState = new State();

    @Nullable
    @Override
    public State getState() {
        return myState;
    }

    @Override
    public void loadState(State state) {
        System.out.println("load");
        myState = state;
    }

    @Override
    public void projectOpened() {

    }

    @Override
    public void projectClosed() {

    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "COLT Project Settings";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static class State {
        public ColtLauncherType lastLauncherType = null;
        public String projectPath = "";
        public String runConfigurationName = "";

    }
}
