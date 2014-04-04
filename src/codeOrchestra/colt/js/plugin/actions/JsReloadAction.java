package codeOrchestra.colt.js.plugin.actions;

import codeOrchestra.colt.core.plugin.ColtSettings;
import codeOrchestra.colt.core.plugin.actions.AbstractColtRemoteAction;
import codeOrchestra.colt.core.plugin.icons.Icons;
import codeOrchestra.colt.core.rpc.ColtRemoteServiceProvider;
import codeOrchestra.colt.core.rpc.ColtRemoteTransferableException;
import codeOrchestra.colt.core.rpc.security.InvalidAuthTokenException;
import codeOrchestra.colt.js.rpc.ColtJsRemoteService;
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
            e.getPresentation().setEnabled(editor != null && editor instanceof EditorEx && project.getComponent(ColtRemoteServiceProvider.class).isLive());
        } else {
            e.getPresentation().setEnabled(false);
        }
    }

    @Override
    protected void doRemoteAction(AnActionEvent event, ColtJsRemoteService coltRemoteService) throws InvalidAuthTokenException {
        EditorEx editor = (EditorEx) event.getDataContext().getData("editor");
        assert editor != null;

        try {
            coltRemoteService.reload(ColtSettings.getInstance().getSecurityToken());
        } catch (ColtRemoteTransferableException e) {
            e.printStackTrace();
        }
    }

}
