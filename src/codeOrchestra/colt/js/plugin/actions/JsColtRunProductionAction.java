package codeOrchestra.colt.js.plugin.actions;

import codeOrchestra.colt.core.plugin.actions.AbstractColtRemoteAction;
import codeOrchestra.colt.core.rpc.security.InvalidAuthTokenException;
import codeOrchestra.colt.js.plugin.controller.JsColtPluginController;
import codeOrchestra.colt.js.rpc.ColtJsRemoteService;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author Alexander Eliseyev
 */
public class JsColtRunProductionAction extends AbstractColtRemoteAction<ColtJsRemoteService> {

    public JsColtRunProductionAction() {
        super("Run Production Build");
    }

    @Override
    protected void doRemoteAction(AnActionEvent event, ColtJsRemoteService coltRemoteService) throws InvalidAuthTokenException {
        JsColtPluginController.runProduction(coltRemoteService, event.getProject());
    }
}
