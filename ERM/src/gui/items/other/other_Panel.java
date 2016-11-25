package gui.items.other;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.mapdb.Fun.Tuple2;

import database.wallet.BlockMap;
import gui.CoreRowSorter;
import gui.MainFrame;
import gui.models.BlocksTableModel;
import gui.models.PeersTableModel;
import gui.models.Renderer_Left;
import gui.models.Renderer_Right;
import gui.models.WalletBlocksTableModel;
import gui.models.WalletTransactionsTableModel;
import gui.records.RecordsPanel;
import lang.Lang;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Саша
 */
public class other_Panel extends javax.swing.JPanel {

    private PeersTableModel peersTableModel;
	//private JTable peersTable;
	private BlocksTableModel All_Blocks_TableModel;
	private WalletBlocksTableModel blocksModel;
	private WalletTransactionsTableModel transactionsModel;
	private RecordsPanel record_Panel;
	/**
     * Creates new form other_Panel
     */
    public other_Panel() {
    // peers table	
    	 this.peersTableModel = new PeersTableModel();
         this.jTable_Peers = new JTable(this.peersTableModel);
        
         this.jTable_Peers.setDefaultRenderer(Long.class, new Renderer_Right()); // set renderer
    //     Renderer_Left ren = new Renderer_Left(this.jTable_Peers.getFontMetrics(this.jTable_Peers.getFont()),peersTableModel.get_Column_AutoHeight())); // set renderer
         this.jTable_Peers.setDefaultRenderer(String.class, new Renderer_Left(this.jTable_Peers.getFontMetrics(this.jTable_Peers.getFont()),peersTableModel.get_Column_AutoHeight())); // set renderer
         this.jTable_Peers.setDefaultRenderer(Tuple2.class, new Renderer_Left(this.jTable_Peers.getFontMetrics(this.jTable_Peers.getFont()),peersTableModel.get_Column_AutoHeight())); // set renderer
         this.jTable_Peers.setEnabled(false);
         
         
      
         
         
    // all block table
         this.All_Blocks_TableModel = new BlocksTableModel(true);
		this.jTable_All_Block = new JTable(this.All_Blocks_TableModel);
		 this.jTable_All_Block.setDefaultRenderer(Long.class, new Renderer_Right()); // set renderer
         this.jTable_All_Block.setDefaultRenderer(String.class, new Renderer_Left(  this.jTable_All_Block.getFontMetrics(  this.jTable_All_Block.getFont()),All_Blocks_TableModel.get_Column_AutoHeight())); // set renderer
         this.jTable_All_Block.setDefaultRenderer(Tuple2.class, new Renderer_Left(  this.jTable_All_Block.getFontMetrics(  this.jTable_All_Block.getFont()),All_Blocks_TableModel.get_Column_AutoHeight())); // set renderer
         this.jTable_All_Block.setDefaultRenderer(Integer.class, new Renderer_Right()); // set renderer
         this.jTable_All_Block.setEnabled(false);
		
		
		
	// my block
Map<Integer, Integer> indexes = new TreeMap<Integer, Integer>();
		
		CoreRowSorter sorter = new CoreRowSorter(transactionsModel, indexes);
		
		//TRANSACTIONS
		this.blocksModel = new WalletBlocksTableModel();
		this.jTable_My_Block = new JTable(blocksModel);
				
		//TRANSACTIONS SORTER
		indexes = new TreeMap<Integer, Integer>();
		indexes.put(WalletBlocksTableModel.COLUMN_HEIGHT, BlockMap.TIMESTAMP_INDEX);
		indexes.put(WalletBlocksTableModel.COLUMN_TIMESTAMP, BlockMap.TIMESTAMP_INDEX);
		indexes.put(WalletBlocksTableModel.COLUMN_GENERATOR, BlockMap.GENERATOR_INDEX);
		indexes.put(WalletBlocksTableModel.COLUMN_BASETARGET, BlockMap.BALANCE_INDEX);
		indexes.put(WalletBlocksTableModel.COLUMN_TRANSACTIONS, BlockMap.TRANSACTIONS_INDEX);
		indexes.put(WalletBlocksTableModel.COLUMN_FEE, BlockMap.FEE_INDEX);
		sorter = new CoreRowSorter(blocksModel, indexes);
		jTable_My_Block.setRowSorter(sorter);
		 this.jTable_My_Block.setDefaultRenderer(Long.class, new Renderer_Right()); // set renderer
		 this.jTable_My_Block.setDefaultRenderer(Integer.class, new Renderer_Right()); // set renderer
         this.jTable_My_Block.setDefaultRenderer(String.class, new Renderer_Left( this.jTable_My_Block.getFontMetrics( this.jTable_My_Block.getFont()),blocksModel.get_Column_AutoHeight())); // set renderer
         this.jTable_My_Block.setDefaultRenderer(Tuple2.class, new Renderer_Left( this.jTable_My_Block.getFontMetrics( this.jTable_My_Block.getFont()),blocksModel.get_Column_AutoHeight())); // set renderer
         this.jTable_My_Block.setEnabled(false);
		
		
	//	jScrollPanel_LeftPanel.setViewportView(jTable_jScrollPanel_LeftPanel);
	//	setRowHeightFormat(true);
         
 // panel records
		
		this.record_Panel = new RecordsPanel();
        initComponents();
        
        this.jLabel_All_Block.setText(Lang.getInstance().translate("Last 100 blocks"));
        this.jLabel_Peer_Title.setText(Lang.getInstance().translate("Peers"));
        this.jLabel_Transaction_Title.setText(Lang.getInstance().translate("My Transactions"));
        this.jLabel_My_Block_Title.setText(Lang.getInstance().translate("My Generated Blocks"));
        
        Dimension size = MainFrame.desktopPane.getSize();
        //this.setSize(new Dimension((int)size.getWidth()-100,(int)size.getHeight()-100));
	    //split_generated_Block.jSplitPanel.setDividerLocation((int)(size.getWidth()/2));
        int splitP2_Div_LOC = (int)((size.getHeight()-100)*.15);
        if (splitP2_Div_LOC <90) splitP2_Div_LOC =90;
     //   splitP2_Div_LOC =90;
        this.jSplitPane2.setDividerLocation(splitP2_Div_LOC);
        
        this.jSplitPane3.setDividerLocation((int)((size.getHeight()-100)*.4));
        this.jSplitPane5.setDividerLocation((int)((size.getWidth()-100)*.5));
       
//		this.addTab(Lang.getInstance().translate("Peers"), new JScrollPane(new JTable(this.peersTableModel)));	
//ADD BLOCK TABLE
	//	this.jScrollPane_Peers_Table.add(this.peersTable);
	//	this.jScrollPane_Peers_Table.setViewportView(this.peersTable);
	//	split_Peers.setRowHeightFormat(true);	
        
        
        
    }
 /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLayeredPane1 = new javax.swing.JLayeredPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel_Peer_Title = new javax.swing.JLabel();
        jScrollPane_Peers_Table = new javax.swing.JScrollPane();
      //  jTable_Peers = new javax.swing.JTable();
        jSplitPane3 = new javax.swing.JSplitPane();
        jSplitPane4 = new javax.swing.JSplitPane();
        jPanel5 = new javax.swing.JPanel();
        jSplitPane5 = new javax.swing.JSplitPane();
        jPanel7 = new javax.swing.JPanel();
        jLabel_My_Block_Title = new javax.swing.JLabel();
        jScrollPane_My_Block_Table = new javax.swing.JScrollPane();
     //   jTable_My_Block = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel_All_Block = new javax.swing.JLabel();
        jScrollPane_All_Block_Table = new javax.swing.JScrollPane();
    //    jTable_All_Block = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jLabel_Transaction_Title = new javax.swing.JLabel();
    //    jSplitPane6 = new javax.swing.JSplitPane();
        jScrollPane_Transaction_Table = new javax.swing.JScrollPane();
        jTable_My_Records = new javax.swing.JTable();
        jScrollPane_Transaction_Info = new javax.swing.JScrollPane();

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setLayout(new java.awt.GridBagLayout());

