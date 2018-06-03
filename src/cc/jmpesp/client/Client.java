package cc.jmpesp.client;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

import cc.jmpesp.lib.*;

public class Client {
	Integer uid = null;
	String password = null;
	Socket socket = null;
	User user = new User();
	FileEntity fe = null;
	private Scanner input = null;
	String host;
	short port;


	public Client(String host, short port) {
		this.host = host;
		this.port = port;
		this.input = new Scanner(System.in);
	}
	

	/*
	 * 发送对象
	 */
	private void sendData(CommandRelay cr) throws Exception {
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(cr);
		oos.flush();
	}

	/*
	 * 接收对象
	 */
	private CommandRelay getData() throws Exception {
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		CommandRelay cr = (CommandRelay) ois.readObject();
		return cr;
	}

	/*
	 * 用户登陆
	 */
	private void login() throws Exception {
		int count = 0;
		String username = null;
		CommandRelay cr = new CommandRelay();
		while (true) {
			System.out.print("请输入帐号: ");
			username = input.next();
			user.setUsername(username);
			System.out.print("请输入密码: ");
			user.setPassword(input.next());
			count++;
			if (count == 3) {
				System.out.println("[!] 密码错误3次,再见");
				System.exit(0);
			}
			cr.setCommand("login");
			cr.setUserObject(user);
			//socket = new Socket("localhost", 8722);
			sendData(cr);
			cr = getData();
			if (cr.isFlag()) {
				this.uid = cr.getUserId();
				break;
			} else {
				System.out.println("[!] 帐号或密码错误");
				continue;
			}
		}
		System.out.println("[*] 登陆成功");
		fileMain();
	}

	/*
	 * 用户注册
	 */
	private void register() throws Exception {
		String confirm = null;
		String username = null;
		CommandRelay cr = new CommandRelay();
		while (true) {
			System.out.print("请输入帐号: ");
			username = input.next();
			System.out.print("请输入密码: ");
			password = input.next();
			System.out.print("请再次输入密码: ");
			confirm = input.next();
			if (!password.equals(confirm)) {
				System.out.println("[*] 两次密码不一致");
				continue;
			}
			user.setUsername(username);
			user.setPassword(password);
			cr.setCommand("register");
			cr.setUserObject(user);
			//socket = new Socket("localhost", 8722);
			sendData(cr);
			cr = getData();
			if (cr.isFlag()) {
				break;
			} else {
				System.out.println("[!] 注册失败");
				continue;
			}
		}
		System.out.println("[*] 注册成功!");
		login();
	}

	/*
	 * 文件上传下载界面
	 */
	private void fileMain() throws Exception {
		String flag = null;
		while (true) {
			System.out.print("\n[1.上传 2.下载 3.查看相册 4.打开图片 5.删除 6.退出]: ");
			flag = input.next();
			switch (flag) {
			case "1":
				upload();
				break;
			case "2":
				download();
				break;
			case "3":
				view();
				break;
			case "4":

				try {
					open();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}   

				break;
			case "5":
				delete();
				break;
			case "6":
				System.out.println("再见!");
				System.exit(0);
				break;
			default:
				System.out.println("[!] 输入不正确");
				continue;
			}
		}
	}
	
	/*
	 * 文件下载
	 */
	@SuppressWarnings("resource")
	private void open() throws Exception {
		System.out.print("图片的编号:");
		FileEntity fe = new FileEntity();
		fe.setId(input.nextInt());
		String folder=System.getProperty("java.io.tmpdir");
		String fileName = folder + "/" + UUID.randomUUID();
		CommandRelay cr = new CommandRelay();
		cr.setCommand("download");
		cr.setFileObject(fe);
		sendData(cr);
		cr = getData();
		if (cr.isFlag()) {
			byte[] bytes = cr.getBytes();
			FileOutputStream out = new FileOutputStream(fileName);
			out.write(bytes);
			Viewer viewer = new Viewer(fileName);
			viewer.setVisible(true);
		} else {
			System.out.println("[!] 下载失败,找不到");
		}
		//fileMain();
		//close();
	}

