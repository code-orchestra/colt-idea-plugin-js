package codeOrchestra.colt.js;

import codeOrchestra.colt.core.ColtFacade;
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
    public void startLive() {
        // TODO: implement
    }

}
