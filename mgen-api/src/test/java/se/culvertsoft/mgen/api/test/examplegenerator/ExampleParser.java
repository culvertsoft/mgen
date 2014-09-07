package se.culvertsoft.mgen.api.test.examplegenerator;

import java.io.File;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.ClassType;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.Int32Type;
import se.culvertsoft.mgen.api.model.Module;
import se.culvertsoft.mgen.api.model.Project;
import se.culvertsoft.mgen.api.plugins.Parser;

/**
 * An Generator class for MGen tutorials and examples.
 */
public class ExampleParser implements Parser {

	@Override
	public void parse(
			List<File> sources, 
			Map<String, String> settings, 
			Project parent) {
		
		// This parser doesn't parse any code.
		// It just pretends to already have done so 
		// and adds a mock Module with a mock Class for
		// example and tutorial purposes.
		
		String modulePath = "com.fruitcompany";
		String idlRelFilePath = "com.fruitcompany.txt";
		String idlAbsFilePath = "c:\\myMgenProject\\com.fruitcompant.txt";
		
		// Get or create the module we want to add a class to
		Module module = parent.getOrCreateModule(
				modulePath,
				idlRelFilePath,
				idlAbsFilePath,
				settings);
		
		// Create a class called "FruitBowl"
		ClassType bowl = new ClassType(
				"FruitBowl", // Class name 
				module, // Parent module
				null); // Super type
		
		// Add a field named "capacity" to class "FruitBowl"
		bowl.addField(
				new Field(
						bowl.fullName(), // parent class name
						"capacity", // field name
						Int32Type.INSTANCE, // field type
						null)); // field flags
		
		
	}

}