	/*
	 * 文件上传
	 */
	@SuppressWarnings("resource")
	private void upload() throws Exception {
		System.out.println("待上传文件位置(如 ~/Picture/foo.jpg):");
		String path = input.next();
		String fileName = path.substring(path.lastIndexOf('/') + 1);
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		switch(suffix) {
			case "jpeg":
			case "gif":
			case "jpg":
			case "png":
				break;
			default:
				System.out.println("[!] 不支持的文件格式 `" + suffix + "'");
				return;
		}
		
		FileInputStream fis = new FileInputStream(path);
		byte[] fileBytes = new byte[fis.available()];
		BufferedInputStream bis = new BufferedInputStream(fis);
		bis.read(fileBytes);
		FileEntity fe = new FileEntity();
		fe.setUserId(uid);
		fe.setFileName(fileName);
		fe.setFileContent(fileBytes);
		CommandRelay cr = new CommandRelay();
		cr.setCommand("upload");
		cr.setFileObject(fe);
		sendData(cr);
		cr = getData();
		if (cr.isFlag()) {
			System.out.println("[*] 上传成功");
		} else {
			System.out.println("[!] 上传失败");
		}
	}

	/*
	 * 文件查看
	 */
	private void view() throws Exception {
		FileEntity fe = new FileEntity();
		fe.setUserId(this.uid);
		//fe.setUserId(this.uid);
		CommandRelay cr = new CommandRelay();
		cr.setCommand("view");
		cr.setFileObject(fe);
		//socket = new Socket("localhost", 8722);
		sendData(cr);
		cr = getData();
		if (cr.isFlag()) {
			ArrayList<FileEntity> fileList = cr.getFileList();
			System.out.println("编号\t文件名");
			for (FileEntity fileEntity : fileList) {
				System.out.println(fileEntity);
			}
		} else {
			System.out.println("[*] 无文件");
		}
		//fileMain();
		//close();
	}


	/*
	 * 文件下载
	 */
	@SuppressWarnings("resource")
	private void download() throws Exception {
		System.out.print("请输入要下载文件的编号:");
		FileEntity fe = new FileEntity();
		fe.setId(input.nextInt());
		System.out.println("本地路径:");
		String fileName = input.next();
		CommandRelay cr = new CommandRelay();
		cr.setCommand("download");
		cr.setFileObject(fe);
		//socket = new Socket("localhost", 8722);
		sendData(cr);
		cr = getData();
		if (cr.isFlag()) {
			byte[] bytes = cr.getBytes();
			FileOutputStream out = new FileOutputStream(fileName);
			out.write(bytes);
			System.out.println("[*] 下载成功");
		} else {
			System.out.println("[!] 下载失败,找不到");
		}
		//fileMain();
		//close();
	}

	/*
	 * 删除文件
	 */
	private void delete() throws Exception {
		System.out.print("删除文件的编号:");
		FileEntity fe = new FileEntity();
		fe.setId(input.nextInt());
		CommandRelay cr = new CommandRelay();
		cr.setCommand("delete");
		cr.setFileObject(fe);
		//socket = new Socket("localhost", 8722);
		sendData(cr);
		cr = getData();
		if (cr.isFlag()) {
			System.out.println("[*]删除成功");
		} else {
			System.out.println("[!]删除失败,没有此文件!");
		}
		//fileMain();
		//close();
	}

	/*
	 * 关闭资源
	 */
//	public void close() {
//		input.close();
//		try {
//			if (socket != null) {
//				socket.close();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public void start() throws Exception {
		try {
			this.socket = new Socket(host, port);
		} catch(Exception e) {
			System.out.println("[!] 与服务器连接失败");
			return;
		}
		System.out.println("*** Lord Album ***");
		String flag = null;
		while (true) {
			try {
				System.out.print("\n[1.登陆 2.注册 3.退出] : ");
				flag = this.input.next();
				switch (flag) {
				case "1":
					this.login();
					break;
				case "2":
					this.register();
					break;
				case "3":
					input.close();
					socket.close();
					System.out.println("Bye!");
					return;
				default:
					System.out.println("[!]输入指令不正确,请重新输入");
					continue;
				}
				break;
			} catch(Exception e) {
				System.out.println("Client:" + e);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Client client = new Client("localhost", (short)8722);
		client.start();
	}
}
