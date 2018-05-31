package cc.jmpesp.client;
import java.awt.*;
import javax.swing.*;

public class Test extends JFrame {
	private static final long serialVersionUID = 1L;
	// 定义一个类继承JFrame类
	public Test () { // 定义一个CreateJFrame()方法
        JFrame jf = this;//象
        Container container = jf.getContentPane(); // 获取一个容器
        JLabel jl=new JLabel("这是一个Jframe窗体");
        jl.setHorizontalAlignment(SwingConstants.CENTER);
        container.add(jl);
        container.setBackground(Color.white);//设置容器的背景颜色
        
        jf.setSize(200, 150); // 设置窗体大小
        // 设置窗体关闭方
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        jf.setVisible(true); // 使窗体可视
    }
    public static void main(String args[]){//在主方法中调用createJFrame()方法
        new Test();
    }
}
