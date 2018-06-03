package cc.jmpesp.server;

import java.io.IOException;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import cc.jmpesp.lib.User;
import cc.jmpesp.lib.*;

public class ServerThread extends Thread {
	private Socket socket = null;
	//private String username;
	private Integer uid = null;

	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			while(true) {
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				CommandRelay cr = (CommandRelay) ois.readObject();
				try {
					cr = inquire(cr);
					oos.writeObject(cr);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (EOFException e) {
			System.out.println("[!] 连接断开");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
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
			uid = dbt.login(user);
			if(uid != null) {
				cr.setUserId(this.uid);
				flag = true;
			} else flag = false;
			break;
		case "register":
			flag = dbt.register(user);
			break;
		case "upload":
			if(this.uid == null) break;
			flag = dbt.uploadFile(fe);
			break;
		case "view":
			if(this.uid == null) break;
			ArrayList<FileEntity> fileList = dbt.viewFile(this.uid);
			if (!fileList.isEmpty()) {
				flag = true;
				cr.setFileList(fileList);
			}
			break;
		case "download":
			if(this.uid == null) break;
			if(dbt.getFileOwner(fe.getId()) != this.uid) {
				cr.setErrorMessage("找不到该文件");
				break;
			}
			byte[] bytes = dbt.downloadFile(fe.getId());
			if (bytes != null) {
				flag = true;
				cr.setBytes(bytes);
			}
			break;
		case "delete":
			if(this.uid == null) break;
			if(dbt.getFileOwner(fe.getId()) != this.uid) {
				cr.setErrorMessage("找不到该文件");
				break;
			}
			//System.out.println(fe.getUserId());
			//System.out.println(this.uid);
			flag = dbt.deleteFile(fe.getId());
			break;
		default:
			break;
		}
		cr.setFlag(flag);
		return cr;
	}
}
