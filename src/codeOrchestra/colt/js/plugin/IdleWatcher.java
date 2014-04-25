package codeOrchestra.colt.js.plugin;

import codeOrchestra.colt.core.plugin.ColtSettings;
import codeOrchestra.colt.core.plugin.icons.Icons;
import codeOrchestra.colt.core.rpc.ColtRemoteServiceListener;
import codeOrchestra.colt.core.rpc.ColtRemoteServiceProvider;
import codeOrchestra.colt.core.rpc.ColtRemoteTransferableException;
import codeOrchestra.colt.core.rpc.model.ColtMessage;
import codeOrchestra.colt.core.rpc.model.ColtState;
import codeOrchestra.colt.js.plugin.editor.CountGutterController;
import codeOrchestra.colt.js.rpc.ColtJsRemoteService;
import codeOrchestra.colt.js.rpc.model.jsScript.MethodCount;
import codeOrchestra.colt.js.rpc.model.jsScript.RuntimeErrorInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dima Kruk
 */
public class IdleWatcher extends AbstractProjectComponent implements ProjectComponent, ColtRemoteServiceListener {
    private static final int SLEEP_TIME = 1000;

    protected boolean isRunning = false;
    private ColtRemoteServiceProvider coltRemoteServiceProvider;
    private ColtJsRemoteService coltRemoteService;

    private IdleWatcherThread watcherThread;

    private final Map<String, Map<String, MethodCount>> methodCountsMap;

    private final CountGutterController countGutterController;

    private RuntimeErrorInfo lastRuntimeError;
    private Editor errorEditor;
    private RangeHighlighter errorHighlighter;

    private final EditorFactoryListener editorFactoryListener;

    protected IdleWatcher(Project project) {
        super(project);
        coltRemoteServiceProvider = project.getComponent(ColtRemoteServiceProvider.class);
        coltRemoteServiceProvider.addListener(this);

        methodCountsMap = new HashMap<String, Map<String, MethodCount>>();

        countGutterController = new CountGutterController();

        editorFactoryListener = new CustomEditorFactoryListener();
    }



    @Override
    public void onConnected() {
        coltRemoteService = coltRemoteServiceProvider.getService();
        if(watcherThread == null) {
            watcherThread = new IdleWatcherThread();
            watcherThread.start();
            EditorFactory.getInstance().addEditorFactoryListener(editorFactoryListener);
        }
    }

    @Override
    public void onStateUpdate(ColtState state) {
        isRunning = !(state.getActiveConnections() == null || state.getActiveConnections().length == 0);
    }

    @Override
    public void onMessage(ColtMessage coltCompilerMessage) {

    }

    @Override
    public void onDisconnected() {
        coltRemoteService = null;
        isRunning = false;
        if(watcherThread != null) {
            watcherThread.stopRightTHere();
        }
        watcherThread = null;
        EditorFactory.getInstance().removeEditorFactoryListener(editorFactoryListener);
    }

    private void reset() {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                clearAllHighlighter();
                for (Map<String, MethodCount> map:methodCountsMap.values()) {
                    map.clear();
                }
                methodCountsMap.clear();
            }
        });
    }

    private void update() {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    clearAllHighlighter();
                    EditorEx editor = (EditorEx)FileEditorManagerEx.getInstance(myProject).getSelectedTextEditor();
                    if (editor != null) {
                        update(editor);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void update(final EditorEx editor) {
        VirtualFile virtualFile = editor.getVirtualFile();
        if(virtualFile == null) {
            return;
        }
        String path = virtualFile.getPath();
        if(SystemInfo.isWindows) {
            path = path.replace("/", "\\");
        }

        synchronized (methodCountsMap) {
            for (String key: methodCountsMap.keySet()) {
                if (key.equals(path)) {
                    Map<String, MethodCount> integerIntegerMap = methodCountsMap.get(key);
                    for(MethodCount methodCount: integerIntegerMap.values()) {
                        try{
                            int line = editor.offsetToLogicalPosition(methodCount.position).line;
                            countGutterController.add(editor, line, methodCount.count);
                        } catch (Exception e) {
                            //wrong data from COLT
                            //ignore
                        }
                    }
                }
            }
        }

        if (lastRuntimeError != null && lastRuntimeError.filePath != null) {
            String filePath = lastRuntimeError.filePath;
            if(SystemInfo.isWindows) {
                filePath = filePath.replace("/", "\\");
            }
            if (path.equals(filePath)) {
                try{
                    int line = editor.offsetToLogicalPosition(lastRuntimeError.position).line;
                    errorEditor = editor;
                    errorHighlighter = editor.getMarkupModel().addLineHighlighter(line, HighlighterLayer.FIRST - 1, null);
                    errorHighlighter.setGutterIconRenderer(new ErrorGutterIconRenderer(lastRuntimeError.errorMessage));
                } catch (Exception e) {
                    //wrong data from COLT
                    //ignore
                }
            }

        }
    }

    private void clearAllHighlighter() {
        try {
            countGutterController.clear();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (errorEditor != null) {
            errorEditor.getMarkupModel().removeHighlighter(errorHighlighter);
            errorEditor = null;
            errorHighlighter = null;
        }

    }

    private class CustomEditorFactoryListener implements EditorFactoryListener {

        @Override
        public void editorCreated(@NotNull EditorFactoryEvent editorFactoryEvent) {
            update((EditorEx) editorFactoryEvent.getEditor());
        }

        @Override
        public void editorReleased(@NotNull EditorFactoryEvent editorFactoryEvent) {
            System.out.println("editorReleased");
        }
    }

    private class ErrorGutterIconRenderer extends GutterIconRenderer {

        private String message;

        ErrorGutterIconRenderer(String message) {
            this.message = message;
        }

        @NotNull
        @Override
        public Icon getIcon() {
            return Icons.ERROR;
        }

        @Nullable
        @Override
        public String getTooltipText() {
            return message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }

            ErrorGutterIconRenderer that = (ErrorGutterIconRenderer) o;
            return that.message.equals(message);
        }

        @Override
        public int hashCode() {
            return message.hashCode();
        }
    }

    private class IdleWatcherThread extends Thread {

        private boolean mustStop;

        public void stopRightTHere() {
            mustStop = true;
        }

        @Override
        public void run() {
            mustStop = false;
            while (!mustStop) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    // ignore
                }

                if (isRunning && coltRemoteService != null) {
                    try {
                        lastRuntimeError = coltRemoteService.getLastRuntimeError(ColtSettings.getInstance().getSecurityToken());
                    } catch (Throwable e) {
                        lastRuntimeError = null;
                        //ignore
                    }
                    try {
                        ArrayList<MethodCount> methodCounts = coltRemoteService.getMethodCounts(ColtSettings.getInstance().getSecurityToken());
                        synchronized (methodCountsMap) {
                            for(Map<String, MethodCount> value: methodCountsMap.values()) {
                                value.clear();
                            }
                            methodCountsMap.clear();
                            for (MethodCount methodCount: methodCounts) {
                                Map<String, MethodCount> integerIntegerMap = methodCountsMap.get(methodCount.filePath);
                                if(integerIntegerMap == null) {
                                    integerIntegerMap = new HashMap<String, MethodCount>();
                                    methodCountsMap.put(methodCount.filePath, integerIntegerMap);
                                }
                                integerIntegerMap.put(methodCount.methodId, methodCount);
                            }
                        }
                        if(methodCountsMap.size() > 0) {
                            update();
                        }
                    } catch (ColtRemoteTransferableException e) {
                        e.printStackTrace();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                } else {
                    reset();
                }
            }
        }
    }
}
