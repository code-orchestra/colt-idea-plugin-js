package codeOrchestra.colt.core.workset;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import utils.XMLUtils;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class Workset {

    public static final String COLT_DIR_NAME = ".colt";

    public static void addProjectPath(String projectPath, boolean openRecent) {
        List<String> recentProjects = getProjectsList();
        recentProjects.removeAll(Collections.singletonList(projectPath));
        recentProjects.add(0, projectPath);

        save(recentProjects, openRecent);
    }

    private static void save(List<String> recentProjects, boolean openRecent) {
        Document document = XMLUtils.createDocument();
        Element workingSetElement = document.createElement("workingset");
        document.appendChild(workingSetElement);

        workingSetElement.setAttribute("openRecent", String.valueOf(openRecent));

        for (String recentProject : recentProjects) {
            Element projectElement = document.createElement("project");
            projectElement.setAttribute("path", recentProject);
            workingSetElement.appendChild(projectElement);
        }

        try {
            XMLUtils.saveToFile(getWorkingSetFile().getPath(), document);
        } catch (TransformerException e) {
            throw new RuntimeException("Can't save working set file " + getWorkingSetFile().getPath(), e);
        }
    }

    private static List<String> getProjectsList() {
        List<String> result = new ArrayList<String>();

        File workingSetFile = getWorkingSetFile();
        if (!workingSetFile.exists()) {
            return result;
        }

        Document document;
        try {
            document = XMLUtils.fileToDOM(workingSetFile);
        } catch (Throwable t) {
            return result;
        }

        Element rootElement = document.getDocumentElement();
        NodeList projectList = rootElement.getElementsByTagName("project");

        for (int i = 0; i < projectList.getLength(); i++) {
            Element projectElement = (Element) projectList.item(i);
            result.add(projectElement.getTextContent());
        }

        return result;
    }

    private static File getWorkingSetFile() {
        return new File(getOrCreateColtDir(), "workingset.xml");
    }

    private static File getOrCreateColtDir() {
        File coltDir = new File(System.getProperty("user.home"), COLT_DIR_NAME);
        if (!coltDir.exists()) {
            coltDir.mkdir();
        }
        return coltDir;
    }

}
