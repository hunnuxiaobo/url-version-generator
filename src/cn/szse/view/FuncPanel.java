package cn.szse.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 功能选项面板，容纳组件有：
 * 1，搜索路径、文件选择按钮；
 * 2，文件类型选项；
 * 3，资源类型选项；
 * 4，执行按钮。
 * @author bxiao01.oth
 *
 */
public class FuncPanel extends JPanel {
	private static final long serialVersionUID = 100801846827553513L;
	
	//搜索路径
	private JLabel searchDirLabel = new JLabel("搜索路径：");
	private JTextField searchDirField = new JTextField(32);
	private JButton fileSelectButton = new JButton("浏览..");
	
	//文件类型
	private JLabel fileTypesLabel = new JLabel("文件类型：");
	private List<JCheckBox> fileTypes = new ArrayList<JCheckBox>();
	private JTextField fileTypesField = new JTextField(15);
	private String tips = "请输入文件后缀名,以逗号分隔.";
	
	//资源类型
	private JLabel rsrcTypesLabel = new JLabel("资源类型：");
	private List<JCheckBox> rscTypes = new ArrayList<JCheckBox>();
	
	//执行按钮
	private JButton execButton = new JButton("执行");
	

	public FuncPanel() {
		this.setLayout(new BorderLayout());
		this.setMinimumSize(new Dimension(200, 70));
		this.setMaximumSize(new Dimension(200, 70));
		this.setBorder(BorderFactory.createEtchedBorder());//设置环绕框线
		this.add(BorderLayout.WEST, buildInputPanel());//设置功能选项面板
		this.add(BorderLayout.CENTER, buildExecButtonPanel());//设置执行按钮面板
	}
	
	//获取选定的文件类型
	public Set<String> getSelectedFilePostfix() {
		Set<String> res = new HashSet<String>();
		for(JCheckBox cb : fileTypes) {
			if(cb.isSelected()) res.add(cb.getText());
		}
		
		if(!tips.equals(fileTypesField.getText())) {
			for(String t : fileTypesField.getText().split(",")) {
				if(!t.trim().equals("")) res.add(t.trim().toLowerCase());
			}
		}
		
		return res;
	}
	
	//获取选定的资源类型
	public Set<String> getSelectedResourceType() {
		Set<String> res = new HashSet<String>();
		for(JCheckBox cb : rscTypes) {
			if(cb.isSelected())
				res.add(cb.getText());
		}
		
		return res;
	}
	
	private JPanel buildInputPanel() {
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.add(BorderLayout.NORTH, buildSearchPanel());
		inputPanel.add(BorderLayout.CENTER, buildFileTypesPanel());
		inputPanel.add(BorderLayout.SOUTH, buildRsrcTypesPanel());
		
		return inputPanel;
	}
	
	//构建搜索路径面板
	private JPanel buildSearchPanel() {
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(searchDirLabel);
		p.add(searchDirField);
		fileSelectButton.setPreferredSize(new Dimension(68, 20));
		p.add(fileSelectButton);
		
		return p;
	}
	
	//构建文件类型面板
	private JPanel buildFileTypesPanel() {
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(fileTypesLabel);
		this.buildFileCheckBoxs(p);
		p.add(this.buildFileTypesTextField());
		
		return p;
	}
	
	private void buildFileCheckBoxs(JPanel p) {
		JCheckBox cb1 = new JCheckBox("html", true);
		this.fileTypes.add(cb1);
		JCheckBox cb2 = new JCheckBox("jsp", true);
		this.fileTypes.add(cb2);
		JCheckBox cb3 = new JCheckBox("css", true);
		this.fileTypes.add(cb3);
		JCheckBox cb4 = new JCheckBox("aspx", true);
		this.fileTypes.add(cb4);
		JCheckBox cb5 = new JCheckBox("php", true);
		this.fileTypes.add(cb5);
		
		for(JCheckBox cb : fileTypes) {
			p.add(cb);
		}
	}
	
	private JTextField buildFileTypesTextField() {
		fileTypesField.setToolTipText(tips);
		fileTypesField.setText(tips);
		fileTypesField.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(tips.equals(fileTypesField.getText())) {
					fileTypesField.setText("");
				}
			}
		});
		return fileTypesField;
	}
	
	//构建资源类型面板
	private JPanel buildRsrcTypesPanel() {
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 235));
		p.setLayout(new FlowLayout());
		p.add(rsrcTypesLabel);
		this.buildRsrcCheckBoxs(p);
		
		return p;
	}
	
	private void buildRsrcCheckBoxs(JPanel p) {
		JCheckBox cb1 = new JCheckBox("script", true);
		this.rscTypes.add(cb1);
		JCheckBox cb2 = new JCheckBox("link", true);
		this.rscTypes.add(cb2);
		JCheckBox cb3 = new JCheckBox("img", true);
		this.rscTypes.add(cb3);
		JCheckBox cb4 = new JCheckBox("a", false);
		this.rscTypes.add(cb4);
		
		for(JCheckBox cb : rscTypes) {
			p.add(cb);
		}
	}
	
	private JPanel buildExecButtonPanel() {
		execButton.setToolTipText("执行版本替换操作");
		execButton.setPreferredSize(new Dimension(80, 60));
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(execButton);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
		
		return buttonPanel;
	}

	public JTextField getSearchDirField() {
		return searchDirField;
	}

	public JButton getFileSelectButton() {
		return fileSelectButton;
	}

	public JButton getExecButton() {
		return execButton;
	}

	public List<JCheckBox> getFileTypes() {
		return fileTypes;
	}

	public String getTips() {
		return tips;
	}
	
}
