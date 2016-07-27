package cn.szse.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 进度对话框
 * @author bxiao01.oth
 *
 */
public class ProgressDialog extends JDialog{
	private static final long serialVersionUID = 1578050823176607234L;

	public ProgressDialog(Container parent) {
		super();
		this.setSize(new Dimension(130, 60));
		this.setLocationRelativeTo(parent);
		this.add(buildIconPanel());
		this.setUndecorated(true);
		this.setModal(true);
	}
	
	private static JPanel buildIconPanel() {
		URL url = ProgressDialog.class.getResource("/progress.gif");
		ImageIcon icon = new ImageIcon(url);
		icon.setImage(icon.getImage().getScaledInstance(icon.getIconWidth()/4,  
                icon.getIconHeight()/4, Image.SCALE_DEFAULT));
		
		JLabel imglb = new JLabel("正在处理...");
		imglb.setIcon(icon);
		
		JPanel p = new JPanel();
		p.add(imglb);
		p.setBackground(Color.WHITE);
		p.setSize(new Dimension(100, 50));
		
		return p;
	}
	
	public static void main(String args[]) throws InterruptedException {
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setVisible(true);
		
		//dialog
		final ProgressDialog dg = new ProgressDialog(frame.getContentPane());
		dg.setModal(false);
		
		JButton openBnt = new JButton("打开进度框");
		openBnt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dg.setVisible(true);
			}
			
		});
		
		JButton closeBnt = new JButton("关闭进度框");
		closeBnt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dg.setVisible(false);
			}
			
		});
		
		frame.add(openBnt);
		frame.add(closeBnt);
	}
}
