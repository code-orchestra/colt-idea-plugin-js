package codeOrchestra.colt.js.rpc.model.jsScript;

/**
 * @author Dima Kruk
 */
public class RuntimeErrorInfo {
    public String errorMessage;
    public String methodId;
    public String filePath;
    public int position;
    public int row = -1;
}
