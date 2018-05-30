package cc.jmpesp.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import cc.jmpesp.lib.User;
import cc.jmpesp.lib.*;

public class ServerThread extends Thread {
	Socket socket = null;

	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			CommandRelay cr = (CommandRelay) ois.readObject();
			try {
				cr = inquire(cr);
				oos.writeObject(cr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CommandRelay inquire(CommandRelay cr) throws Exception {
		User user = (User) cr.getUserObject();
		FileEntity fe = (FileEntity) cr.getFileObject();
		DBTools dbt = new DBTools();
		boolean flag = false;
		switch (cr.getCommand()) {
		case "login":
			flag = dbt.login(user);
			break;
		case "register":
			flag = dbt.register(user);
			break;
		case "upload":
			flag = dbt.uploadFile(fe);
			break;
		case "view":
			ArrayList<FileEntity> fileList = dbt.viewFile(fe);
			if (!fileList.isEmpty()) {
				flag = true;
				cr.setFileList(fileList);
			}
			break;
		case "download":
			byte[] bytes = dbt.downloadFile(fe);
			if (bytes != null) {
				flag = true;
				cr.setBytes(bytes);
			}
			break;
		case "delete":
			flag = dbt.deleteFile(fe);
			break;
		default:
			break;
		}
		cr.setFlag(flag);
		return cr;
	}
}
