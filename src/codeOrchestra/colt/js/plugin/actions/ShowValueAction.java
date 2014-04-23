package codeOrchestra.colt.js.plugin.actions;

import codeOrchestra.colt.core.plugin.ColtSettings;
import codeOrchestra.colt.core.plugin.actions.AbstractColtRemoteAction;
import codeOrchestra.colt.core.plugin.icons.Icons;
import codeOrchestra.colt.core.rpc.ColtRemoteServiceProvider;
import codeOrchestra.colt.core.rpc.ColtRemoteTransferableException;
import codeOrchestra.colt.core.rpc.security.InvalidAuthTokenException;
import codeOrchestra.colt.js.rpc.ColtJsRemoteService;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.project.Project;

import java.awt.*;
import java.util.regex.Pattern;

/**
 * @author Alexander Eliseyev
 */
public class ShowValueAction extends AbstractColtRemoteAction<ColtJsRemoteService> {

    public ShowValueAction() {
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
        String selection = editor.getSelectionModel().getSelectedText();
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
            String contextForPosition = coltRemoteService.evaluateExpression(
                    ColtSettings.getInstance().getSecurityToken(),
                    filePath,
                    selection,
                    offset,
                    currentState
            );

//            JLabel jLabel = new JLabel("<html>" + contextForPosition + "</html>");
//            jLabel.setMaximumSize(new Dimension(300, 100));
//            HintManager.getInstance().showHint(jLabel, new RelativePoint(getPopupLocation(editor)), HintManager.HIDE_BY_ANY_KEY, 0);

//            JComponent informationLabel = HintUtil.createInformationLabel(contextForPosition);
//            HintManager.getInstance().showHint(informationLabel, new RelativePoint(getPopupLocation(editor)), HintManager.HIDE_BY_ANY_KEY, 0);

            ideaProject.getComponent(ColtRemoteServiceProvider.class).fireMessageAvailable(contextForPosition);
        } catch (ColtRemoteTransferableException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private Point getPopupLocation(EditorEx editor) {
        final Point cursorAbsoluteLocation = editor.visualPositionToXY(editor.getCaretModel().getVisualPosition());
        final Point editorLocation= editor.getComponent().getLocationOnScreen();
        final Point editorContentLocation = editor.getContentComponent().getLocationOnScreen();
        final Point popupLocation = new Point(editorContentLocation.x + cursorAbsoluteLocation.x ,
                editorLocation.y + cursorAbsoluteLocation.y - editor.getScrollingModel().getVerticalScrollOffset());

        return popupLocation;
    }

}
