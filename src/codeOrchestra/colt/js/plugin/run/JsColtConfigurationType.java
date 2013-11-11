package codeOrchestra.colt.js.plugin.run;

import codeOrchestra.colt.core.plugin.icons.Icons;
import com.intellij.execution.configurations.ConfigurationTypeBase;

/**
 * @author Alexander Eliseyev
 */
public class JsColtConfigurationType extends ConfigurationTypeBase {

    public JsColtConfigurationType() {
        super("codeOrchestra.colt.js", "COLT JavaScript", "Start COLT Session", Icons.COLT_ICON_16);
        addFactory(new JsColtConfigurationFactory(this));
    }

}
