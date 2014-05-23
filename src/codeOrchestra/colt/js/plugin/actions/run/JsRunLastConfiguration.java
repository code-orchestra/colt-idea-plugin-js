package codeOrchestra.colt.js.plugin.actions.run;

import codeOrchestra.colt.core.plugin.icons.Icons;
import codeOrchestra.colt.js.plugin.ProjectSettings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;

/**
 * @author Dima Kruk
 */
public class JsRunLastConfiguration extends AnAction {
    @Override
    public void update(AnActionEvent e) {
        super.update(e);

        Presentation presentation = e.getPresentation();
        presentation.setIcon(Icons.COLT_ICON_16);

        Project project = e.getProject();

        if (project == null) {
            presentation.setEnabled(false);
        } else {
            ProjectSettings.State state = project.getComponent(ProjectSettings.class).getState();
            if (state != null
                    && state.runConfigurationName != null
                    && !"".equals(state.runConfigurationName)
                    && JsRunWithColt.findConfiguration(project, state.runConfigurationName) != null
                    ) {
                presentation.setEnabled(true);
            } else {
                presentation.setEnabled(false);
            }
        }

        presentation.setVisible(presentation.isEnabled());
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        if (project == null) {
            return;
        }
        ProjectSettings.State state = project.getComponent(ProjectSettings.class).getState();

        if(state != null) {
            JsRunWithColt.runExistsConfiguration(project, state.runConfigurationName);
        }

    }
}
