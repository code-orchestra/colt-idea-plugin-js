package codeOrchestra.colt.js.rpc.model.codec;

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
            createElement("sources-set", "**/*.js, -lib/*.js", pathsElement);
            createElement("excludes-set", "out/**, .git/**, .*/**, **/*bak___", pathsElement);
            createElement("reloads-set", "**/*.htm*, **/*.css, **/*.png", pathsElement);
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
                createElement("disconnect", "true", settingsElement);
            }
            liveElement.appendChild(settingsElement);

            // Launch
            Element launchElement = createElement("launch");
            {
                createElement("launcher", "DEFAULT", launchElement);
            }
            liveElement.appendChild(launchElement);

            // Inner live
            Element innerLiveElement = createElement("live");
            {
                createElement("paused", "false", innerLiveElement);
                createElement("max-loop", "10000", innerLiveElement);
                createElement("live-html-edit", "true", innerLiveElement);
                createElement("disable-in-minified", "true", innerLiveElement);
            }
            liveElement.appendChild(innerLiveElement);
        }
        rootElement.appendChild(liveElement);

        return projectDocument;
    }
}