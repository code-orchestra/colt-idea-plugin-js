package codeOrchestra.colt.js.plugin.actions;

import codeOrchestra.colt.core.plugin.ColtSettings;
import codeOrchestra.colt.core.plugin.actions.AbstractColtRemoteAction;
import codeOrchestra.colt.core.plugin.icons.Icons;
import codeOrchestra.colt.core.rpc.ColtRemoteServiceProvider;
import codeOrchestra.colt.core.rpc.ColtRemoteTransferableException;
import codeOrchestra.colt.core.rpc.security.InvalidAuthTokenException;
import codeOrchestra.colt.js.rpc.ColtJsRemoteService;
import com.intellij.codeInsight.hint.HintManager;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
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
        String selection = editor.getSelectionModel().getSelectedText();
        CharSequence charsSequence = editor.getDocument().getCharsSequence();
        while(offset < charsSequence.length()) {
            char c = charsSequence.charAt(offset);
            if(c == '.' || c == '(' || c == ' ' || c == ';' || c == ',' || c == ')' || c == '=' || c == '\t' || c == '\r' || c == '\n') {
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

            if(contextForPosition != null) {
                String value = contextForPosition.substring(contextForPosition.indexOf(":") + 2);
                if (value.length() > 150) {
                    value = value.substring(0, 150);
                    value += "...";
                }
                HintManager.getInstance().showInformationHint(editor, value);
                ideaProject.getComponent(ColtRemoteServiceProvider.class).fireMessageAvailable(contextForPosition);
            } else {
                HintManager.getInstance().showErrorHint(editor, "Value not found");
            }
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