        jSplitPane2.setBorder(null);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel_Peer_Title.setText("jLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(9, 11, 0, 11);
        jPanel2.add(jLabel_Peer_Title, gridBagConstraints);

        jScrollPane_Peers_Table.setBorder(null);

       
        jScrollPane_Peers_Table.setViewportView(jTable_Peers);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(7, 11, 11, 11);
        jPanel2.add(jScrollPane_Peers_Table, gridBagConstraints);

        jSplitPane2.setTopComponent(jPanel2);

        jSplitPane3.setBorder(null);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel5.setLayout(new java.awt.GridBagLayout());
        jSplitPane4.setLeftComponent(jPanel5);

        jSplitPane3.setTopComponent(jSplitPane4);

        jSplitPane5.setBorder(null);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jLabel_My_Block_Title.setText("jLabel4");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 11, 0, 11);
        jPanel7.add(jLabel_My_Block_Title, gridBagConstraints);

        jScrollPane_My_Block_Table.setBorder(null);

        jScrollPane_My_Block_Table.setViewportView(jTable_My_Block);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 11, 11, 11);
        jPanel7.add(jScrollPane_My_Block_Table, gridBagConstraints);

        jSplitPane5.setLeftComponent(jPanel7);

        jPanel8.setLayout(new java.awt.GridBagLayout());

        jLabel_All_Block.setText("jLabel5");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(11, 11, 0, 11);
        jPanel8.add(jLabel_All_Block, gridBagConstraints);

        jScrollPane_All_Block_Table.setBorder(null);

   
        jScrollPane_All_Block_Table.setViewportView(jTable_All_Block);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(6, 11, 11, 11);
        jPanel8.add(jScrollPane_All_Block_Table, gridBagConstraints);

        jSplitPane5.setRightComponent(jPanel8);

        jSplitPane3.setRightComponent(jSplitPane5);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jLabel_Transaction_Title.setText("jLabel3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(11, 11, 0, 11);
        jPanel6.add(jLabel_Transaction_Title, gridBagConstraints);

   //     jSplitPane6.setBorder(null);

        jScrollPane_Transaction_Table.setBorder(null);

        jTable_My_Records.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane_Transaction_Table.setViewportView(jTable_My_Records);

  //      jSplitPane6.setLeftComponent(jScrollPane_Transaction_Table);

        jScrollPane_Transaction_Info.setBorder(null);
   //     jSplitPane6.setRightComponent(jScrollPane_Transaction_Info);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(6, 11, 12, 11);
        jPanel6.add(record_Panel, gridBagConstraints);

        jSplitPane3.setTopComponent(jPanel6);

        jSplitPane2.setRightComponent(jSplitPane3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        add(jSplitPane2, gridBagConstraints);
    }// </editor-fold>                        


    // Variables declaration - do not modify                     
    private javax.swing.JLabel jLabel_All_Block;
    private javax.swing.JLabel jLabel_My_Block_Title;
    private javax.swing.JLabel jLabel_Peer_Title;
    private javax.swing.JLabel jLabel_Transaction_Title;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane_All_Block_Table;
    private javax.swing.JScrollPane jScrollPane_My_Block_Table;
    private javax.swing.JScrollPane jScrollPane_Peers_Table;
    private javax.swing.JScrollPane jScrollPane_Transaction_Info;
    private javax.swing.JScrollPane jScrollPane_Transaction_Table;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JSplitPane jSplitPane5;
//    private javax.swing.JSplitPane jSplitPane6;
    private javax.swing.JTable jTable_All_Block;
    private javax.swing.JTable jTable_My_Block;
    private javax.swing.JTable jTable_My_Records;
    private javax.swing.JTable jTable_Peers;         



    /**
     * Добавляет в стандартный рендерер ячейки таблицы черезстрочную подсветку
     * @param renderer рендерер
     * @param row строка
     * @param column столбец
     * @return новый рендерер
     */
   

}