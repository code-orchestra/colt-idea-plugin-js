package codeOrchestra.colt.js.plugin.controller;

import codeOrchestra.colt.core.plugin.ColtSettings;
import codeOrchestra.colt.core.rpc.ColtRemoteServiceProvider;
import codeOrchestra.colt.core.rpc.ColtRemoteTransferableException;
import codeOrchestra.colt.core.rpc.model.ColtMessage;
import codeOrchestra.colt.core.rpc.security.InvalidAuthTokenException;
import codeOrchestra.colt.js.rpc.ColtJsRemoteService;
import codeOrchestra.colt.js.rpc.model.ColtJsRemoteProject;
import codeOrchestra.colt.js.rpc.model.codec.ColtJsRemoteProjectEncoder;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.wm.ex.StatusBarEx;
import org.jetbrains.annotations.NotNull;
import utils.XMLUtils;

import javax.xml.transform.TransformerException;
import java.io.File;

/**
 * @author Alexander Eliseyev
 */
public final class JsColtPluginController {

    private JsColtPluginController() {
    }

    public static String export(Project project, String mainDocumentPath) {
        return export(project, project.getName(), mainDocumentPath);
    }

    public static String export(Project project, String projectName, String mainDocumentPath) {
        ColtJsRemoteProject coltProject = new ColtJsRemoteProject();

        File baseDir = new File(project.getBasePath());

        coltProject.setMainDocument(mainDocumentPath);
        coltProject.setPath(new File(baseDir, projectName + ".colt").getPath());
        coltProject.setName(projectName);

        try {
            XMLUtils.saveToFile(coltProject.getPath(), new ColtJsRemoteProjectEncoder(coltProject).toDocument());
        } catch (TransformerException e) {
            throw new RuntimeException("Can't write COLT project file to " + coltProject.getPath(), e);
        }

        return coltProject.getPath();
    }

    public static void runLive(final ColtJsRemoteService coltRemoteService, final Project ideaProject, final AnActionEvent actionEvent) {
        // Report errors and warnings
        final ColtRemoteServiceProvider remoteServiceProvider = ideaProject.getComponent(ColtRemoteServiceProvider.class);

        if (!remoteServiceProvider.authorize()) {
            int result = Messages.showDialog("This plugin needs an authorization from the COLT application.", "COLT Connectivity", new String[]{
                    "Try again", "Cancel"
            }, 0, Messages.getWarningIcon());

            if (result == 0) {
                runLive(coltRemoteService, ideaProject, actionEvent);
            } else {
                return;
            }
        }

        try {
            coltRemoteService.checkAuth(ColtSettings.getInstance().getSecurityToken());
        } catch (InvalidAuthTokenException e) {
            ColtSettings.getInstance().invalidate();
            runLive(coltRemoteService, ideaProject, actionEvent);
        }

        new Task.Backgroundable(ideaProject, "COLT Build", false) {
            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    // ignore
                }
                ideaProject.getComponent(ColtRemoteServiceProvider.class).fireMessageAvailable("Starting Live Session");

                final IdeFrame ideFrame = WindowManager.getInstance().getIdeFrame(myProject);
                StatusBarEx statusBar = (StatusBarEx) ideFrame.getStatusBar();

                try {
                    coltRemoteService.startLive(ColtSettings.getInstance().getSecurityToken());
                    statusBar.notifyProgressByBalloon(MessageType.INFO, "Launching in live mode is successful");
                } catch (ColtRemoteTransferableException e) {
                    remoteServiceProvider.fireCompileMessageAvailable(new ColtMessage("Can't start Live Session with COLT: " + e.getMessage()));
                    statusBar.notifyProgressByBalloon(MessageType.ERROR, "Starting Live Session has failed, check the error messages");
                }
            }
        }.queue();
    }
}
