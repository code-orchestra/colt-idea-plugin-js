package codeOrchestra.colt.core.rpc.discovery;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import codeOrchestra.colt.core.rpc.ColtRemoteService;

/**
 * @author Alexander Eliseyev
 */
public abstract class AbstractColtServiceLocator extends AbstractProjectComponent implements ProjectComponent, ColtServiceLocator {

    public static final int COLT_SERVICE_LOOKOUT_TIMEOUT = 20000;
    public static final int SERVICE_AVAILABILITY_CHECK_PERIOD = 300;

    protected AbstractColtServiceLocator(Project project) {
        super(project);
    }

    @Override
    public <S extends ColtRemoteService> S waitForService(Class<S> serviceClass, String projectPath, String name) throws ProcessCanceledException {
        long timeout = COLT_SERVICE_LOOKOUT_TIMEOUT;
        while (timeout > 0) {
            ProgressManager.checkCanceled();

            try {
                Thread.sleep(SERVICE_AVAILABILITY_CHECK_PERIOD);
            } catch (InterruptedException e) {
                // ignore
            }

            timeout -= SERVICE_AVAILABILITY_CHECK_PERIOD;

            S service = locateService(serviceClass, projectPath, name);
            if (service != null) {
                return service;
            }
        }

        return null;
    }
}
