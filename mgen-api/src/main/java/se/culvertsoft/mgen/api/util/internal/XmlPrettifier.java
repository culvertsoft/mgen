package se.culvertsoft.mgen.api.util.internal;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

/**
 * Pretty-prints xml, supplied as a string.
 * <p/>
 * eg. <code>
 * String formattedXml = new XmlFormatter().format("<tag><nested>hello</nested></tag>");
 * </code>
 */
public class XmlPrettifier {

	public static void prettyPrint(final String xml) throws Exception {

		prettyPrint(xml.getBytes());

	}

	public static String prettyPrint(final byte[] xml) throws Exception {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		dbf.setValidating(false);

		DocumentBuilder db = dbf.newDocumentBuilder();

		Document doc = db.parse(new BufferedInputStream(
				new ByteArrayInputStream(xml)));

		return prettyPrint(doc);
	}

	public static String prettyPrint(Document xml) throws Exception {

		Transformer tf = TransformerFactory.newInstance().newTransformer();

		tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		tf.setOutputProperty(OutputKeys.INDENT, "yes");

		Writer out = new StringWriter();

		tf.transform(new DOMSource(xml), new StreamResult(out));

		return out.toString();

	}

}
