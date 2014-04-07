package codeOrchestra.colt.core.plugin.launch;

import codeOrchestra.colt.core.plugin.ColtSettings;
import com.intellij.execution.ExecutionException;
import com.intellij.openapi.util.SystemInfo;

import java.io.File;
import java.io.IOException;

/**
 * @author Alexander Eliseyev
 */
public final class ColtLauncher {

    public static Process launch(String projectPath) throws ColtPathNotConfiguredException, ExecutionException, IOException {
        if (!ColtSettings.getInstance().isColtPathValid()) {
            throw new ColtPathNotConfiguredException();
        }

        File coltBaseDir = new File(ColtSettings.getInstance().getColtPath());

        if (SystemInfo.isMac && coltBaseDir.getPath().endsWith(".app")) {
            return Runtime.getRuntime().exec("open -n -a " + coltBaseDir.getPath() + " " + projectPath + " --args -plugin:WS");
        } else if (SystemInfo.isWindows || SystemInfo.isLinux) {
            File executable = getApplicationExecutable(coltBaseDir);
            if (executable != null && executable.exists()) {
                return Runtime.getRuntime().exec(executable.getPath() + " " + projectPath + " -plugin:WS");
            }

            throw new IllegalStateException("Can't locate the COLT executable");
        } else {
            throw new IllegalStateException("Unsupported OS: " + System.getProperty("os.name"));
        }
    }

    public static File getApplicationExecutable(File coltBaseDir) {
        if (SystemInfo.isMac) {
            File executable = coltBaseDir;
            return executable.exists() ? executable : null;
        } else if (SystemInfo.isWindows) {
            File executable = new File(coltBaseDir, "colt.exe");
            return executable.exists() ? executable : null;
        } else if (SystemInfo.isLinux) {
            File executable = new File(coltBaseDir, "colt");
            return executable.exists() ? executable : null;
        }

        throw new IllegalStateException("Unsupported OS: " + System.getProperty("os.name"));
    }

}
