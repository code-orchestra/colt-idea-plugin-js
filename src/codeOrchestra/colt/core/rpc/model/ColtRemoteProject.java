package codeOrchestra.colt.core.rpc.model;

/**
 * @author Alexander Eliseyev
 */
public abstract class ColtRemoteProject {

    private String path;

    private String name;

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
}
