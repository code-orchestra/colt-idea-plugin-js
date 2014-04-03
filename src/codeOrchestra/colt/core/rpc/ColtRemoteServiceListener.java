package codeOrchestra.colt.core.rpc;

import codeOrchestra.colt.core.rpc.model.ColtMessage;
import codeOrchestra.colt.core.rpc.model.ColtState;

/**
 * @author Alexander Eliseyev
 */
public interface ColtRemoteServiceListener {

    void onConnected();

    void onStateUpdate(ColtState state);

    void onMessage(ColtMessage coltCompilerMessage);

    void onDisconnected();

}
