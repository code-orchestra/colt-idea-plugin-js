package codeOrchestra.colt.js;

import codeOrchestra.colt.core.ColtFacade;
import codeOrchestra.colt.core.rpc.ColtRemoteServiceProvider;
import codeOrchestra.colt.js.plugin.controller.JsColtPluginController;
import codeOrchestra.colt.js.rpc.ColtJsRemoteService;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;

/**
 * @author Alexander Eliseyev
 */
public class ColtJsFacade extends AbstractProjectComponent implements ColtFacade, ProjectComponent {

    public ColtJsFacade(Project project) {
        super(project);
    }

    @Override
    public String getRequestorCode() {
        return "WebStorm Plugin";
    }

    @Override
    public void startLive() {
        JsColtPluginController.runLive((ColtJsRemoteService) myProject.getComponent(ColtRemoteServiceProvider.class).getService(), myProject);
    }

    @Override
    public void startProduction() {
        JsColtPluginController.runProduction((ColtJsRemoteService) myProject.getComponent(ColtRemoteServiceProvider.class).getService(), myProject);
    }
}
