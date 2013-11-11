package codeOrchestra.colt.js.plugin.controller;

import codeOrchestra.colt.js.rpc.ColtJsRemoteService;
import com.intellij.openapi.project.Project;

/**
 * @author Alexander Eliseyev
 */
public final class JsColtPluginController {

    private JsColtPluginController() {
    }

    public static String export(Project project) {
        return export(project, project.getName(), null);
    }

    public static String export(Project project, String projectName, String mainDocumentPath) {
        // TODO: implement
        return null;
    }

    public static void runLive(ColtJsRemoteService service, Project project) {
        // TODO: implement
    }
}
