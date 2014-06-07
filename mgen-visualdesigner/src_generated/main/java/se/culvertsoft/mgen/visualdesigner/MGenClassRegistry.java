package se.culvertsoft.mgen.visualdesigner;

public class MGenClassRegistry extends se.culvertsoft.mgen.javapack.classes.ClassRegistry {

    public MGenClassRegistry() {
        add(new se.culvertsoft.mgen.visualdesigner.model.MGenModuleClassRegistry());
    }
}
