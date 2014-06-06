package se.culvertsoft.mgen.api.plugins;

public class GeneratedSourceFile {

	public GeneratedSourceFile(
			final String folder,
			final String fileName,
			final String sourceCode) {
		m_folder = folder;
		m_fileName = fileName;
		m_filePath = m_folder + "/" + m_fileName;
		m_sourceCode = sourceCode;
	}

	public String folder() {
		return m_folder;
	}

	public String fileName() {
		return m_fileName;
	}

	public String sourceCode() {
		return m_sourceCode;
	}

	public String filePath() {
		return m_filePath;
	}

	@Override
	public String toString() {
		return "GeneratedSourceFile [m_folder=" + m_folder + ", m_fileName="
				+ m_fileName + ", m_sourceCode=" + m_sourceCode + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((m_fileName == null) ? 0 : m_fileName.hashCode());
		result = prime * result
				+ ((m_folder == null) ? 0 : m_folder.hashCode());
		result = prime * result
				+ ((m_sourceCode == null) ? 0 : m_sourceCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeneratedSourceFile other = (GeneratedSourceFile) obj;
		if (m_fileName == null) {
			if (other.m_fileName != null)
				return false;
		} else if (!m_fileName.equals(other.m_fileName))
			return false;
		if (m_folder == null) {
			if (other.m_folder != null)
				return false;
		} else if (!m_folder.equals(other.m_folder))
			return false;
		if (m_sourceCode == null) {
			if (other.m_sourceCode != null)
				return false;
		} else if (!m_sourceCode.equals(other.m_sourceCode))
			return false;
		return true;
	}

	private final String m_folder;
	private final String m_fileName;
	private final String m_filePath;
	private final String m_sourceCode;

}
