package cn.szse.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import cn.szse.model.CoreFacade;
import cn.szse.view.FuncPanel;
import cn.szse.view.MainUI;

/**
 * 控制器：核心逻辑模型和用户界面组件之间的连接器，隔离业务层与展示层。
 * @author bxiao01.oth
 *
 */
public class Controller {
	public static void main(String args[]) throws Exception {
		//设定皮肤风格
		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		
		//初始化用户界面
		final MainUI ui = new MainUI();
		
		//绑定“选择文件”按钮事件
		ui.getFuncPanel().getFileSelectButton().addActionListener(buildSelectBtnEvent(ui.getFuncPanel()));
		
		//绑定“执行”按钮事件
		ui.getFuncPanel().getExecButton().addActionListener(buildExecBtnEvent(ui));
	}
	
	private static ActionListener buildSelectBtnEvent(final FuncPanel p) {
		return new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                if(fileChooser.showOpenDialog(p) == JFileChooser.APPROVE_OPTION) {//判断是否为打开的按钮
                    File selectedFile = fileChooser.getSelectedFile();  //取得选中的文件/目录
                    p.getSearchDirField().setText(selectedFile.getPath());   //取得路径
                }
			}
		};
	}
	
	private static ActionListener buildExecBtnEvent(final MainUI ui) {
		return new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//读取选项参数
				final String filePath = ui.getFuncPanel().getSearchDirField().getText();
				final Set<String> postfixSet = ui.getFuncPanel().getSelectedFilePostfix();
				final Set<String> typeSet = ui.getFuncPanel().getSelectedResourceType();
				
				//验证选项参数
				if(!validateAndPopMsg(filePath, postfixSet, typeSet)) 
					return ;
				
				ui.getFuncPanel().getExecButton().setEnabled(false);//禁用按钮
				
				startHandlerThread(filePath, postfixSet, typeSet);//启动处理线程
				
				ui.showProgress();//打开进度框
			}
			
			private void startHandlerThread(final String filePath, final Set<String> postfixSet, final Set<String> typeSet) {
				new Thread() {
					@Override
					public void run() {
						try {
							long start = System.currentTimeMillis();
							CoreFacade bgCore = new CoreFacade();
							bgCore.search(filePath, postfixSet);//搜索指定目录指定类型文件
							bgCore.handle(typeSet);//处理指定静态资源类型
							long cost = System.currentTimeMillis() - start;
							
							ui.showResult(bgCore.getTargetFiles(), cost);
							
							JOptionPane.showMessageDialog(ui, "处理完成！", "消息", JOptionPane.INFORMATION_MESSAGE);
						} catch(Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(ui, "程序错误！ERROR:"+e.getMessage(), "异常", JOptionPane.ERROR_MESSAGE);
						} finally {
							ui.getFuncPanel().getExecButton().setEnabled(true);//启动按钮
							ui.hiddenProgress();//关闭进度框
						}
					}
				}.start();
			}
			
			private boolean validateAndPopMsg(String filePath, Set<String> postfixSet, Set<String> typeSet) {
				if(filePath == null || filePath.trim().equals("")) {
					JOptionPane.showMessageDialog(ui, "请选择搜索路径或文件！", "消息", JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
				if(postfixSet == null || postfixSet.isEmpty()) {
					JOptionPane.showMessageDialog(ui, "请至少指定一个文件类型！", "消息", JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
				if(typeSet == null || typeSet.isEmpty()) {
					JOptionPane.showMessageDialog(ui, "请至少指定一个资源类型！", "消息", JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
				return true;
			}
		};
	}
}
