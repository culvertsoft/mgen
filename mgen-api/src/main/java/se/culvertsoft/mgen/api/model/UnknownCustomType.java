package se.culvertsoft.mgen.api.model;

public interface UnknownCustomType extends Type {

	public String writtenType();

	public boolean matchesOneOf(final short[] typeIds);

}
