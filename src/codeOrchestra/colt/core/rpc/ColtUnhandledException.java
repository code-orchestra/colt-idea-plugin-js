package codeOrchestra.colt.core.rpc;

/**
 * @author Alexander Eliseyev
 */
public class ColtUnhandledException extends ColtRemoteTransferableException {

    public ColtUnhandledException() {
    }

    public ColtUnhandledException(String message, Throwable cause) {
        super(message, cause);
    }

    public ColtUnhandledException(String message) {
        super(message);
    }

    public ColtUnhandledException(Throwable cause) {
        super(cause);
    }

}
