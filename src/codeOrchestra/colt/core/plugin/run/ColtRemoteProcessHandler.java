package codeOrchestra.colt.core.plugin.run;

import codeOrchestra.colt.core.rpc.ColtRemoteService;
import codeOrchestra.colt.core.rpc.ColtRemoteServiceListener;
import codeOrchestra.colt.core.rpc.ColtRemoteServiceProvider;
import codeOrchestra.colt.core.rpc.model.ColtMessage;
import codeOrchestra.colt.core.rpc.model.ColtState;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

import java.io.OutputStream;

/**
 * @author Alexander Eliseyev
 */
public class ColtRemoteProcessHandler extends ProcessHandler implements ColtRemoteServiceListener {

    private final ColtRemoteServiceProvider remoteServiceProvider;

    public ColtRemoteProcessHandler(Project project) {

        remoteServiceProvider = project.getComponent(ColtRemoteServiceProvider.class);
        remoteServiceProvider.addListener(this);
    }

    @Override
    public void startNotify() {
        super.startNotify();
        notifyTextAvailable("Established a connection with COLT running project " + remoteServiceProvider.getService().getState().getProjectName() + "\n", ProcessOutputTypes.SYSTEM);
    }

    public ColtRemoteServiceProvider getRemoteServiceProvider() {
        return remoteServiceProvider;
    }

    @Override
    public void onMessage(ColtMessage coltCompilerMessage) {
        if ("Info".equals(coltCompilerMessage.getType())) {
            notifyTextAvailable(coltCompilerMessage.getFullMessage(), ProcessOutputTypes.SYSTEM);
        } else {
            notifyTextAvailable(coltCompilerMessage.getFullMessage(), coltCompilerMessage.getType().equals("Error") ? ProcessOutputTypes.STDERR : ProcessOutputTypes.STDOUT);
        }
    }

    @Override
    protected void destroyProcessImpl() {
        doDetach();
    }

    @Override
    protected void detachProcessImpl() {
        doDetach();
    }

    private void doDetach() {
        remoteServiceProvider.disconnect();
    }

    @Override
    public boolean detachIsDefault() {
        return true;
    }

    @Nullable
    @Override
    public OutputStream getProcessInput() {
        return null;
    }

    @Override
    public void onDisconnected() {
        notifyTextAvailable("\nDisconnected", ProcessOutputTypes.SYSTEM);
        notifyProcessDetached();
    }

    @Override
    public void onConnected() {
    }

    @Override
    public void onStateUpdate(ColtState state) {
    }

    public <S extends ColtRemoteService> S getService() {
        if (remoteServiceProvider == null) {
            return null;
        }
        return remoteServiceProvider.getService();
    }
}
