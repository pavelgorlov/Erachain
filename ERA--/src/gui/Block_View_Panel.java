package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import gui.records.VouchRecordDialog;
import lang.Lang;
import utils.MenuPopupUtil;

public class Block_View_Panel extends JPanel {
	  /**
     * Creates new form Block_View_Panel
     */
	
	int block;
	int trans;
	
	
    public Block_View_Panel(int block, int trans) {
    	this.trans = trans;
    	this.block = block;
        initComponents( block, trans);
    }

  

	/**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents(int block, int trans) {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel_Block = new javax.swing.JLabel();
        jTextField_Block = new javax.swing.JTextField();
        jLabel_Trans = new javax.swing.JLabel();
        jTextField_Trans = new javax.swing.JTextField();
        jButtonVouch = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jLabel_Block.setText(Lang.getInstance().translate("Block")+":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 0);
        add(jLabel_Block, gridBagConstraints);

        jTextField_Block.setEditable(false);
        jTextField_Block.setText("" +block +"-"+ trans);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 8, 0);
        add(jTextField_Block, gridBagConstraints);
        MenuPopupUtil.installContextMenu(jTextField_Block);

        jLabel_Trans.setText(Lang.getInstance().translate("RecNo")+":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 8, 0);
/*        add(jLabel_Trans, gridBagConstraints);

        jTextField_Trans.setEditable(false);
        jTextField_Trans.setText(trans+"");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 8, 0);
        add(jTextField_Trans, gridBagConstraints);
        MenuPopupUtil.installContextMenu(jTextField_Trans);
*/
        jButtonVouch.setText(Lang.getInstance().translate("Vouch"));
 //       gridBagConstraints = new java.awt.GridBagConstraints();
  //      gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
  //      gridBagConstraints.insets = new java.awt.Insets(8, 16, 8, 8);
        add(jButtonVouch, gridBagConstraints);
        
        jButtonVouch.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new  VouchRecordDialog(block, trans);	
			}
        	
        	
        	
        });
        
        
        
        
    }// </editor-fold>                        


    // Variables declaration - do not modify                     
    private javax.swing.JButton jButtonVouch;
    private javax.swing.JLabel jLabel_Block;
    private javax.swing.JLabel jLabel_Trans;
    private javax.swing.JTextField jTextField_Block;
    private javax.swing.JTextField jTextField_Trans;
    // End of variables declaration       



}