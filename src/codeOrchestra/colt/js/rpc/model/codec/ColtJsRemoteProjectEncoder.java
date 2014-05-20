package codeOrchestra.colt.js.rpc.model.codec;

import codeOrchestra.colt.core.plugin.ColtSettings;
import codeOrchestra.colt.core.rpc.model.ColtLauncherType;
import codeOrchestra.colt.core.rpc.model.codec.ColtRemoteProjectEncoder;
import codeOrchestra.colt.js.rpc.model.ColtJsRemoteProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Alexander Eliseyev
 */
public class ColtJsRemoteProjectEncoder extends ColtRemoteProjectEncoder<ColtJsRemoteProject> {

    public ColtJsRemoteProjectEncoder(ColtJsRemoteProject project) {
        super(project);
    }

    @Override
    public Document toDocument() {
        // Root
        Element rootElement = projectDocument.createElement("xml");
        rootElement.setAttribute("projectName", project.getName());
        rootElement.setAttribute("projectType", "JS");
        projectDocument.appendChild(rootElement);

        // Paths
        Element pathsElement = projectDocument.createElement("paths");
        {
            createElement("excludes-set", "out, node_modules, bower_components", pathsElement);
        }
        rootElement.appendChild(pathsElement);

        // Build
        Element buildElement = createElement("build");
        {
            createElement("main-document", project.getMainDocument(), buildElement);
            createElement("use-custom-output-path", "false", buildElement);
            createElement("out-path", "", buildElement);
        }
        rootElement.appendChild(buildElement);

        // Live
        Element liveElement = createElement("live");
        {
            // Settings
            Element settingsElement = createElement("settings");
            {
                createElement("clear-log", "false", settingsElement);
                createElement("disconnect", "false", settingsElement);
            }
            liveElement.appendChild(settingsElement);

            // Launch
            Element launchElement = createElement("launch");
            {
                createElement("launcher", project.getLauncher().toString(), launchElement);
                createElement("browser-path", "", launchElement);
                if (project.getLauncher() == ColtLauncherType.NODE_JS) {
                    createElement("nodejs-path", ColtSettings.getInstance().getNodePath(), launchElement);
                } else {
                    createElement("nodejs-path", "", launchElement);
                }
                if (project.getLauncher() == ColtLauncherType.NODE_WEBKIT) {
                    createElement("node-webkit-path", ColtSettings.getInstance().getNodeWebkitPath(), launchElement);
                } else {
                    createElement("node-webkit-path", "", launchElement);
                }
//                createElement("console-value", "", launchElement);
            }
            liveElement.appendChild(launchElement);

            // Inner live
            Element innerLiveElement = createElement("live");
            {
                createElement("paused", "false", innerLiveElement);
                createElement("max-loop", "10000", innerLiveElement);
                createElement("disable-in-minified", "true", innerLiveElement);
            }
            liveElement.appendChild(innerLiveElement);
        }
        rootElement.appendChild(liveElement);

        return projectDocument;
    }
}
