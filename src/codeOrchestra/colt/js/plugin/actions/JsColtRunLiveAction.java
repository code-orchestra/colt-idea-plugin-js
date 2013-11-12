package codeOrchestra.colt.js.plugin.actions;

import codeOrchestra.colt.core.plugin.actions.AbstractColtRemoteAction;
import codeOrchestra.colt.core.rpc.security.InvalidAuthTokenException;
import codeOrchestra.colt.js.plugin.controller.JsColtPluginController;
import codeOrchestra.colt.js.rpc.ColtJsRemoteService;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author Alexander Eliseyev
 */
public class JsColtRunLiveAction extends AbstractColtRemoteAction<ColtJsRemoteService> {

    public JsColtRunLiveAction() {
        super("Start Live Session");
    }

    @Override
    protected void doRemoteAction(AnActionEvent event, ColtJsRemoteService coltRemoteService) throws InvalidAuthTokenException {
        JsColtPluginController.runLive(coltRemoteService, event.getProject(), event);
    }
}
