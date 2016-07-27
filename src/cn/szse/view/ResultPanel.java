package cn.szse.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 执行版本生成/替换操作后，结果展示页面。
 * @author bxiao01.oth
 *
 */
public class ResultPanel extends JPanel {
	private static final long serialVersionUID = 20622014726548294L;
	
	private JLabel title = new JLabel("被替换文件列表：");
	private JTextArea textArea = new JTextArea(30, 30);

	public ResultPanel() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		this.add(BorderLayout.NORTH, title);
		this.add(BorderLayout.CENTER, buildTextArea());
	}
	
	//构建可滚动文本框
	private JScrollPane buildTextArea() {
		textArea.setColumns (0); 
		textArea.setRows (0);
		textArea.setEditable(false);
		JScrollPane panel = new JScrollPane(textArea);
		panel.setPreferredSize(new Dimension(50, 100));
		
		return panel;
	}

	public JTextArea getTextArea() {
		return textArea;
	}
	
}
