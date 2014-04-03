package codeOrchestra.colt.core.rpc;

/**
 * @author Alexander Eliseyev
 */
public class ColtRemoteException extends Exception {

  public ColtRemoteException() {
    super();
  }

  public ColtRemoteException(String message, Throwable cause) {
    super(message, cause);
  }

  public ColtRemoteException(String message) {
    super(message);
  }

  public ColtRemoteException(Throwable cause) {
    super(cause);
  }

}
