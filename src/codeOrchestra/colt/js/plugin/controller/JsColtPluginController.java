package codeOrchestra.colt.js.plugin.controller;

import codeOrchestra.colt.js.rpc.ColtJsRemoteService;
import codeOrchestra.colt.js.rpc.model.ColtJsRemoteProject;
import codeOrchestra.colt.js.rpc.model.codec.ColtJsRemoteProjectEncoder;
import com.intellij.openapi.project.Project;
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

    public static void runLive(ColtJsRemoteService service, Project project) {
        // TODO: implement
    }
}
