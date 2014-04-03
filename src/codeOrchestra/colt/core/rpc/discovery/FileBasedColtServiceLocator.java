package codeOrchestra.colt.core.rpc.discovery;

import codeOrchestra.colt.core.rpc.ColtRemoteService;
import codeOrchestra.colt.core.storage.ProjectStorageHelper;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.googlecode.jsonrpc4j.ProxyUtil;
import com.intellij.openapi.project.Project;
import utils.FileUtils;
import utils.StringUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Alexander Eliseyev
 */
public class FileBasedColtServiceLocator extends AbstractColtServiceLocator {

    public FileBasedColtServiceLocator(Project project) {
        super(project);
    }

    @Override
    public  <S extends ColtRemoteService> S locateService(Class<S> serviceClass, String projectPath, String name) {
        File coltProjectStorageDir = ProjectStorageHelper.getColtProjectStorageDir(projectPath);
        if (coltProjectStorageDir == null) {
            return null;
        }

        File serviceInfoFile = new File(coltProjectStorageDir, "rpc.info");
        if (!serviceInfoFile.exists() || (System.currentTimeMillis() - serviceInfoFile.lastModified() > 2000)) {
            return null;
        }

        String serviceURL = FileUtils.read(serviceInfoFile).trim();
        if (StringUtils.isEmpty(serviceURL)) {
            return null;
        }

        String portStr = serviceURL.split(":")[1];
        System.out.println(portStr);

        int port = Integer.valueOf(portStr);

        try {
            JsonRpcHttpClient client = null;
            try {
                client = new JsonRpcHttpClient(new URL("http://localhost:" + port + "/rpc/coltService"));
            } catch (MalformedURLException e) {
                // should not happen
            }
            return ProxyUtil.createClientProxy(getClass().getClassLoader(), serviceClass, client);
        } catch (Throwable t) {
            return null;
        }
    }

}
