package utils;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.EventObject;

/**
 * @author Alexander Eliseyev
 */
public final class EventUtils {

    public static AnActionEvent cloneEvent(AnActionEvent event) {
        Presentation presentation = event.getPresentation().clone();
        InputEvent inputEvent = createFakeInputEvent(event);
        return new AnActionEvent(inputEvent, event.getDataContext(), event.getPlace(), presentation, event.getActionManager(), 0);
    }

    public static KeyEvent createFakeInputEvent(AnActionEvent event) {
        return new KeyEvent(event.getInputEvent().getComponent(), 0, System.currentTimeMillis(), 0, KeyEvent.VK_0, '0');
    }

    public static KeyEvent createFakeInputEvent(EventObject event) {
        return new KeyEvent((Component) event.getSource(), 0, System.currentTimeMillis(), 0, KeyEvent.VK_0, '0');
    }


}
