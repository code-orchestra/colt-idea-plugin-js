package codeOrchestra.colt.js.plugin.run;

import com.intellij.execution.BeforeRunTask;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;

/**
 * @author Alexander Eliseyev
 */
public class JsColtConfigurationFactory extends ConfigurationFactory {

    public JsColtConfigurationFactory(@NotNull ConfigurationType type) {
        super(type);
    }

    @Override
    public RunConfiguration createTemplateConfiguration(Project project) {
        return new JsColtRunConfiguration("", project, JsColtConfigurationFactory.this);
    }

    @Override
    public String getName() {
        return "COLT ActionScript";
    }

    public void configureBeforeRunTaskDefaults(Key<? extends BeforeRunTask> providerID, BeforeRunTask task) {
        if ("Make".equals(providerID.toString())) {
            task.setEnabled(false);
        }
    }

}
