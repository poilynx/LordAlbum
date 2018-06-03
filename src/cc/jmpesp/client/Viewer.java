package cc.jmpesp.client;
import java.awt.*;
import javax.swing.*;

public class Viewer extends JFrame {
	private static final long serialVersionUID = 1L;
	// 定义一个类继承JFrame类
	public Viewer (String filename) { // 定义一个CreateJFrame()方法
        JFrame jf = this;//象
        JPanel jp = new JPanel();
        Container container = jf.getContentPane(); // 获取一个容器
        JLabel jl=new JLabel("");
        JScrollPane sp = new JScrollPane(jl);
        ImageIcon img = new ImageIcon(filename);
        jl.setSize(img.getIconWidth(), img.getIconHeight());;
        jl.setIcon(img);
        jl.setHorizontalAlignment(SwingConstants.CENTER);
        container.add(sp);
        container.setBackground(Color.white);//设置容器的背景颜色
        
       jf.setSize(800, 600); // 设置窗体大小
        // 设置窗体关闭方
        jf.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setLocationRelativeTo(getOwner());
        //setResizable(false);

    }
}
