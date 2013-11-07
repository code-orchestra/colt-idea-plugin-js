package codeOrchestra.colt.js.rpc.model;

import codeOrchestra.colt.core.rpc.model.ColtRemoteProject;

/**
 * @author Alexander Eliseyev
 */
public class ColtJsRemoteProject extends ColtRemoteProject {

    private String mainDocument;

    public String getMainDocument() {
        return mainDocument;
    }

    public void setMainDocument(String mainDocument) {
        this.mainDocument = mainDocument;
    }
}
