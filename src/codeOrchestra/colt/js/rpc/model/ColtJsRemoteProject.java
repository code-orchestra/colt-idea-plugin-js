package codeOrchestra.colt.js.rpc.model;

import codeOrchestra.colt.core.rpc.model.ColtRemoteProject;

/**
 * @author Alexander Eliseyev
 */
public class ColtJsRemoteProject extends ColtRemoteProject {

    private String path;
    private String name;

    private String mainDocument;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMainDocument() {
        return mainDocument;
    }

    public void setMainDocument(String mainDocument) {
        this.mainDocument = mainDocument;
    }
}
