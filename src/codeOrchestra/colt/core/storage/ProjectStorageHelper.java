package codeOrchestra.colt.core.storage;

import com.intellij.openapi.diagnostic.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import utils.XMLUtils;

import java.io.File;

/**
 * @author Alexander Eliseyev
 */
public final class ProjectStorageHelper {

    public static final String COLT_DIR_NAME = ".colt";

    private static Logger LOG = Logger.getInstance(ProjectStorageHelper.class);

    public static File getColtProjectStorageDir(String coltProjectPath) {
        File storageDescriptorsFile = getStorageDescriptorsFile();
        if (storageDescriptorsFile == null || !storageDescriptorsFile.exists()) {
            return null;
        }

        Document document;
        try {
            document = XMLUtils.fileToDOM(storageDescriptorsFile);
        } catch (Throwable t) {
            LOG.error("Can't parse storage descriptor file " + storageDescriptorsFile.getPath(), t);
            return null;
        }

        NodeList storageElements = document.getDocumentElement().getElementsByTagName("storage");
        for (int i = 0; i < storageElements.getLength(); i++) {
            Element storageElement = (Element) storageElements.item(i);

            String path = storageElement.getAttribute("path");
            if (!coltProjectPath.equals(path) && !coltProjectPath.replace("/", "\\").equals(path)) {
                continue;
            }

            File projectStoragePath = getStorageDir(storageElement.getAttribute("subDir"));
            if (!projectStoragePath.exists()) {
                return null;
            }

            return projectStoragePath;
        }

        return null;
    }

    private static File getStorageDescriptorsFile() {
        return new File(getColtDir(), "storage.xml");
    }

    private static File getStorageDir(String subDir) {
        return new File(new File(getColtDir(), "storage"), subDir);
    }

    private static File getColtDir() {
        return new File(System.getProperty("user.home"), COLT_DIR_NAME);
    }

}
