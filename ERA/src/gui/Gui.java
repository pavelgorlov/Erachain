package gui;


import java.awt.TrayIcon.MessageType;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import controller.Controller;
import gui.create.NoWalletFrame;
import gui.create.SettingLangFrame;
import gui.library.MTable;
import lang.Lang;
import settings.Settings;
import utils.SysTray;

public class Gui extends JFrame{

	//private static final long serialVersionUID = 1L;
	private static final long serialVersionUID = 2717571093561259483L;


	private static Gui maingui;
	private MainFrame mainframe;
	public static Gui getInstance() throws Exception
	{
		if(maingui == null)
		{
			maingui = new Gui();
		}
		
		return maingui;
	}
	
	private Gui() throws Exception
	{
		
        
		
			
		 gui.library.library.Set_GUI_Look_And_Feel("");
        
        if(Settings.getInstance().Dump().containsKey("lang"))
        {
        	if(!Settings.getInstance().getLang().equals(Settings.DEFAULT_LANGUAGE))
        	{
	        	File langFile = new File( Settings.getInstance().getLangDir(), Settings.getInstance().getLang() );
				if ( !langFile.isFile() ) {
					new SettingLangFrame();	
				}
        	}
        } 
        else
        {
        	new SettingLangFrame();
        } 
        
        gui.library.library.Set_GUI_Look_And_Feel("");
        
        //CHECK IF WALLET EXISTS
        if(!Controller.getInstance().doesWalletExists())
        {
        	//OPEN WALLET CREATION SCREEN
        	new NoWalletFrame(this);
        } else if (Settings.getInstance().isGuiEnabled())
    	{
    		mainframe =	MainFrame.getInstance();
    		mainframe.setVisible(true);
    	}
        
	}
	
	public static boolean isGuiStarted()
	{
		return maingui != null;
	}
	
	public void onWalletCreated()
	{

		SysTray.getInstance().sendMessage(Lang.getInstance().translate("Wallet Initialized"),
				Lang.getInstance().translate("Your wallet is initialized"), MessageType.INFO);
		if (Settings.getInstance().isGuiEnabled())
			mainframe = MainFrame.getInstance();
	}
	
	public void bringtoFront()
	{
		if(mainframe != null)
		{
			mainframe.toFront();
		}
	}

	public void hideMainFrame()
	{
		if(mainframe != null)
		{
			mainframe.setVisible(false);
		}
	}
	
	public void onCancelCreateWallet() 
	{
		Controller.getInstance().stopAll();
		System.exit(0);
	}
	
	public static <T extends TableModel> MTable createSortableTable(T tableModel, int defaultSort)
	{
		//CREATE TABLE
		MTable table = new MTable(tableModel);
		
		//CREATE SORTER
		TableRowSorter<T> rowSorter = new TableRowSorter<T>(tableModel);
		//drowSorter.setSortsOnUpdates(true);
		
		//DEFAULT SORT DESCENDING
		rowSorter.toggleSortOrder(defaultSort);	
		rowSorter.toggleSortOrder(defaultSort);	
		
		//ADD TO TABLE
		table.setRowSorter(rowSorter);
		
		//RETURN
		return table;
	}

	public static <T extends TableModel> MTable createSortableTable(T tableModel, int defaultSort, RowFilter<T, Object> rowFilter)
	{
		//CREATE TABLE
		MTable table = new MTable(tableModel);
		
		//CREATE SORTER
		TableRowSorter<T> rowSorter = new TableRowSorter<T>(tableModel);
		//rowSorter.setSortsOnUpdates(true);
		rowSorter.setRowFilter(rowFilter);
		
		//DEFAULT SORT DESCENDING
		rowSorter.toggleSortOrder(defaultSort);	
		rowSorter.toggleSortOrder(defaultSort);	
		
		//ADD TO TABLE
		table.setRowSorter(rowSorter);
		
		//RETURN
		return table;
	}
	
}
