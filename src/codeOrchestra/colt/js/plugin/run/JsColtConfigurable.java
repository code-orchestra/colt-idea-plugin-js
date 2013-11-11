package codeOrchestra.colt.js.plugin.run;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Alexander Eliseyev
 */
public class JsColtConfigurable extends SettingsEditor<JsColtRunConfiguration> {

    private JPanel mainPanel;

    private Project project;
    private TextFieldWithBrowseButton coltProjectPathChooser;

    public JsColtConfigurable(Project project) {
        this.project = project;
        createUIComponents();
    }

    private void createUIComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel coltProjectPathPane = new JPanel();
        coltProjectPathPane.setLayout(new BorderLayout());

        LabeledComponent<JPanel> coltProjectPathEditor = new LabeledComponent<JPanel>();
        coltProjectPathChooser = new TextFieldWithBrowseButton();
        coltProjectPathChooser.addBrowseFolderListener(
                "COLT Project Path",
                "Specify the COLT project location",
                null,
                new FileChooserDescriptor(true, false, false, false, false, false) {
                    @Override
                    public boolean isFileSelectable(VirtualFile file) {
                        return file.getPath().toLowerCase().endsWith(".colt");
                    }
                },
                TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT,
                false);

        coltProjectPathPane.add(coltProjectPathChooser, BorderLayout.CENTER);
        JButton exportButton = new JButton("New");
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // TODO: implement

                /*
                String exportPath = JsColtPluginController.export(project);
                if (exportPath != null) {
                    coltProjectPathChooser.setText(exportPath);
                }
                */
            }
        });
        coltProjectPathPane.add(exportButton, BorderLayout.AFTER_LINE_ENDS);

        coltProjectPathEditor.setComponent(coltProjectPathPane);
        coltProjectPathEditor.setText("COLT Project Path");

        mainPanel.add(coltProjectPathEditor, BorderLayout.CENTER);
    }

    @Override
    protected void resetEditorFrom(JsColtRunConfiguration asColtRunConfiguration) {
        coltProjectPathChooser.setText(asColtRunConfiguration.getColtProjectPath());
    }

    @Override
    protected void applyEditorTo(JsColtRunConfiguration asColtRunConfiguration) throws ConfigurationException {
        asColtRunConfiguration.setColtProjectPath(coltProjectPathChooser.getText());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return mainPanel;
    }

    @Override
    protected void disposeEditor() {
    }

}
