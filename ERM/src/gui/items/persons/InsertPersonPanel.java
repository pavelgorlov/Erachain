package gui.items.persons;

import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import controller.Controller;
import core.account.Account;
import core.account.PrivateKeyAccount;
import core.account.PublicKeyAccount;
import core.crypto.Base58;
import core.item.persons.PersonCls;
import core.item.persons.PersonFactory;
import core.item.persons.PersonHuman;
import core.transaction.IssuePersonRecord;
import core.transaction.Transaction;
import core.transaction.TransactionFactory;
import gui.transaction.OnDealClick;
import lang.Lang;
import utils.Pair;

public class InsertPersonPanel extends IssuePersonPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected JTextField txt_Sign;
	protected JTextField txt_public_key;
	protected JTextField txt_info;
	protected JTextField txtGenderTxt;
	protected JTextField txtBirthdayTxt;
	protected JTextField txtDeathdayTxt;
	
	
	
	protected javax.swing.JButton trans_Button;
    protected javax.swing.JLabel label_Sign;
    protected javax.swing.JLabel label_public_key;
    protected javax.swing.JLabel label_info;
    protected javax.swing.JLabel iconLabel;
    protected javax.swing.JPanel jPanel_Paste;
    protected javax.swing.JButton pasteButton;
    
    
    
    
    //protected IssuePersonRecord issuePersonRecord;
    protected PersonHuman person;
	
	InsertPersonPanel(){
		
		super();
		
		init();	
		
	}
	
	 public String getClipboardContents() {
	    String result = "";
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    //odd: the Object param of getContents is not currently used
	    Transferable contents = clipboard.getContents(null);
	    boolean hasTransferableText =
	      (contents != null) &&
	      contents.isDataFlavorSupported(DataFlavor.stringFlavor)
	    ;
	    if (hasTransferableText) {
	      try {
	        result = (String)contents.getTransferData(DataFlavor.stringFlavor);
	      }
	      catch (Exception e){
	      }
	    }
	    return result;
	  }
	 
@SuppressWarnings("deprecation")
private void init(){
	
	
	txt_Sign  = new javax.swing.JTextField();
	txt_public_key = new javax.swing.JTextField();
	txt_info = new javax.swing.JTextField();
	txtGenderTxt = new javax.swing.JTextField();
	txtBirthdayTxt = new javax.swing.JTextField();
	txtDeathdayTxt = new javax.swing.JTextField();
	
	
	
	
	
	iconLabel = new javax.swing.JLabel();
	label_Sign= new javax.swing.JLabel();
	label_public_key= new javax.swing.JLabel();
	label_info= new javax.swing.JLabel();
	
	
	
	//txtBirthday = new javax.swing.JTextField();
	//txtDeathday = new javax.swing.JTextField();

   	txtRace.setText("");
   	this.txtBirthLatitude.setText("");
   	this.txtBirthLongitude.setText("");
   	this.txtHeight.setText("");
   	this.txtFeePow.setText("0");

	
//	cbxFrom.setEnabled(false);
	//txtFeePow.setEditable(false);
	txtName.setEditable(false);
	txtareaDescription.setEditable(false);
	txtBirthday.setVisible(false); //setEnabled(false);
	txtDeathday.setVisible(false); //setEnabled(false);
	txtBirthdayTxt.setEnabled(false);
	txtDeathdayTxt.setEnabled(false);
	
	copyButton.setVisible(false);
	
	
	iconButton.setVisible(false);
	txtGender.setVisible(false);
	txtGenderTxt.setEditable(false);
	txtRace.setEditable(false);
	txtBirthLatitude.setEditable(false);
	txtBirthLongitude.setEditable(false);
	txtSkinColor.setEditable(false);
	txtEyeColor.setEditable(false);
	txtHairСolor.setEditable(false);
	txtHeight.setEditable(false);
	issueButton.setVisible(false);
	
	txt_public_key.setEditable(false);
	
	
	label_Sign.setText( Lang.getInstance().translate("Signature")+ ":");
    GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 17;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.insets = new java.awt.Insets(0, 18, 0, 0);
    add(label_Sign, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 17;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.weightx = 0.2;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 1);
    txt_Sign.setEditable(false);
    add(txt_Sign, gridBagConstraints);
	
     label_public_key.setText( Lang.getInstance().translate("Public Key")+ ":");
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 18;
     gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
     gridBagConstraints.insets = new java.awt.Insets(0, 18, 0, 0);
     add(label_public_key, gridBagConstraints);
     gridBagConstraints = new java.awt.GridBagConstraints();
     gridBagConstraints.gridx = 2;
     gridBagConstraints.gridy = 18;
     gridBagConstraints.gridwidth = 3;
     gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
     gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
     gridBagConstraints.weightx = 0.2;
     gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 1);
     add(txt_public_key, gridBagConstraints);
     
     gridBagConstraints = new java.awt.GridBagConstraints();
     gridBagConstraints.gridx = 2;
     gridBagConstraints.gridy = 8;
     gridBagConstraints.gridwidth = 3;
     gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
     gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
     gridBagConstraints.weightx = 0.1;
     add(txtGenderTxt, gridBagConstraints);
     
     
    // txtBirthday.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("yyyy-MM-dd"))));
     gridBagConstraints = new java.awt.GridBagConstraints();
     gridBagConstraints.gridx = 2;
     gridBagConstraints.gridy = 6;
     gridBagConstraints.gridwidth = 3;
     gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
     gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
     gridBagConstraints.weightx = 0.2;
     add( txtBirthdayTxt, gridBagConstraints);

