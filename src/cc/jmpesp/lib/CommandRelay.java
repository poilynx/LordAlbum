package cc.jmpesp.lib;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class CommandRelay implements Serializable {
	private String command;
	private boolean flag;
	private Object userObject = null;
	private Object fileObject = null;
	private byte[] bytes = null;
	private ArrayList<FileEntity> fileList = null;

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Object getUserObject() {
		return userObject;
	}

	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	public Object getFileObject() {
		return fileObject;
	}

	public void setFileObject(Object fileObject) {
		this.fileObject = fileObject;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public ArrayList<FileEntity> getFileList() {
		return fileList;
	}

	public void setFileList(ArrayList<FileEntity> fileList) {
		this.fileList = fileList;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

}
