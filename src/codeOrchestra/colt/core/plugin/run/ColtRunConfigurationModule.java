package codeOrchestra.colt.core.plugin.run;

import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.openapi.project.Project;

/**
 * @author Alexander Eliseyev
 */
public class ColtRunConfigurationModule extends RunConfigurationModule {

    public ColtRunConfigurationModule(Project project) {
        super(project);
    }

}