//      txtDeathday.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("yyyy-MM-dd"))));
//      txtDeathday.addActionListener(new java.awt.event.ActionListener() {
//           public void actionPerformed(java.awt.event.ActionEvent evt) {
//               txtDeathdayActionPerformed(evt);
//           }
//       });
     
 //    txtDeathday.setDateFormatString("yyyy-MM-dd");
     gridBagConstraints = new java.awt.GridBagConstraints();
     gridBagConstraints.gridx = 8;
     gridBagConstraints.gridy = 6;
     gridBagConstraints.gridwidth = 7;
     gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
     gridBagConstraints.weightx = 0.2;
     gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 16);
     add(txtDeathdayTxt, gridBagConstraints);
     
     
     gridBagConstraints = new java.awt.GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 0;
     gridBagConstraints.gridwidth = 7;
     gridBagConstraints.gridheight = 5;
     gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
     gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
     gridBagConstraints.weightx = 0.05;
     jPanel1.add(iconLabel, gridBagConstraints);
 	 
     
	 pasteButton = new JButton();
     pasteButton.setText(Lang.getInstance().translate("Paste") +"...");
     pasteButton.addActionListener(new ActionListener(){

 		@Override
 		public void actionPerformed(ActionEvent arg0) {

			issueButton.setEnabled(false);
			copyButton.setEnabled(false);

			// TODO Auto-generated method stub
 			person = null;
 			reset();
 			
 			String base58str = getClipboardContents();
 			byte[] dataPerson = Base58.decode(base58str);
 			try {
 				person = (PersonHuman)PersonFactory.getInstance().parse(dataPerson, false);		
 			} catch (Exception ee) {
 				return;
 			}
 			
 			txtName.setText(person.getName());
 			iconLabel.setIcon(new ImageIcon(person.getImage()));

 			txtBirthdayTxt.setText(new Date(person.getBirthday())+ "");
 			long dayTimestamp = person.getDeathday();
 			if ( dayTimestamp > person.getBirthday()) {
 				txtDeathdayTxt.setText(new Date(person.getDeathday())+"");
 			}
 
 			txtareaDescription.setText(person.getDescription()==null?"":person.getDescription());

 			txtGender.setSelectedIndex(person.getGender());
 			txtGenderTxt.setText(txtGender.getSelectedItem().toString());
 			
 			
 			if (person.getRace() != null)
 				txtRace.setText(person.getRace());
 			txtBirthLatitude.setText("" + person.getBirthLatitude());
 			txtBirthLongitude.setText("" + person.getBirthLongitude());
 			if (person.getSkinColor()!= null)
 				txtSkinColor.setText(person.getSkinColor());
 			if (person.getEyeColor() != null)
 				txtEyeColor.setText(person.getEyeColor());
 			if (person.getHairСolor() != null)
 				txtHairСolor.setText(person.getHairСolor());
 			txtHeight.setText("" + person.getHeight());

 			
 			txt_Sign.setText(Base58.encode(person.getOwnerSignature()));
 			txt_public_key.setText(Base58.encode(person.getOwner().getPublicKey()));

 		}
     	 
	     	 
      });
	
     GridBagConstraints gridBagConstraints1 = new java.awt.GridBagConstraints();
     gridBagConstraints1.gridx = 4;
     gridBagConstraints1.gridy = 19;
  //   gridBagConstraints1.gridwidth = ;
     gridBagConstraints1.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
     gridBagConstraints1.insets = new java.awt.Insets(20, 0, 0, 0);
     add(pasteButton, gridBagConstraints1);
	
 
 
    
     trans_Button = new JButton();
     trans_Button.setText(Lang.getInstance().translate("Check")+ " & " + Lang.getInstance().translate("Issue") + "...");
     trans_Button.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {

			issueButton.setEnabled(false);
			copyButton.setEnabled(false);

			//READ CREATOR
			Account creatorAccount = (Account) cbxFrom.getSelectedItem();

			int parse = 0;
			int feePow = 0;

			try
			{
				
				//READ FEE POW
				feePow = Integer.parseInt(txtFeePow.getText());				
				//String b = txtFeePow.getText();
				
			}
			catch(Exception e)
			{
				String mess = "Invalid pars... " + parse;
				switch(parse)
				{
				case 0:
					mess = "Invalid fee power 0..6";
					break;
				}
				JOptionPane.showMessageDialog(new JFrame(), Lang.getInstance().translate(e + mess), Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
				
				issueButton.setEnabled(true);
				copyButton.setEnabled(true);
				return;
			}

			// TODO Auto-generated method stub
			PrivateKeyAccount creator = Controller.getInstance().getPrivateKeyAccountByAddress(creatorAccount.getAddress());
			//PublicKeyAccount owner = (PublicKeyAccount)creator;
			Pair<Transaction, Integer> result = Controller.getInstance().issuePersonHuman(
					creator, feePow, person);
			
			//CHECK VALIDATE MESSAGE
			if (result.getB() == Transaction.VALIDATE_OK) {
			
				JOptionPane.showMessageDialog(new JFrame(), Lang.getInstance().translate(
						"Person issue has been sent!"),
						Lang.getInstance().translate("Success"), JOptionPane.INFORMATION_MESSAGE);
				reset();
				
			} else {		
				JOptionPane.showMessageDialog(new JFrame(), Lang.getInstance().translate(OnDealClick.resultMess(result.getB())), Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
			}
			
			//ENABLE
			issueButton.setEnabled(true);
			copyButton.setEnabled(true);

		}
    	 
	    	 
     });
	
	  
     gridBagConstraints1.gridx = 6;
     gridBagConstraints1.gridy = 19;
 //    gridBagConstraints1.gridwidth = 15;
     gridBagConstraints1.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
     gridBagConstraints1.insets = new java.awt.Insets(20, 0, 0, 16);
     add(trans_Button, gridBagConstraints1);
	
		
		
	}
	
	protected void reset() {
		//txtFeePow.setText("0");
		txtName.setText("");
		txtareaDescription.setText("");
		//txtBirthday.setText("0000-00-00");
		//txtDeathday.setText("0000-00-00");
		
		txtGender.setSelectedIndex(2);
		txtGenderTxt.setText("");
		txtRace.setText("");
		txtBirthLatitude.setText("");
		txtBirthLongitude.setText("");
		txtSkinColor.setText("");
		txtEyeColor.setText("");
		txtHairСolor.setText("");
		txtHeight.setText("");
		imgButes = null;
		iconButton.setIcon(null);
	
	}


}