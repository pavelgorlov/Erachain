package test.records;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.mapdb.Fun.Tuple2;
import org.mapdb.Fun.Tuple3;

import ntp.NTP;

import org.junit.Test;

import core.BlockChain;
import core.account.Account;
import core.account.PrivateKeyAccount;
import core.account.PublicKeyAccount;
import core.block.GenesisBlock;
import core.crypto.Crypto;
import core.item.ItemCls;
import core.item.ItemFactory;
import core.item.unions.UnionCls;
import core.item.unions.Union;
import core.item.statuses.StatusCls;
import core.transaction.IssueUnionRecord;
import core.transaction.Transaction;
import core.transaction.TransactionFactory;
import core.wallet.Wallet;

//import com.google.common.primitives.Longs;

import database.DBSet;
//import database.AddressUnionMap;
//import database.UnionAddressMap;
import database.KKUnionStatusMap;

public class TestRecUnion {

	static Logger LOGGER = Logger.getLogger(TestRecUnion.class.getName());

	Long releaserReference = null;

	BigDecimal BG_ZERO = BigDecimal.ZERO.setScale(8);
	long ERM_KEY = Transaction.RIGHTS_KEY;
	long FEE_KEY = Transaction.FEE_KEY;
	//long ALIVE_KEY = StatusCls.ALIVE_KEY;
	byte FEE_POWER = (byte)1;
	byte[] unionReference = new byte[64];
	long timestamp = NTP.getTime();
	
	private byte[] icon = new byte[]{1,3,4,5,6,9}; // default value
	private byte[] image = new byte[]{4,11,32,23,45,122,11,-45}; // default value

	//CREATE EMPTY MEMORY DATABASE
	private DBSet db;
	private GenesisBlock gb;
	
	//CREATE KNOWN ACCOUNT
	byte[] seed = Crypto.getInstance().digest("test".getBytes());
	byte[] privateKey = Crypto.getInstance().createKeyPair(seed).getA();
	PrivateKeyAccount certifier = new PrivateKeyAccount(privateKey);

	//GENERATE ACCOUNT SEED
	int nonce = 1;
	//byte[] accountSeed;
	//core.wallet.Wallet.generateAccountSeed(byte[], int)
	byte[] accountSeed1 = Wallet.generateAccountSeed(seed, nonce++);
    PrivateKeyAccount userAccount1 = new PrivateKeyAccount(accountSeed1);
    String userAddress1 = userAccount1.getAddress();
    byte[] accountSeed2 = Wallet.generateAccountSeed(seed, nonce++);
    PrivateKeyAccount userAccount2 = new PrivateKeyAccount(accountSeed2);
    String userAddress2 = userAccount2.getAddress();
    byte[] accountSeed3 = Wallet.generateAccountSeed(seed, nonce++);
    PrivateKeyAccount userAccount3 = new PrivateKeyAccount(accountSeed3);
    String userAddress3 = userAccount3.getAddress();

    List<PrivateKeyAccount> sertifiedPrivateKeys = new ArrayList<PrivateKeyAccount>();
    List<PublicKeyAccount> sertifiedPublicKeys = new ArrayList<PublicKeyAccount>();
    	
	UnionCls unionGeneral;
	UnionCls union;
	long unionKey = -1;
	IssueUnionRecord issueUnionTransaction;

	KKUnionStatusMap dbPS;
	//UnionAddressMap dbPA;
	//AddressUnionMap dbAP;

	int version = 0;
	long parent = -1;
	
	// INIT UNIONS
	private void init() {
		
		db = DBSet.createEmptyDatabaseSet();

		gb = new GenesisBlock();
		gb.process(db);

		//dbPA = db.getUnionAddressMap();
		//dbAP = db.getAddressUnionMap();
		dbPS = db.getUnionStatusMap();

		// GET RIGHTS TO CERTIFIER
		unionGeneral = new Union(certifier, "СССР", timestamp - 12345678,
				parent, icon, image, "Союз Совестких Социалистических Республик");
		//GenesisIssueUnionRecord genesis_issue_union = new GenesisIssueUnionRecord(unionGeneral, certifier);
		//genesis_issue_union.process(db, false);
		//GenesisCertifyUnionRecord genesis_certify = new GenesisCertifyUnionRecord(certifier, 0L);
		//genesis_certify.process(db, false);
		
		certifier.setLastReference(gb.getTimestamp(db), db);
		certifier.changeBalance(db, false, ERM_KEY, BlockChain.MAJOR_ERA_BALANCE_BD);
		certifier.changeBalance(db, false, FEE_KEY, BigDecimal.valueOf(1).setScale(8));
		
		union = new Union(certifier, "РСФСР", timestamp - 1234567,
				parent + 1, icon, image, "Россия");

		//CREATE ISSUE UNION TRANSACTION
		issueUnionTransaction = new IssueUnionRecord(certifier, union, FEE_POWER, timestamp, certifier.getLastReference(db));

		sertifiedPrivateKeys.add(userAccount1);
		sertifiedPrivateKeys.add(userAccount2);
		sertifiedPrivateKeys.add(userAccount3);
		
	    sertifiedPublicKeys.add( new PublicKeyAccount(userAccount1.getPublicKey()));
	    sertifiedPublicKeys.add( new PublicKeyAccount(userAccount2.getPublicKey()));
	    sertifiedPublicKeys.add( new PublicKeyAccount(userAccount3.getPublicKey()));

	}
	
