package codeOrchestra.colt.js.rpc.model;

import codeOrchestra.colt.core.rpc.model.ColtLauncherType;
import codeOrchestra.colt.core.rpc.model.ColtRemoteProject;

/**
 * @author Alexander Eliseyev
 */
public class ColtJsRemoteProject extends ColtRemoteProject {

    private String mainDocument;
    private ColtLauncherType launcher = ColtLauncherType.BROWSER;

    public String getMainDocument() {
        return mainDocument;
    }

    public void setMainDocument(String mainDocument) {
        this.mainDocument = mainDocument;
    }

    public ColtLauncherType getLauncher() {
        return launcher;
    }

    public void setLauncher(ColtLauncherType launcher) {
        this.launcher = launcher;
    }
}
