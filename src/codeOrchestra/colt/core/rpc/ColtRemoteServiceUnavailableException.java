package codeOrchestra.colt.core.rpc;

/**
 * @author Alexander Eliseyev
 */
public class ColtRemoteServiceUnavailableException extends Exception {

    private static final String DEFAULT_MESSAGE = "Can't reach COLT remote service API";

    public ColtRemoteServiceUnavailableException() {
        this(DEFAULT_MESSAGE);
    }

    public ColtRemoteServiceUnavailableException(String s) {
        super(s);
    }

    public ColtRemoteServiceUnavailableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ColtRemoteServiceUnavailableException(Throwable throwable) {
        this(DEFAULT_MESSAGE, throwable);
    }
}
