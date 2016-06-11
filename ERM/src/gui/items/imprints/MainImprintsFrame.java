package gui.items.imprints;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JInternalFrame;

import gui.MainFrame;
import gui.Main_Internal_Frame;
import lang.Lang;

public class MainImprintsFrame extends Main_Internal_Frame{

	private static final long serialVersionUID = 1L;

public MainImprintsFrame(){
// not show buttons main Toolbar
	this.setTitle(Lang.getInstance().translate("Imprints"));
	this.jButton2_jToolBar.setVisible(false);
	this.jButton3_jToolBar.setVisible(false);
// buttun1
	this.jButton1_jToolBar.setText(Lang.getInstance().translate("Issue Imprint"));
// status panel
	this.jLabel_status_jPanel.setText(Lang.getInstance().translate("Work with imprints"));
	this.jButton1_jToolBar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new IssueImprintDialog();
			}
	});		
	My_Imprints_Tab my_Imprints_SplitPanel = new My_Imprints_Tab();
	this.jTabbedPane.add(my_Imprints_SplitPanel);
	Search_Imprints_Tab search_Imprints_SplitPanel = new Search_Imprints_Tab();
	this.jTabbedPane.add(search_Imprints_SplitPanel);
	this.pack();
	this.setSize(800,600);
	this.setMaximizable(true);
	this.setClosable(true);
	this.setResizable(true);
	this.setLocation(20, 20);
	setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
	this.setResizable(true);
    this.setVisible(true);
	Dimension size = MainFrame.desktopPane.getSize();
	this.setSize(new Dimension((int)size.getWidth()-100,(int)size.getHeight()-100));
	search_Imprints_SplitPanel.jSplitPanel.setDividerLocation((int)(size.getWidth()/1.618));
	my_Imprints_SplitPanel.jSplitPanel.setDividerLocation((int)(size.getWidth()/1.618));	
	
	}

}