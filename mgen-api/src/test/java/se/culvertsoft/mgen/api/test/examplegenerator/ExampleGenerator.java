package se.culvertsoft.mgen.api.test.examplegenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.ClassType;
import se.culvertsoft.mgen.api.model.EnumType;
import se.culvertsoft.mgen.api.model.GeneratedSourceFile;
import se.culvertsoft.mgen.api.model.Module;
import se.culvertsoft.mgen.api.model.Project;
import se.culvertsoft.mgen.api.plugins.Generator;

/**
 * An Generator class for MGen tutorials and examples.
 */
public class ExampleGenerator implements Generator {

	@Override
	public List<GeneratedSourceFile> generate(
			Project project, 
			Map<String, String> settinsg) {

		StringBuilder sb = new StringBuilder();
		
		sb.append("Generator log for: " + project.name()).append("\n");
		
		// Print all the modules and their contents
		for (Module module : project.modules()) {
			
			// First we print the module path
			sb.append(module.path()).append("\n");
			
			// Print enums
			sb.append("  enums:").append("\n");
			for (EnumType enumT : module.enums()) {
				sb.append("    ").append(enumT.shortName()).append("\n");
			}
			
			// Print classes
			sb.append("  classes:").append("\n");
			for (ClassType classT : module.classes()) {
				sb.append("    ").append(classT.shortName()).append("\n");
			}
			
		}
		
		
		String fileName = "generated_files.log";
		String sourceCode = sb.toString();

		return Arrays.asList(new GeneratedSourceFile(fileName, sourceCode));

	}

}
