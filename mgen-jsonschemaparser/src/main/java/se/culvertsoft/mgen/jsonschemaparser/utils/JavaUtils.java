package se.culvertsoft.mgen.jsonschemaparser.utils;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jsonschema2pojo.Annotator;
import org.jsonschema2pojo.AnnotatorFactory;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.SchemaMapper;
import org.jsonschema2pojo.rules.RuleFactory;

import com.sun.codemodel.JCodeModel;

/**
 * Contains code extracted from the class Jsonschema2Pojo from the github
 * apache2 project with the same name
 */
public class JavaUtils {

	public static RuleFactory createRuleFactory(GenerationConfig config) {
		Class<? extends RuleFactory> clazz = config.getCustomRuleFactory();

		if (!RuleFactory.class.isAssignableFrom(clazz)) {
			throw new IllegalArgumentException(
					"The class name given as a rule factory  ("
							+ clazz.getName()
							+ ") does not refer to a class that implements "
							+ RuleFactory.class.getName());
		}

		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException(
					"Failed to create a rule factory from the given class. An exception was thrown on trying to create a new instance.",
					e.getCause());
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(
					"Failed to create a rule factory from the given class. It appears that we do not have access to this class - is both the class and its no-arg constructor marked public?",
					e);
		}
	}

	public static void generateRecursive(
			GenerationConfig config,
			SchemaMapper mapper,
			JCodeModel codeModel,
			String packageName,
			List<File> schemaFiles) throws FileNotFoundException, IOException {
		Collections.sort(schemaFiles);

		for (File child : schemaFiles) {
			if (child.isFile()) {
				mapper.generate(
						codeModel,
						getNodeName(child),
						defaultString(packageName),
						child.toURI().toURL());
			} else {
				generateRecursive(
						config,
						mapper,
						codeModel,
						packageName + "." + child.getName(),
						Arrays.asList(child.listFiles(config.getFileFilter())));
			}
		}
	}

	public static void removeOldOutput(File targetDirectory) {
		if (targetDirectory.exists()) {
			for (File f : targetDirectory.listFiles()) {
				delete(f);
			}
		}
	}

	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "RV_RETURN_VALUE_IGNORED_BAD_PRACTICE")
	public static
			void
			delete(File f) {
		if (f.isDirectory()) {
			for (File child : f.listFiles()) {
				delete(child);
			}
		}
		f.delete();
	}

	public static Annotator getAnnotator(GenerationConfig config) {
		AnnotatorFactory factory = new AnnotatorFactory();
		return factory.getAnnotator(
				factory.getAnnotator(config.getAnnotationStyle()),
				factory.getAnnotator(config.getCustomAnnotator()));
	}

	public static String getNodeName(File file) {
		return substringBeforeLast(file.getName(), ".");
	}
}
