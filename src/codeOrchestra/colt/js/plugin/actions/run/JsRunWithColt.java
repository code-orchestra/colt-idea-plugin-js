package codeOrchestra.colt.js.plugin.actions.run;

import codeOrchestra.colt.core.ColtFacade;
import codeOrchestra.colt.core.plugin.icons.Icons;
import codeOrchestra.colt.core.rpc.ColtRemoteServiceProvider;
import codeOrchestra.colt.core.rpc.model.ColtLauncherType;
import codeOrchestra.colt.js.plugin.ProjectSettings;
import codeOrchestra.colt.js.plugin.controller.JsColtPluginController;
import codeOrchestra.colt.js.plugin.run.JsColtConfigurationFactory;
import codeOrchestra.colt.js.plugin.run.JsColtConfigurationType;
import codeOrchestra.colt.js.plugin.run.JsColtRunConfiguration;
import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunManagerEx;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author Dima Kruk
 */
public abstract class JsRunWithColt extends AnAction {

    protected ColtLauncherType myLauncherType;

    @Override
    public void update(AnActionEvent e) {
        super.update(e);

        e.getPresentation().setIcon(Icons.COLT_ICON_16);

        if (e.getProject() == null) {
            e.getPresentation().setEnabled(false);
        }

        VirtualFile[] virtualFileArray = (VirtualFile[]) e.getDataContext().getData("virtualFileArray");
        if (virtualFileArray != null && virtualFileArray.length == 1 && !virtualFileArray[0].isDirectory()) {
            e.getPresentation().setEnabled(true);
        } else {
            e.getPresentation().setEnabled(false);
        }
    }

    protected String exportProject(Project project, String mainDocumentName, String mainDocumentPath) {
        return JsColtPluginController.export(project, mainDocumentName, mainDocumentPath, myLauncherType);
    }

    protected RunnerAndConfigurationSettings findConfiguration(Project project, String runConfigurationName) {
        RunManager runManager = RunManager.getInstance(project);
        for (RunnerAndConfigurationSettings runnerAndConfigurationSettings : runManager.getConfigurationSettingsList(getColtConfigurationType(runManager))) {
            if (runnerAndConfigurationSettings.getName().equals(runConfigurationName)) {
                return runnerAndConfigurationSettings;
            }
        }
        return null;
    }

    protected boolean runExistsConfiguration (Project project, String runConfigurationName) {
        RunnerAndConfigurationSettings runnerAndConfigurationSettings = findConfiguration(project, runConfigurationName);
        if(runnerAndConfigurationSettings != null && runnerAndConfigurationSettings.getConfiguration() instanceof JsColtRunConfiguration) {
            ProgramRunnerUtil.executeConfiguration(project, runnerAndConfigurationSettings, DefaultRunExecutor.getRunExecutorInstance());
            return true;
        } else {
            return false;
        }
    }

    protected void createAndRunConfiguration(Project project, String runConfigurationName, String projectPath) {
        RunManager runManager = RunManager.getInstance(project);
        JsColtConfigurationType coltConfigurationType = getColtConfigurationType(runManager);
        if (coltConfigurationType == null) {
            throw new IllegalStateException("Can't locate COLT configuration type");
        }
        JsColtConfigurationFactory coltConfigurationFactory = null;
        for (ConfigurationFactory configurationFactory : coltConfigurationType.getConfigurationFactories()) {
            if (configurationFactory instanceof JsColtConfigurationFactory) {
                coltConfigurationFactory = (JsColtConfigurationFactory) configurationFactory;
                break;
            }
        }
        if (coltConfigurationFactory == null) {
            throw new IllegalStateException("Can't locate COLT configuration factory");
        }
        RunnerAndConfigurationSettings runConfiguration = runManager.createRunConfiguration(runConfigurationName, coltConfigurationFactory);
        JsColtRunConfiguration JsColtRunConfiguration = (JsColtRunConfiguration) runConfiguration.getConfiguration();
        JsColtRunConfiguration.setColtProjectPath(projectPath);
        if (runManager instanceof RunManagerEx) {
            runManager.addConfiguration(runConfiguration, false);
            runManager.setSelectedConfiguration(runConfiguration);
        }

        // run configuration
        ProgramRunnerUtil.executeConfiguration(project, runConfiguration, DefaultRunExecutor.getRunExecutorInstance());
    }

    protected void runWithColt(AnActionEvent actionEvent, String mainDocumentPath, String mainDocumentName, String runConfigurationName) {
        Project project = actionEvent.getProject();
        if(project == null) {
            return;
        }

        // export project
        String projectPath = exportProject(project, mainDocumentName, mainDocumentPath);

        ProjectSettings.State state = project.getComponent(ProjectSettings.class).getState();
        if (project.getComponent(ColtRemoteServiceProvider.class).isConnected() && state != null && state.lastLauncherType == myLauncherType) {
            project.getComponent(ColtFacade.class).startLive();
            return;
        }

        // check if such configuration already exists
        RunnerAndConfigurationSettings runnerAndConfigurationSettings = findConfiguration(project, runConfigurationName);
        if(runnerAndConfigurationSettings != null && runnerAndConfigurationSettings.getConfiguration() instanceof JsColtRunConfiguration) {
            ProgramRunnerUtil.executeConfiguration(project, runnerAndConfigurationSettings, DefaultRunExecutor.getRunExecutorInstance());
        } else {
            // create configuration and run
            if (runnerAndConfigurationSettings != null) {
                createAndRunConfiguration(project, runConfigurationName + " (1)", projectPath);
            } else {
                createAndRunConfiguration(project, runConfigurationName, projectPath);
            }

        }
    }



    protected JsColtConfigurationType getColtConfigurationType(RunManager runManager) {
        JsColtConfigurationType coltConfigurationType = null;
        for (ConfigurationType configurationType : runManager.getConfigurationFactories()) {
            if (configurationType instanceof JsColtConfigurationType) {
                coltConfigurationType = (JsColtConfigurationType) configurationType;
                break;
            }
        }
        return coltConfigurationType;
    }
}
