package utils;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * @author Alexander Eliseyev
 */
public final class XMLUtils {

    private XMLUtils() {
    }

    public static String documentToString(Document document) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tf.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        try {
            transformer.transform(new DOMSource(document), new StreamResult(writer));
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
        return output;
    }

    public static File saveToFile(String filename, Document document) throws TransformerException {
        // Prepare the DOM document for writing
        Source source = new DOMSource(document);

        // Prepare the output file
        File file = new File(filename);
        Result result = new StreamResult(file);

        // Write the DOM document to the file
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        xformer.setOutputProperty(OutputKeys.INDENT, "yes");
        xformer.transform(source, result);

        return file;
    }

    public static Document createDocument() {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();
            return doc;
        } catch (ParserConfigurationException e) {
        }
        return null;
    }

    public static Document stringToDOM(String xmlString) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Throwable t) {
            throw new RuntimeException("Error while building document");
        }
    }

    public static Document fileToDOM(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        return docBuilder.parse(file);

    }

    public static Document streamToDocument(InputStream is) throws IOException {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String inline = "";
        while ((inline = inputReader.readLine()) != null) {
            sb.append(inline);
        }

        return stringToDOM(sb.toString());
    }

}
