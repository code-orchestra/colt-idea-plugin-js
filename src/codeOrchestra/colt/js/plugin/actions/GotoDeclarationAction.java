package codeOrchestra.colt.js.plugin.actions;

import codeOrchestra.colt.core.plugin.ColtSettings;
import codeOrchestra.colt.core.plugin.actions.AbstractColtRemoteAction;
import codeOrchestra.colt.core.plugin.icons.Icons;
import codeOrchestra.colt.core.rpc.ColtRemoteServiceProvider;
import codeOrchestra.colt.core.rpc.ColtRemoteTransferableException;
import codeOrchestra.colt.core.rpc.security.InvalidAuthTokenException;
import codeOrchestra.colt.js.rpc.ColtJsRemoteService;
import codeOrchestra.colt.js.rpc.model.jsScript.ScriptPoint;
import com.intellij.codeInsight.hint.HintManager;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.editor.ScrollingModel;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author Dima Kruk
 */
public class GotoDeclarationAction extends AbstractColtRemoteAction<ColtJsRemoteService> {

    public GotoDeclarationAction() {
        super("Go to DeclarationAction");
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);

        e.getPresentation().setIcon(Icons.COLT_ICON_16);

        Project project = e.getProject();
        if (project != null) {
            Object editor = e.getDataContext().getData("editor");
            e.getPresentation().setEnabled(editor != null
                    && editor instanceof EditorEx
                    && ((EditorEx) editor).getVirtualFile() != null);
        } else {
            e.getPresentation().setEnabled(false);
        }
    }

    @Override
    protected void doRemoteAction(AnActionEvent event, ColtJsRemoteService coltRemoteService) throws InvalidAuthTokenException {
        Project project = event.getProject();
        if(project == null) {
            return;
        }
        if(!project.getComponent(ColtRemoteServiceProvider.class).isLive()) {
            Notifications.Bus.notify(new Notification("colt.notification", "COLT", "To run this action you need active 'live' session.", NotificationType.ERROR));
            return;
        }

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
            ScriptPoint declarationPosition = coltRemoteService.getDeclarationPosition(
                    ColtSettings.getInstance().getSecurityToken(),
                    filePath,
                    offset,
                    currentState
            );

            if (declarationPosition != null) {
                VirtualFile fileByPath = LocalFileSystem.getInstance().findFileByPath(declarationPosition.filePath);
                if (fileByPath != null) {
                    FileEditorManager.getInstance(project).openFile(fileByPath, true);
                    Editor selectedEditor = FileEditorManager.getInstance(project).getSelectedTextEditor();
                    if (selectedEditor != null) {
                        CaretModel caretModel = selectedEditor.getCaretModel();
                        caretModel.moveToOffset(declarationPosition.position, true);

                        //Scroll to the caret
                        ScrollingModel scrollingModel = editor.getScrollingModel();
                        scrollingModel.scrollToCaret(ScrollType.CENTER);
                    }
                }
            } else {
                HintManager.getInstance().showErrorHint(editor, "Cannot find declaration to go to");
            }

        } catch (ColtRemoteTransferableException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
