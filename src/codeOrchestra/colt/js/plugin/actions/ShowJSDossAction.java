package codeOrchestra.colt.js.plugin.actions;

import codeOrchestra.colt.core.plugin.ColtSettings;
import codeOrchestra.colt.core.plugin.actions.AbstractColtRemoteAction;
import codeOrchestra.colt.core.plugin.icons.Icons;
import codeOrchestra.colt.core.rpc.ColtRemoteServiceProvider;
import codeOrchestra.colt.core.rpc.ColtRemoteTransferableException;
import codeOrchestra.colt.core.rpc.security.InvalidAuthTokenException;
import codeOrchestra.colt.js.rpc.ColtJsRemoteService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.project.Project;

/**
 * @author Dima Kruk
 */
public class ShowJSDossAction extends AbstractColtRemoteAction<ColtJsRemoteService> {

    public ShowJSDossAction() {
        super("Ask COLT for value");
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);

        e.getPresentation().setIcon(Icons.COLT_ICON_16);

        Project project = e.getProject();
        if (project != null) {
            Object editor = e.getDataContext().getData("editor");
            e.getPresentation().setEnabled(editor != null && editor instanceof EditorEx && ((EditorEx) editor).getVirtualFile() != null && project.getComponent(ColtRemoteServiceProvider.class).isLive());
        } else {
            e.getPresentation().setEnabled(false);
        }
    }

    @Override
    protected void doRemoteAction(AnActionEvent event, ColtJsRemoteService coltRemoteService) throws InvalidAuthTokenException {
        EditorEx editor = (EditorEx) event.getDataContext().getData("editor");
        assert editor != null;

        String filePath = editor.getVirtualFile().getPath();
        int offset = editor.getCaretModel().getOffset();
        CharSequence charsSequence = editor.getDocument().getCharsSequence();
        while(offset < charsSequence.length()) {
            char c = charsSequence.charAt(offset);
            if(c == '.' || c == '(' || c == ' ' || c == ';' || c == ',' || c == ')' || c == '\t' || c == '\r' || c == '\n') {
                break;
            }
            offset++;
        }
        String currentState = editor.getDocument().getText();

        try {
            coltRemoteService.findAndShowJavaDocs(
                    ColtSettings.getInstance().getSecurityToken(),
                    filePath,
                    offset,
                    currentState
            );
        } catch (ColtRemoteTransferableException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
