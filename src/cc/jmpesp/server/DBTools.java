package cc.jmpesp.server;

import cc.jmpesp.lib.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import cc.jmpesp.lib.User;

public class DBTools {
	DBUtil dbu = new DBUtil();
	Connection con = dbu.getConnection();

	/*
	 * 用户登录
	 */
	public Integer login(User user) throws Exception {
		String sql = "select id from user where username = ? and password = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, user.getUsername());
		ps.setString(2, user.getPassword());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return rs.getInt(1);
		} else {
			return null;
		}
	}

	/*
	 * 用户注册
	 */
	public boolean register(User user) {
		String sql = "insert into user(username,password) value(?,?)";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * 上传文件
	 */
	public boolean uploadFile(FileEntity fe) {
		String sql = "insert into file(user_id,filename,content) VALUES(?,?,?)";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, fe.getUserId());
			ps.setString(2, fe.getFileName());
			ps.setBytes(3, fe.getFileContent());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * 下载文件
	 */
	public byte[] downloadFile(FileEntity fe) throws Exception {
		String sql = "select content from file where id = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, fe.getId());
		ResultSet rs = ps.executeQuery();
		InputStream is = null;
		if (rs.next()) {
			is = rs.getBinaryStream(1);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[is.available()];
			int len;
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			return buffer;
		}
		return null;
	}

	/*
	 * 查看文件
	 */
	public ArrayList<FileEntity> viewFile(FileEntity fe) throws Exception {
		ArrayList<FileEntity> fileList = new ArrayList<>();
		String sql = "select id,filename from file where user_id= ? ";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, fe.getUserId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			fileList.add(new FileEntity(rs.getInt(1), rs.getString(2)));
		}
		return fileList;
	}

	/*
	 * 删除文件
	 */
	public boolean deleteFile(FileEntity fe, int uid) throws Exception {
		String sql = "delete from file where id = ? and user_id = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, fe.getId());
		ps.setInt(2, uid);
		ps.execute();
		if (ps.getUpdateCount() > 0) {
			return true;
		}
		return false;
	}
}
