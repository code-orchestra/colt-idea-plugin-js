package codeOrchestra.colt.core.plugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import codeOrchestra.colt.core.plugin.view.ColtConfigurationPage;
import com.intellij.openapi.util.SystemInfo;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

/**
 * @author Dima Kruk
 * @author Alexander Eliseyev
 */
@State(
    name = "ColtSettings",
    storages = {
            @Storage(
                file = StoragePathMacros.APP_CONFIG + "/colt_settings.xml")
    }
)
public class ColtSettings implements PersistentStateComponent<ColtSettings.State>, SearchableConfigurable, ApplicationComponent {

    private static ColtSettings instance = null;

    public static ColtSettings getInstance() {
        if (instance == null) {
            instance = ApplicationManager.getApplication().getComponent(ColtSettings.class);
        }
        return instance;
    }
    private State myState = new State();

    private ColtConfigurationPage configurationPage;

    @Nullable
    @Override
    public State getState() {
        if(myState.coltPath.equals("")) {
            if(SystemInfo.isMac) {
                myState.coltPath = "/Applications/COLT/COLT.app";
            } else if(SystemInfo.isWindows) {
                myState.coltPath = "C:\\Program Files\\COLT\\colt.exe";
            }
        }
        return myState;
    }

    @Override
    public void loadState(State state) {
        myState = state;
        //fix
        if(myState.coltPath.equals("")) {
            if(SystemInfo.isMac) {
                myState.coltPath = "/Applications/COLT/COLT.app";
            } else if(SystemInfo.isWindows) {
                myState.coltPath = "C:\\Program Files\\COLT\\colt.exe";
            }
        }
        if(!myState.nodePath.equals("") && new File(myState.nodePath).isDirectory()) {
            if(SystemInfo.isWindows) {
                myState.nodePath = new File(myState.nodePath, "node.exe").getPath();
            } else {
                myState.nodePath = new File(myState.nodePath, "node").getPath();
            }
        }
        if (myState.nodePath.equals("")) {
           if(SystemInfo.isMac) {
               myState.nodePath = "/usr/local/bin/node";
           }
        }
        //end fix
    }

    public boolean isEmpty() {
        return StringUtils.isEmpty(getSecurityToken());
    }

    public void invalidate() {
        setSecurityToken("");
    }

    @NotNull
    @Override
    public String getId() {
        return "ColtSettings";
    }

    @Nullable
    @Override
    public Runnable enableSearch(String s) {
        return null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "COLT";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public synchronized JComponent createComponent() {
        if (configurationPage == null) {
            configurationPage = new ColtConfigurationPage(this);
        }
        return configurationPage.getContentPane();
    }

    @Override
    public boolean isModified() {
        return configurationPage.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        configurationPage.apply();
    }

    @Override
    public void reset() {
        configurationPage.reset();
    }

    @Override
    public synchronized void disposeUIResources() {
        if (configurationPage != null) {
            configurationPage.dispose();
        }
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
        return "COLT Settings";
    }

    public boolean isColtPathValid() {
        return validateColtPath(getColtPath());
    }

    public String getColtPath() {
        return myState.coltPath;
    }

    public void setColtPath(String path) {
        myState.coltPath = path;
    }

    public String getNodePath() {
        return myState.nodePath;
    }

    public void setNodePath(String path) {
        myState.nodePath = path;
    }

    public String getNodeWebkitPath() {
        return myState.nodeWebkitPath;
    }

    public void setNodeWebkitPath(String path) {
        myState.nodeWebkitPath = path;
    }

    public String getSecurityToken() {
        return myState.securityToken;
    }

    public void setSecurityToken(String token) {
        myState.securityToken = token;
    }

    public boolean getAutoSaveEnabled() {
        return myState.autoSaveEnabled;
    }

    public void setAutoSaveEnabled(boolean value) {
        myState.autoSaveEnabled = value;
    }

    public static boolean validateColtPath(String coltPath) {
        if (StringUtils.isEmpty(coltPath)) {
            return false;
        }

        File coltLocation = new File(coltPath);
        if(SystemInfo.isWindows || SystemInfo.isLinux) {
            if(coltLocation.isDirectory()) {
                return new File(coltLocation, "colt.exe").exists() || (new File(coltLocation, "colt").exists() && new File(coltLocation, "colt").isFile());
            } else {
                return coltLocation.exists();
            }
        } else {
            if(coltPath.endsWith(".app")) {
                return coltLocation.exists() && coltLocation.isDirectory();
            } else {
                return new File(coltPath, "COLT.app").exists() || new File(coltPath, "colt.app").exists();
            }
        }
    }

    public static class State {

        public String securityToken = "a14d5455";
        public String coltPath = "";
        public String nodePath = "";
        public String nodeWebkitPath = "";
        public boolean autoSaveEnabled = true;

    }

}
