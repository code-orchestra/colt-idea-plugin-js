package codeOrchestra.colt.core.rpc.model.codec;

import codeOrchestra.colt.core.rpc.model.ColtRemoteProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import utils.StringUtils;
import utils.XMLUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Alexander Eliseyev
 */
public abstract class ColtRemoteProjectEncoder<P extends ColtRemoteProject> {

    protected Document projectDocument = XMLUtils.createDocument();

    protected P project;

    protected ColtRemoteProjectEncoder(P project) {
        this.project = project;
    }

    public abstract Document toDocument();

    protected Element createElement(String name) {
        return createElement(name, null);
    }

    protected Element createElement(String name, String textValue) {
        return createElement(name, textValue, null);
    }

    protected Element createElement(String name, String textValue, Element parentElement) {
        Element element = projectDocument.createElement(name);
        if (textValue != null) {
            element.setTextContent(textValue);
        }
        if (parentElement != null) {
            parentElement.appendChild(element);
        }
        return element;
    }

    protected String safe(String str) {
        return safe(str, "");
    }

    protected String safe(String str, String defaultValue) {
        return StringUtils.isEmpty(str) ? defaultValue : str;
    }

    protected String createFileSetFromPathArray(String[] paths) {
        if (paths == null || paths.length == 0) {
            return "";
        }

        File baseDir = new File(project.getPath()).getParentFile();

        StringBuilder sb = new StringBuilder();
        Iterator<String> pathIterator = Arrays.asList(paths).iterator();
        while (pathIterator.hasNext()) {
            String path = pathIterator.next();
            File file = new File(path);
            if (file.getPath().startsWith(baseDir.getPath())) {
                String relativePath = file.getPath().replace(baseDir.getPath(), "");
                if (relativePath.startsWith("/") || relativePath.startsWith("\\")) {
                    relativePath = relativePath.substring(1, relativePath.length());
                }

                sb.append(relativePath);
            } else {
                sb.append(file.getPath());
            }

            if (pathIterator.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

}
