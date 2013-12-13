package codeOrchestra.colt.js.plugin.run;

import codeOrchestra.colt.core.plugin.run.ColtRemoteProcessHandler;
import codeOrchestra.colt.js.plugin.controller.JsColtPluginController;
import codeOrchestra.colt.js.rpc.ColtJsRemoteService;
import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Alexander Eliseyev
 */
public class JsColtRunProfileState implements RunProfileState {

    private Project project;

    public JsColtRunProfileState(Project project) {
        this.project = project;
    }

    @Nullable
    @Override
    public ExecutionResult execute(Executor executor, @NotNull ProgramRunner programRunner) throws ExecutionException {
        ConsoleViewImpl consoleView = new ConsoleViewImpl(project, false);
        ColtRemoteProcessHandler process = new ColtRemoteProcessHandler(project);
        consoleView.attachToProcess(process);

        ColtJsRemoteService service = process.getService();

        if (service == null) {
            throw new ExecutionException("Can't establish connection with COLT");
        }

        JsColtPluginController.runLive(service, project);
        return new DefaultExecutionResult(consoleView, process);
    }

}
