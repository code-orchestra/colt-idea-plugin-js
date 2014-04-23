package codeOrchestra.colt.core.rpc;

import codeOrchestra.colt.core.rpc.model.ColtState;
import codeOrchestra.colt.core.rpc.security.InvalidAuthTokenException;
import codeOrchestra.colt.core.rpc.security.TooManyFailedCodeTypeAttemptsException;
import codeOrchestra.colt.core.rpc.security.InvalidShortCodeException;

/**
 * @author Alexander Eliseyev
 */
public interface ColtRemoteService {

    ColtState getState();

    int ping();

    // Authorization methods

    void requestShortCode(String requestor) throws ColtRemoteTransferableException;

    String obtainAuthToken(String shortCode) throws TooManyFailedCodeTypeAttemptsException, InvalidShortCodeException;

    void checkAuth(String securityToken) throws InvalidAuthTokenException;

    void stopAllSessions(String securityToken);

}
