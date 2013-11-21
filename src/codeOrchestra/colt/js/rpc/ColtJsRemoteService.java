package codeOrchestra.colt.js.rpc;

import codeOrchestra.colt.core.rpc.ColtRemoteService;
import codeOrchestra.colt.core.rpc.ColtRemoteTransferableException;

/**
 * @author Alexander Eliseyev
 */
public interface ColtJsRemoteService extends ColtRemoteService {

    // Secured methods

    void startLive(String securityToken) throws ColtRemoteTransferableException;

    void startProduction(String securityToken) throws ColtRemoteTransferableException;

    String getContextForPosition(String securityToken, String file, int position, String currentContent, String contextType) throws ColtRemoteTransferableException;

}
