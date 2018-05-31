package cc.jmpesp.lib;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FileEntity implements Serializable {
	private int uid;
	private int id;
	private String fileName;
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

	public int getUserId() {
		return uid;
	}

	public void setUserId(int uid) {
		this.uid= uid;
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
