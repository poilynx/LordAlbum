package cc.jmpesp.lib;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FileEntity implements Serializable {
	private String username;
	private String fileName;
	private int id;
	private byte[] fileContent;

	public FileEntity(int id, String fileName) {
		this.id = id;
		this.fileName = fileName;
	}

	public FileEntity() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	@Override
	public String toString() {
		return id + "\t" + fileName;
	}

}
