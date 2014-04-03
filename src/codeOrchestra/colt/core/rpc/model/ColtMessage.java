package codeOrchestra.colt.core.rpc.model;

/**
 * @author Alexander Eliseyev
 */
public class ColtMessage {

    private String sourcePath;
    private int lineNumber;
    private int columnNumber;
    private String type;
    private String content;
    private String fullMessage;

    public ColtMessage() {
    }

    public ColtMessage(String fullMessage) {
        this.fullMessage = fullMessage + "\n";
        this.type = "Info";
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFullMessage() {
        return fullMessage;
    }

    public void setFullMessage(String fullMessage) {
        this.fullMessage = fullMessage;
    }
}
