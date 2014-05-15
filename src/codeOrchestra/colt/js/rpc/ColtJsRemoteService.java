package codeOrchestra.colt.js.rpc;

import codeOrchestra.colt.core.rpc.ColtRemoteService;
import codeOrchestra.colt.core.rpc.ColtRemoteTransferableException;
import codeOrchestra.colt.js.rpc.model.jsScript.*;

import java.util.ArrayList;

/**
 * @author Alexander Eliseyev
 */
public interface ColtJsRemoteService extends ColtRemoteService {

    // Secured methods

    void startLive(String securityToken) throws ColtRemoteTransferableException;

    void startProduction(String securityToken) throws ColtRemoteTransferableException;

    String getContextForPosition(String securityToken, String file, int position, String currentContent, String contextType) throws ColtRemoteTransferableException;

    String evaluateExpression(String securityToken, String file, String expression, int position, String currentContent) throws ColtRemoteTransferableException;

    String[] getMethodParams(String securityToken, String file, String methodId) throws ColtRemoteTransferableException;

    String getMethodId(String securityToken, String file, int position, String currentContent) throws ColtRemoteTransferableException;

    void runMethod(String securityToken, String methodId) throws ColtRemoteTransferableException;

    void reload(String securityToken) throws ColtRemoteTransferableException;

    ScriptPoint getDeclarationPosition(String securityToken, String file, int position, String currentContent) throws ColtRemoteTransferableException;

    void resetCallCounts(String securityToken) throws ColtRemoteTransferableException;

    int getCallCount(String securityToken, String file, int position, String currentContent) throws ColtRemoteTransferableException;

    void clearLog(String securityToken) throws ColtRemoteTransferableException;

    ArrayList<MethodCount> getMethodCounts(String securityToken) throws ColtRemoteTransferableException;

    RuntimeErrorInfo getLastRuntimeError (String securityToken) throws ColtRemoteTransferableException;

    ArrayList<LogMessageInfo> getLastLogMessages (String securityToken) throws ColtRemoteTransferableException;

    void reloadScriptAt (String securityToken, String file, int position, String currentContent) throws ColtRemoteTransferableException;

    String getEnclosingTagId(String securityToken, String file, int position, String currentContent) throws ColtRemoteTransferableException;

    void findAndShowJavaDocs(String securityToken, String file, int position, String currentContent) throws ColtRemoteTransferableException;

    String[] angularExpressionCompletion(String securityToken, int tagId, String leftExpression) throws ColtRemoteTransferableException;

    String[] angularExpressionCompletionAfterDot(String securityToken, int tagId, int position, String expression) throws ColtRemoteTransferableException;

    ScriptPoint angularDirectiveDeclaration(String securityToken, String file, int position, String currentContent) throws ColtRemoteTransferableException;

}
