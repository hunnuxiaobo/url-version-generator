package cn.szse.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class MainUI extends JFrame {
	private static final long serialVersionUID = -2490119614609833417L;
	
	private FuncPanel funcPanel = new FuncPanel();//功能选项面板
	private ResultPanel resultPanel = new ResultPanel();//结果展示面板
	private ProgressDialog progress;//进度框
	
	public MainUI() {
		this.setTitle("静态资源版本生成器");
		this.setLayout(new BorderLayout());
		this.getContentPane().add(buildMainPanel() , BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(700, 500);
		this.setMinimumSize(new Dimension(700, 500));
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.progress = new ProgressDialog(this.getContentPane());
	}
	
	//构建主面板，容纳“功能面板”和“结果面板”
	private JComponent buildMainPanel() {
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createEmptyBorder(15, 30, 10, 30));
		p.setLayout(new BorderLayout());
		p.add(BorderLayout.NORTH, this.funcPanel);
		p.add(BorderLayout.CENTER, this.resultPanel);
		
		return p;
	}
	
	public void showResult(List<String> files, long costMillis) {
		JTextArea textArea = this.getResultPanel().getTextArea();
		textArea.setText("");
		if(files == null || files.size() < 1) {
			//JOptionPane.showMessageDialog(ui, "无文件被替换！", "消息", JOptionPane.INFORMATION_MESSAGE);
			textArea.setText( "无文件被替换！");
		} else {
			Collections.sort(files);
			for(String f : files) {
				textArea.append(f+"\n");
			}
			textArea.append("\ntotal: "+files.size()+" file"+(files.size() > 1 ? "s":"")+", cost: "+costMillis+" millis");
		}
	}

	public FuncPanel getFuncPanel() {
		return funcPanel;
	}

	public ResultPanel getResultPanel() {
		return resultPanel;
	}
	
	public void showProgress() {
		this.progress.setVisible(true);
	}
	
	public void hiddenProgress() {
		this.progress.setVisible(false);
	}
}
