package codeOrchestra.colt.js.plugin.actions;

import codeOrchestra.colt.core.plugin.ColtSettings;
import codeOrchestra.colt.core.plugin.icons.Icons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author Dima Kruk
 */
public class AutoSaveAction extends AnAction {

    private static ColtSettings coltSettings = ColtSettings.getInstance();

    @Override
    public void update(AnActionEvent e) {
        super.update(e);

        e.getPresentation().setIcon(Icons.COLT_ICON_16);

        coltSettings = ColtSettings.getInstance();
        if (coltSettings.getAutoSaveEnabled()) {
            e.getPresentation().setText("Disable Autosave");
        } else {
            e.getPresentation().setText("Enable Autosave");
        }
    }

    public void actionPerformed(AnActionEvent e) {
        System.out.println("coltSettings.getAutoSaveEnabled() = " + coltSettings.getAutoSaveEnabled());
        coltSettings.setAutoSaveEnabled(!coltSettings.getAutoSaveEnabled());
    }
}
