package codeOrchestra.colt.js.rpc.model.codec;

import codeOrchestra.colt.core.rpc.model.ColtLauncherType;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

/**
 * @author Dima Kruk
 */
public class ColtJSRemoteProjectDecoder {

    public static ColtLauncherType getLauncher(String path) {
        ColtLauncherType result = ColtLauncherType.BROWSER;
        try {
            File fXmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            String launcher = doc.getElementsByTagName("launcher").item(0).getTextContent();
            result = ColtLauncherType.valueOf(launcher);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
