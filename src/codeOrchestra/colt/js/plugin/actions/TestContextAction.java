package codeOrchestra.colt.js.plugin.actions;

import codeOrchestra.colt.core.plugin.ColtSettings;
import codeOrchestra.colt.core.plugin.actions.AbstractColtRemoteAction;
import codeOrchestra.colt.core.rpc.ColtRemoteTransferableException;
import codeOrchestra.colt.core.rpc.security.InvalidAuthTokenException;
import codeOrchestra.colt.js.rpc.ColtJsRemoteService;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.ex.EditorEx;

/**
 * @author Alexander Eliseyev
 */
public class TestContextAction extends AbstractColtRemoteAction<ColtJsRemoteService> {

    public TestContextAction() {
        super("Ask COLT for context");
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);

        Object editor = e.getDataContext().getData("editor");
        e.getPresentation().setEnabled(editor != null && editor instanceof EditorEx);
    }

    @Override
    protected void doRemoteAction(AnActionEvent event, ColtJsRemoteService coltRemoteService) throws InvalidAuthTokenException {
        EditorEx editor = (EditorEx) event.getDataContext().getData("editor");
        assert editor != null;

        String filePath = editor.getVirtualFile().getPath();
        int offset = editor.getCaretModel().getOffset();
        String currentState = editor.getDocument().getText();

        try {
            String contextForPosition = coltRemoteService.getContextForPosition(
                    ColtSettings.getInstance().getSecurityToken(),
                    filePath,
                    offset,
                    currentState
            );

            System.out.println(contextForPosition);
        } catch (ColtRemoteTransferableException e) {
            e.printStackTrace();
        }
    }

}