	public void initUnionalize() {


		assertEquals(Transaction.VALIDATE_OK, issueUnionTransaction.isValid(db, releaserReference));

		issueUnionTransaction.sign(certifier, false);
		
		issueUnionTransaction.process(db, gb, false);
		unionKey = union.getKey(db);

		assertEquals( 1, unionKey);
		//assertEquals( null, dbPS.getItem(unionKey));
		
	}
	
	//ISSUE UNION TRANSACTION
	
	@Test
	public void validateSignatureIssueUnionRecord() 
	{
		
		init();
						
		issueUnionTransaction.sign(certifier, false);

		//CHECK IF ISSUE UNION TRANSACTION IS VALID
		assertEquals(true, issueUnionTransaction.isSignatureValid());
		
		//INVALID SIGNATURE
		issueUnionTransaction = new IssueUnionRecord(certifier, union, FEE_POWER, timestamp, certifier.getLastReference(db), new byte[64]);		
		//CHECK IF ISSUE UNION IS INVALID
		assertEquals(false, issueUnionTransaction.isSignatureValid());

	}
		
	
	@Test
	public void validateIssueUnionRecord() 
	{

		init();
		
		issueUnionTransaction.sign(certifier, false);

		//CHECK IF ISSUE UNION IS VALID
		assertEquals(Transaction.VALIDATE_OK, issueUnionTransaction.isValid(db, releaserReference));

		//CREATE INVALID ISSUE UNION - INVALID UNIONALIZE
		issueUnionTransaction = new IssueUnionRecord(userAccount1, union, FEE_POWER, timestamp, userAccount1.getLastReference(db), new byte[64]);		
		assertEquals(Transaction.NOT_ENOUGH_FEE, issueUnionTransaction.isValid(db, releaserReference));
		// ADD FEE
		userAccount1.changeBalance(db, false, FEE_KEY, BigDecimal.valueOf(1).setScale(8));
		assertEquals(Transaction.CREATOR_NOT_PERSONALIZED, issueUnionTransaction.isValid(db, releaserReference));

		//CHECK IF ISSUE UNION IS VALID
		userAccount1.changeBalance(db, false, ERM_KEY, BlockChain.MINOR_ERA_BALANCE_BD);
		assertEquals(Transaction.CREATOR_NOT_PERSONALIZED, issueUnionTransaction.isValid(db, releaserReference));

		//CHECK 
		userAccount1.changeBalance(db, false, ERM_KEY, BlockChain.MAJOR_ERA_BALANCE_BD);
		assertEquals(Transaction.VALIDATE_OK, issueUnionTransaction.isValid(db, releaserReference));

	}

	
	@Test
	public void parseIssueUnionRecord() 
	{
		
		init();
		
		LOGGER.info("union: " + union.getType()[0] + ", " + union.getType()[1]);

		// PARSE UNION
		
		byte [] rawUnion = union.toBytes(false, false);
		assertEquals(rawUnion.length, union.getDataLength(false));
		union.setReference(new byte[64]);
		rawUnion = union.toBytes(true, false);
		assertEquals(rawUnion.length, union.getDataLength(true));
		
		rawUnion = union.toBytes(false, false);
		UnionCls parsedUnion = null;
		try 
		{	
			//PARSE FROM BYTES
			parsedUnion = (UnionCls) ItemFactory.getInstance()
					.parse(ItemCls.UNION_TYPE, rawUnion, false);
		}
		catch (Exception e) 
		{
			fail("Exception while parsing transaction.  : " + e);
		}
		assertEquals(rawUnion.length, union.getDataLength(false));
		assertEquals(union.getOwner().getAddress(), parsedUnion.getOwner().getAddress());
		assertEquals(union.getName(), parsedUnion.getName());
		assertEquals(union.getDescription(), parsedUnion.getDescription());
		assertEquals(union.getItemTypeStr(), parsedUnion.getItemTypeStr());
		assertEquals(union.getBirthday(), parsedUnion.getBirthday());			
		assertEquals(union.getParent(), parsedUnion.getParent());
		
		// PARSE ISSEU UNION RECORD
		issueUnionTransaction.sign(certifier, false);
		issueUnionTransaction.process(db, gb, false);
		
		//CONVERT TO BYTES
		byte[] rawIssueUnionRecord = issueUnionTransaction.toBytes(true, null);
		
		//CHECK DATA LENGTH
		assertEquals(rawIssueUnionRecord.length, issueUnionTransaction.getDataLength(false));
		
		IssueUnionRecord parsedIssueUnionRecord = null;
		try 
		{	
			//PARSE FROM BYTES
			parsedIssueUnionRecord = (IssueUnionRecord) TransactionFactory.getInstance().parse(rawIssueUnionRecord, releaserReference);			

		}
		catch (Exception e) 
		{
			fail("Exception while parsing transaction.  : " + e);
		}
		
		//CHECK INSTANCE
		assertEquals(true, parsedIssueUnionRecord instanceof IssueUnionRecord);
		
		//CHECK SIGNATURE
		assertEquals(true, Arrays.equals(issueUnionTransaction.getSignature(), parsedIssueUnionRecord.getSignature()));
		
		//CHECK ISSUER
		assertEquals(issueUnionTransaction.getCreator().getAddress(), parsedIssueUnionRecord.getCreator().getAddress());
		
		parsedUnion = (Union)parsedIssueUnionRecord.getItem();

		//CHECK OWNER
		assertEquals(union.getOwner().getAddress(), parsedUnion.getOwner().getAddress());
		
		//CHECK NAME
		assertEquals(union.getName(), parsedUnion.getName());
			
		//CHECK REFERENCE
		assertEquals(issueUnionTransaction.getReference(), parsedIssueUnionRecord.getReference());	
		
		//CHECK TIMESTAMP
		assertEquals(issueUnionTransaction.getTimestamp(), parsedIssueUnionRecord.getTimestamp());				

		//CHECK DESCRIPTION
		assertEquals(union.getDescription(), parsedUnion.getDescription());
						
		assertEquals(union.getItemTypeStr(), parsedUnion.getItemTypeStr());
		assertEquals(union.getBirthday(), parsedUnion.getBirthday());			
		assertEquals(union.getParent(), parsedUnion.getParent());

		//PARSE TRANSACTION FROM WRONG BYTES
		rawIssueUnionRecord = new byte[issueUnionTransaction.getDataLength(false)];
		
		try 
		{	
			//PARSE FROM BYTES
			TransactionFactory.getInstance().parse(rawIssueUnionRecord, releaserReference);
			
			//FAIL
			fail("this should throw an exception");
		}
		catch (Exception e) 
		{
			//EXCEPTION IS THROWN OK
		}	
	}

	
	@Test
	public void processIssueUnionRecord()
	{
		
		init();				
		
		assertEquals(Transaction.VALIDATE_OK, issueUnionTransaction.isValid(db, releaserReference));

		issueUnionTransaction.sign(certifier, false);
		
		issueUnionTransaction.process(db, gb, false);
		
		LOGGER.info("union KEY: " + union.getKey(db));
		
		//CHECK BALANCE ISSUER
		assertEquals(BlockChain.MAJOR_ERA_BALANCE_BD, certifier.getBalanceUSE(ERM_KEY, db));
		assertEquals(BigDecimal.valueOf(1).subtract(issueUnionTransaction.getFee()).setScale(8), certifier.getBalanceUSE(FEE_KEY, db));
		
		//CHECK UNION EXISTS DB AS CONFIRMED:  key > -1
		long key = db.getIssueUnionMap().get(issueUnionTransaction);
		assertEquals(1, key);
		assertEquals(true, db.getItemUnionMap().contains(key));
		
		//CHECK UNION IS CORRECT
		assertEquals(true, Arrays.equals(db.getItemUnionMap().get(key).toBytes(true, false), union.toBytes(true, false)));
						
		//CHECK REFERENCE SENDER
		assertEquals(issueUnionTransaction.getTimestamp(), certifier.getLastReference(db));

		//////// ORPHAN /////////
		issueUnionTransaction.orphan(db, false);
		
		//CHECK BALANCE ISSUER
		assertEquals(BlockChain.MAJOR_ERA_BALANCE_BD, certifier.getBalanceUSE(ERM_KEY, db));
		assertEquals(BigDecimal.valueOf(1).setScale(8), certifier.getBalanceUSE(FEE_KEY, db));
		
		//CHECK UNION EXISTS ISSUER
		assertEquals(false, db.getItemUnionMap().contains(unionKey));
						
		//CHECK REFERENCE ISSUER
		assertEquals(issueUnionTransaction.getReference(), certifier.getLastReference(db));
	}
	
	
}
