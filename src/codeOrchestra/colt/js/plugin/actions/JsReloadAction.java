package codeOrchestra.colt.js.plugin.actions;

import codeOrchestra.colt.core.plugin.ColtSettings;
import codeOrchestra.colt.core.plugin.actions.AbstractColtRemoteAction;
import codeOrchestra.colt.core.plugin.icons.Icons;
import codeOrchestra.colt.core.rpc.ColtRemoteServiceProvider;
import codeOrchestra.colt.core.rpc.ColtRemoteTransferableException;
import codeOrchestra.colt.core.rpc.model.ColtLauncherType;
import codeOrchestra.colt.core.rpc.security.InvalidAuthTokenException;
import codeOrchestra.colt.js.plugin.controller.JsColtPluginController;
import codeOrchestra.colt.js.rpc.ColtJsRemoteService;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.project.Project;

/**
 * @author Alexander Eliseyev
 */
public class JsReloadAction extends AbstractColtRemoteAction<ColtJsRemoteService> {

    public JsReloadAction() {
        super("Ask COLT for reload");
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);

        e.getPresentation().setIcon(Icons.COLT_ICON_16);

        Project project = e.getProject();
        if (project != null) {
            Object editor = e.getDataContext().getData("editor");
            e.getPresentation().setEnabled(editor != null
                    && editor instanceof EditorEx);
        } else {
            e.getPresentation().setEnabled(false);
        }
    }

    @Override
    protected void doRemoteAction(AnActionEvent event, ColtJsRemoteService coltRemoteService) throws InvalidAuthTokenException {
        Project project = event.getProject();
        if(project == null) {
            return;
        }
        if(!project.getComponent(ColtRemoteServiceProvider.class).isLive()) {
            Notifications.Bus.notify(new Notification("colt.notification", "COLT", "To run this action you need active 'live' session.", NotificationType.ERROR));
            return;
        }
        if (JsColtPluginController.lastLauncherType == ColtLauncherType.NODE_JS) {
            Notifications.Bus.notify(new Notification("colt.notification", "COLT", "Action 'Reload in COLT' isn't available for node.js projects.", NotificationType.ERROR));
            return;
        }

        EditorEx editor = (EditorEx) event.getDataContext().getData("editor");
        assert editor != null;

        try {
            coltRemoteService.reload(ColtSettings.getInstance().getSecurityToken());
        } catch (ColtRemoteTransferableException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
