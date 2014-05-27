package codeOrchestra.colt.js.plugin.completion;

import codeOrchestra.colt.core.plugin.ColtSettings;
import codeOrchestra.colt.core.plugin.icons.Icons;
import codeOrchestra.colt.core.rpc.ColtRemoteServiceProvider;
import codeOrchestra.colt.js.rpc.ColtJsRemoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.injected.editor.EditorWindow;
import com.intellij.injected.editor.VirtualFileWindowImpl;
import com.intellij.openapi.application.AccessToken;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ProcessingContext;
import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Mikhail Tokarev
 * @author Dima Kruk
 */
public class ContextCompletionContributor extends CompletionContributor {

    private static final Logger logger = Logger.getInstance(ContextCompletionContributor.class);

    private static final PsiElementPattern.Capture<PsiElement> AFTER_DOT = PlatformPatterns.psiElement().afterLeaf(".");

    private ObjectMapper mapper = new ObjectMapper();

    private Project project;

    private boolean extendWasCalled = false;

    public ContextCompletionContributor() {

        extend(CompletionType.BASIC, AFTER_DOT, new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext processingContext, @NotNull CompletionResultSet result) {
                ColtRemoteServiceProvider remoteServiceProvider = project.getComponent(ColtRemoteServiceProvider.class);
                if (!remoteServiceProvider.isConnected() || !remoteServiceProvider.authorize() || !remoteServiceProvider.isLive()) {
                    return;
                }

                VirtualFile virtualFile = parameters.getOriginalFile().getVirtualFile(); // may return null if the PSI file exists only in memory
                if (virtualFile == null) {
                    return;
                }

                String filePath = virtualFile.getPath();
                int offset = parameters.getOffset();
                String currentState = parameters.getOriginalFile().getText();
                while (offset > 0 && currentState.charAt(offset) != '.') {
                    offset -= 1;
                }

                try {
                    synchronized (ColtRemoteServiceProvider.MONITOR) {
                        ColtJsRemoteService remoteService = remoteServiceProvider.getService();
                        String props = remoteService.getContextForPosition(
                                ColtSettings.getInstance().getSecurityToken(),
                                filePath,
                                offset,
                                currentState,
                                "PROPERTIES"
                        );

                        if (props != null) {
                            List<String> nodes = mapper.readValue(props, mapper.getTypeFactory().constructCollectionType(List.class, String.class));
                            for (String node : nodes) {
                                result.addElement(LookupElementBuilder.create(node).withIcon(Icons.COLT_ICON_16));
                            }
                        } else {
                            Editor baseEditor = parameters.getEditor();
                            if (baseEditor instanceof EditorWindow) {
                                Editor editor = ((EditorWindow) baseEditor).getDelegate();
                                filePath = ((VirtualFileWindowImpl) virtualFile).getDelegate().getPath();
                                offset = editor.getCaretModel().getOffset();
                                currentState = editor.getDocument().getText();

                                String enclosingTagId = remoteService.getEnclosingTagId(
                                        ColtSettings.getInstance().getSecurityToken(),
                                        filePath,
                                        offset,
                                        currentState
                                );

                                int TagId = Integer.parseInt(enclosingTagId);
                                String[] propsList = remoteService.angularExpressionCompletionAfterDot(
                                        ColtSettings.getInstance().getSecurityToken(),
                                        TagId,
                                        parameters.getOffset(),
                                        parameters.getOriginalFile().getText()
                                );
                                for (String node : propsList) {
                                    result.addElement(LookupElementBuilder.create(node).withIcon(Icons.COLT_ICON_16));
                                }
                                extendWasCalled = true;
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    logger.warn(e);
                }
            }
        });
    }

    @Override
    public void fillCompletionVariants(final CompletionParameters parameters, final CompletionResultSet result) {
        super.fillCompletionVariants(parameters, result);
        if(extendWasCalled) {
            extendWasCalled = false;
            return;
        }
        if(project == null) {
            return;
        }
        ColtRemoteServiceProvider remoteServiceProvider = project.getComponent(ColtRemoteServiceProvider.class);
        if (!remoteServiceProvider.isConnected() || !remoteServiceProvider.authorize() || !remoteServiceProvider.isLive()) {
            return;
        }

        VirtualFile virtualFile = parameters.getOriginalFile().getVirtualFile(); // may return null if the PSI file exists only in memory
        if (virtualFile == null) {
            return;
        }
        if("AngularJS".equals(getElementLanguage(parameters).getID())) {
            try {
                Editor editor = ((EditorWindow) parameters.getEditor()).getDelegate();
                String filePath = ((VirtualFileWindowImpl) virtualFile).getDelegate().getPath();
                int offset = editor.getCaretModel().getOffset();
                String currentState = editor.getDocument().getText();

                synchronized (ColtRemoteServiceProvider.MONITOR) {
                    ColtJsRemoteService remoteService = remoteServiceProvider.getService();
                    String enclosingTagId = remoteService.getEnclosingTagId(
                            ColtSettings.getInstance().getSecurityToken(),
                            filePath,
                            offset,
                            currentState
                    );

                    int TagId = Integer.parseInt(enclosingTagId);
                    String[] props = remoteService.angularExpressionCompletion(
                            ColtSettings.getInstance().getSecurityToken(),
                            TagId,
                            parameters.getOriginalFile().getText()
                    );
                    for (String node : props) {
                        result.addElement(LookupElementBuilder.create(node).withIcon(Icons.COLT_ICON_16));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                logger.warn(e);
            }
        }
    }

    @Override
    public void beforeCompletion(@NotNull CompletionInitializationContext context) {
        project = context.getProject();
    }

    private static Language getElementLanguage(final CompletionParameters parameters) {
        final AccessToken l = ReadAction.start();
        try {
            return PsiUtilCore.getLanguageAtOffset(parameters.getPosition().getContainingFile(), parameters.getOffset());
        } finally {
            l.finish();
        }
    }
}
