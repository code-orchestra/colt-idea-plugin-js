package codeOrchestra.colt.core.plugin.view;

import codeOrchestra.colt.core.plugin.ColtSettings;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.apache.commons.lang.ObjectUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexander Eliseyev
 */
public class ColtConfigurationPage {

    private ColtSettings coltSettings;

    private JPanel mainPanel;
    private TextFieldWithBrowseButton fileChooser;

    public ColtConfigurationPage(ColtSettings coltSettings) {
        this.coltSettings = coltSettings;
        this.mainPanel = new JPanel();

        mainPanel.setLayout(new GridLayoutManager(5, 1, new Insets(10, 10, 10, 10), -1, -1));

        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(new JLabel("Colt path:"), new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        mainPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));

        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        fileChooser = new TextFieldWithBrowseButton();
        fileChooser.addBrowseFolderListener(
                "COLT Installation Path",
                "Specify the COLT location",
                null,
                new FileChooserDescriptor(false, true, false, false, false, false),
                TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT,
                false);
        panel2.add(fileChooser, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        mainPanel.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));

        // Spacer
        mainPanel.add(new Spacer(), new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    private boolean isColtPathChanged() {
        return !ObjectUtils.equals(coltSettings.getColtPath(), fileChooser.getText());
    }

    public boolean isModified() {
        return isColtPathChanged();
    }

    public void apply() throws ConfigurationException {
        String coltPath = fileChooser.getText();

        if (!ColtSettings.validateColtPath(coltPath)) {
            throw new ConfigurationException("Invalid Colt location is specified");
        }

        coltSettings.setColtPath(coltPath);
    }

    public void reset() {
        fileChooser.setText(coltSettings.getColtPath());
    }

    public JPanel getContentPane() {
        return mainPanel;
    }

    public void dispose() {
        // TODO: implement
    }
}
