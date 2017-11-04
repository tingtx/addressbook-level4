package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Helps with reading from and writing to XML files.
 */
public class XmlUtil {

    /**
     * Returns the xml data in the file as an object of the specified type.
     *
     * @param file           Points to a valid xml file containing data that match the {@code classToConvert}.
     *                       Cannot be null.
     * @param classToConvert The class corresponding to the xml data.
     *                       Cannot be null.
     * @throws FileNotFoundException Thrown if the file is missing.
     * @throws JAXBException         Thrown if the file is empty or does not have the correct format.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getDataFromFile(File file, Class<T> classToConvert)
            throws FileNotFoundException, JAXBException {

        requireNonNull(file);
        requireNonNull(classToConvert);

        if (!FileUtil.isFileExists(file)) {
            throw new FileNotFoundException("File not found : " + file.getAbsolutePath());
        }

        JAXBContext context = JAXBContext.newInstance(classToConvert);
        Unmarshaller um = context.createUnmarshaller();

        return ((T) um.unmarshal(file));
    }

    /**
     * Saves the data in the file in xml format.
     *
     * @param file Points to a valid xml file containing data that match the {@code classToConvert}.
     *             Cannot be null.
     * @throws FileNotFoundException Thrown if the file is missing.
     * @throws JAXBException         Thrown if there is an error during converting the data
     *                               into xml and writing to the file.
     */
    public static <T> void saveDataToFile(File file, T data) throws FileNotFoundException, JAXBException {

        requireNonNull(file);
        requireNonNull(data);

        if (!file.exists()) {
            throw new FileNotFoundException("File not found : " + file.getAbsolutePath());
        }

        JAXBContext context = JAXBContext.newInstance(data.getClass());
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        m.marshal(data, file);
    }

    /**
     * Export the data in the xml file to csv.
     * @param source
     * @param destination
     * @param style
     * @param objectElement
     * @throws FileNotFoundException
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws TransformerException
     */
    public static void exportDataToFile(String source, String destination, String style, String objectElement)
            throws FileNotFoundException, ParserConfigurationException,
            IOException, SAXException, TransformerException {

        requireNonNull(source);
        requireNonNull(destination);
        requireNonNull(style);
        requireNonNull(objectElement);

        File xmlSource = new File(source);
        File stylesheet = new File(style);

        if (!xmlSource.exists()) {
            throw new FileNotFoundException("File not found : " + xmlSource.getAbsolutePath());
        }

        //Check whether the Stylesheet
        //if not exists, generate a new stylesheet
        if (!stylesheet.exists()) {
            generateXslStyle(style, objectElement);
            stylesheet = new File(style);
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlSource);

        StreamSource styleSource = new StreamSource(stylesheet);
        Transformer transformer = TransformerFactory.newInstance()
                .newTransformer(styleSource);
        Source sourceDoc = new DOMSource(document);
        Result outputTarget = new StreamResult(new File(destination));
        transformer.transform(sourceDoc, outputTarget);
    }

    /**
     * Generate a XSLT file
     * @param destUrl
     * @param objectElement
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public static void generateXslStyle(String destUrl, String objectElement)
            throws ParserConfigurationException, TransformerException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element rootElement = document.createElement("xsl:stylesheet");
        rootElement.setAttribute("version", "1.0");
        rootElement.setAttribute("xmlns:xsl", "http://www.w3.org/1999/XSL/Transform");
        document.appendChild(rootElement);

        Element emOutput = document.createElement("xsl:output");
        emOutput.setAttribute("method", "text");
        emOutput.setAttribute("omit-xml-declaration", "yes");
        emOutput.setAttribute("indent", "no");
        rootElement.appendChild(emOutput);

        Element emVarDelimiter = document.createElement("xsl:variable");
        emVarDelimiter.setAttribute("name", "delimiter");
        emVarDelimiter.setAttribute("select", "','");
        rootElement.appendChild(emVarDelimiter);

        Element emTemplate = document.createElement("xsl:template");
        emTemplate.setAttribute("match", "/");
        rootElement.appendChild(emTemplate);

        Element emForEachObject = document.createElement("xsl:for-each");
        emForEachObject.setAttribute("select", "//" + objectElement);
        emTemplate.appendChild(emForEachObject);

        Element emProperty = document.createElement("xsl:variable");
        emProperty.setAttribute("name", "property");
        emProperty.setAttribute("select", ".");
        emForEachObject.appendChild(emProperty);

        Element emForEachProperty = document.createElement("xsl:for-each");
        emForEachProperty.setAttribute("select", "$property/*");
        emForEachObject.appendChild(emForEachProperty);

        Element emVarValue = document.createElement("xsl:variable");
        emVarValue.setAttribute("name", "value");
        emVarValue.setAttribute("select", ".");
        emForEachProperty.appendChild(emVarValue);

        Element emValueOf = document.createElement("xsl:value-of");
        emValueOf.setAttribute("select", "$value");
        emForEachProperty.appendChild(emValueOf);

        Element emPosNotLast = document.createElement("xsl:if");
        emPosNotLast.setAttribute("test", "position() != last()");
        emForEachProperty.appendChild(emPosNotLast);

        Element emValueOfDelimiter = document.createElement("xsl:value-of");
        emValueOfDelimiter.setAttribute("select", "$delimiter");
        emPosNotLast.appendChild(emValueOfDelimiter);

        Element emPostLast = document.createElement("xsl:if");
        emPostLast.setAttribute("test", "position() = last()");
        emForEachProperty.appendChild(emPostLast);

        Element emText = document.createElement("xsl:text");
        emText.setTextContent("&#xa;");
        emPostLast.appendChild(emText);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        Source source = new DOMSource(document);
        Result outputTarget = new StreamResult(new File(destUrl));
        transformer.transform(source, outputTarget);

        File xslFile = new File(destUrl);
        BufferedReader br = new BufferedReader(new FileReader(xslFile));
        StringBuilder sb = new StringBuilder();
        String line = null;
        String newline = System.getProperty("line.separator");
        while ((line = br.readLine()) != null) {

            if (line.indexOf("&amp;") != -1) {
                line = line.replaceAll("&amp;", "&");
            }
            sb.append(line).append(newline);
        }
        br.close();

        BufferedWriter bw = new BufferedWriter(new FileWriter(xslFile));
        bw.write(sb.toString());
        bw.close();
    }
}
