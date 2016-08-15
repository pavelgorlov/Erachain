package core.account;
//04/01 +- 
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.mapdb.Fun.Tuple2;
import org.mapdb.Fun.Tuple3;
import org.mapdb.Fun.Tuple4;
import org.mapdb.Fun.Tuple5;

import api.ApiErrorFactory;

//import com.google.common.primitives.Bytes;

import at.AT_Transaction;
import database.Item_Map;
import database.DBSet;
import database.NameMap;
import controller.Controller;
//import core.account.PublicKeyAccount;
import core.BlockGenerator;
import core.block.Block;
import core.block.GenesisBlock;
import core.crypto.Base58;
import core.item.ItemCls;
import core.item.persons.PersonCls;
import core.item.statuses.StatusCls;
import core.naming.Name;
//import core.item.assets.AssetCls;
import core.transaction.Transaction;
import core.transaction.TransactionAmount;
import ntp.NTP;
import settings.Settings;
import utils.NameUtils;
import utils.NumberAsString;
import utils.Pair;
import utils.NameUtils.NameResult;

public class Account {
	
	public static final int ADDRESS_LENGTH = 25;
	private static final long ERM_KEY = Transaction.RIGHTS_KEY;
	private static final long FEE_KEY = Transaction.FEE_KEY;
	public static final long ALIVE_KEY = StatusCls.ALIVE_KEY;
	public static String EMPTY_PUBLICK_ADDRESS = new PublicKeyAccount(new byte[PublicKeyAccount.PUBLIC_KEY_LENGTH]).getAddress();


	protected String address;
	
	private byte[] lastBlockSignature;
	private long generatingBalance; //used  for forging balance
	
	protected Account()
	{
		this.generatingBalance = 0l;
	}
	
	public Account(String address)
	{

		// test address
		assert(Base58.decode(address) instanceof byte[] );

		this.address = address;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	//BALANCE
	
	// GET
	public BigDecimal getUnconfirmedBalance(long key)
	{
		return Controller.getInstance().getUnconfirmedBalance(this, key);
	}
	/*
	public BigDecimal getConfirmedBalance()
	{
		return this.getConfirmedBalance(DBSet.getInstance());
	}
	public BigDecimal getConfirmedBalance(DBSet db)
	{
		return db.getAssetBalanceMap().get(getAddress(), Transaction.FEE_KEY);
	}
	*/
	public BigDecimal getConfirmedBalance(long key)
	{
		return this.getConfirmedBalance(key, DBSet.getInstance());
	}
	
	public BigDecimal getConfirmedBalance(long key, DBSet db)
	{
		return db.getAssetBalanceMap().get(getAddress(), key);
	}
	/*
	public Integer setConfirmedPersonStatus(long personKey, long statusKey, int end_date, DBSet db)
	{
		return db.getPersonStatusMap().addItem(personKey, statusKey, end_date);
	}
	*/

	// SET
	/*
	public void setConfirmedBalance(BigDecimal amount)
	{
		this.setConfirmedBalance(amount, DBSet.getInstance());
	}
	public void setConfirmedBalance(BigDecimal amount, DBSet db)
	{
		//UPDATE BALANCE IN DB
		db.getAssetBalanceMap().set(getAddress(), Transaction.FEE_KEY, amount);
	}
	*/
	//
	public void setConfirmedBalance(long key, BigDecimal amount)
	{
		this.setConfirmedBalance(key, amount, DBSet.getInstance());
	}

	public void setConfirmedBalance(long key, BigDecimal amount, DBSet db)
	{
		//UPDATE BALANCE IN DB
		db.getAssetBalanceMap().set(getAddress(), key, amount);
	}

	// STATUS
	/*
	public void setConfirmedPersonStatus(long personKey, long statusKey, Integer days)
	{
		this.setConfirmedPersonStatus(personKey, statusKey, days, DBSet.getInstance());
	}
		
	public void setConfirmedPersonStatus(long personKey, long statusKey, Integer days, DBSet db)
	{
		//UPDATE PRIMARY TIME IN DB
		db.getPersonStatusMap().set(personKey, statusKey, days);
	}
	*/

	
	public BigDecimal getBalance(int confirmations, long key)
	{
		return this.getBalance(confirmations, key, DBSet.getInstance());
	}
	/*
	public BigDecimal getBalance(int confirmations)
	{
		return this.getBalance(confirmations, FEE_KEY, DBSet.getInstance());
	}
	public BigDecimal getBalance(int confirmations, DBSet db)
	{
		return this.getBalance(confirmations, FEE_KEY, DBSet.getInstance());
	}
	*/
	public BigDecimal getBalance(int confirmations, long key, DBSet db)
	{
		//CHECK IF UNCONFIRMED BALANCE
		if(confirmations <= 0)
		{
			return this.getUnconfirmedBalance(key);
		}
		
		//IF 1 CONFIRMATION
		if(confirmations == 1)
		{
			return this.getConfirmedBalance(key, db);
		}
		
		//GO TO PARENT BLOCK 10
		BigDecimal balance = this.getConfirmedBalance(key, db);
		Block block = db.getBlockMap().getLastBlock();
		
		for(int i=1; i<confirmations && block != null && block instanceof Block; i++)
		{
			for(Transaction transaction: block.getTransactions())
			{
				if(transaction.isInvolved(this))
				{
					balance = balance.subtract(transaction.getAmount(this));
				}
			}
				
			block = block.getParent(db);
		}
		
		//RETURN
		return balance;
	}
	
	private void updateGeneratingBalance(DBSet db)
	{
		//CHECK IF WE NEED TO RECALCULATE
		if(this.lastBlockSignature == null)
		{
			this.lastBlockSignature = db.getBlockMap().getLastBlockSignature();
			calculateGeneratingBalance(db);
		}
		else
		{
			//CHECK IF WE NEED TO RECALCULATE
			if(!Arrays.equals(this.lastBlockSignature, db.getBlockMap().getLastBlockSignature()))
			{
				this.lastBlockSignature = db.getBlockMap().getLastBlockSignature();
				calculateGeneratingBalance(db);
			}
		}
	}

	// take current balance
	public void calculateGeneratingBalance(DBSet db)
	{
		long balance = this.getConfirmedBalance(ERM_KEY, db).setScale(0).longValue();
		this.generatingBalance = balance;
	}

	/*
	// balance FOR generation
	public void calculateGeneratingBalance_old(DBSet db)
	{
		//CONFIRMED BALANCE + ALL NEGATIVE AMOUNTS IN LAST 9 BLOCKS - for ERM_KEY only
		BigDecimal balance = this.getConfirmedBalance(ERM_KEY, db);
		
		Block block = db.getBlockMap().getLastBlock();
		
		int penalty_koeff = 1000000;
		int balance_penalty = penalty_koeff;
		
		// icreator X 10
		// not resolve first 100 blocks
		for(int i=1; i<GenesisBlock.GENERATING_RETARGET * 10 && block != null && block.getHeight(db) > 100; i++)
		{
			for(Transaction transaction: block.getTransactions())
			{
				if(transaction.isInvolved(this) & transaction instanceof TransactionAmount)
				{
					TransactionAmount ta = (TransactionAmount)transaction;
					
					if(ta.getKey() == ERM_KEY & transaction.getAmount(this).compareTo(BigDecimal.ZERO) == 1)
					{
						balance = balance.subtract(transaction.getAmount(this));
					}
				}
			}
			LinkedHashMap<Tuple2<Integer,Integer>,AT_Transaction> atTxs = db.getATTransactionMap().getATTransactions(block.getHeight(db));
			Iterator<AT_Transaction> iter = atTxs.values().iterator(); 
			while ( iter.hasNext() )
			{
				AT_Transaction key = iter.next();
				if ( key.getRecipient().equals( this.getAddress() ) )
				{
					balance = balance.subtract( BigDecimal.valueOf(key.getAmount(), 8) );
				}
			}
			
			// icreator X 0.9 for each block generated
			if (balance_penalty > 0.1 * penalty_koeff && block.getCreator().getAddress().equals(this.address)) {
				balance_penalty *= Settings.GENERATE_CONTINUOUS_PENALTY * 0.001;
			} else {
				// reset
				balance_penalty = penalty_koeff;
			}
			block = block.getParent(db);
		}
		
		//DO NOT GO BELOW 0
		if(balance.compareTo(BigDecimal.ZERO) == -1)
		{
			balance = BigDecimal.ZERO.setScale(8);
		}

		// use penalty
		this.generatingBalance = balance.multiply(new BigDecimal(balance_penalty / penalty_koeff));
		
	}
	*/
	
	public long getGeneratingBalance()
	{
		return this.getGeneratingBalance(DBSet.getInstance());
	}
	
	public long getGeneratingBalance(DBSet db)
	{	
		//UPDATE
		updateGeneratingBalance(db);
		
		//RETURN
		return this.generatingBalance;
	}
	
	//REFERENCE
	
	public Long getLastReference()
	{
		return this.getLastReference(DBSet.getInstance());
	}
	
	public Long getLastReference(DBSet db)
	{
		return db.getReferenceMap().get(this.getAddress());
	}
	
	public void setLastReference(Long timestamp)
	{
		this.setLastReference(timestamp, DBSet.getInstance());
	}
	
	public void setLastReference(Long timestamp, DBSet db)
	{
		db.getReferenceMap().set(this.getAddress(), timestamp);
	}
	
	public void removeReference() 
	{
		this.removeReference(DBSet.getInstance());
	}
	
	public void removeReference(DBSet db) 
	{
		db.getReferenceMap().delete(this.getAddress());
	}
	
	//TOSTRING
	
	@Override
	public String toString()
	{
		Tuple2<Integer, PersonCls> personRes = this.hasPerson();
		String personStr;
		String addressStr;
		if (personRes == null) {
			personStr = "";
			addressStr = this.getAddress();
		}
		else {
			personStr = personRes.b.getShort();
			addressStr = this.getAddress().substring(0, 8);
			if (personRes.a == -2) personStr = "[-]" + personStr;
			else if (personRes.a == -1) personStr = "[?]" + personStr;
			else if (personRes.a == 0) personStr = "[++]" + personStr;
			else if (personRes.a == 1) personStr = "[+]" + personStr;
		}
		return " {" + NumberAsString.getInstance().numberAsString(this.getConfirmedBalance(FEE_KEY)) + "}"
				+ " " + addressStr + " " + personStr;
	}
	
	public String toString(long key)
	{
		Tuple2<Integer, PersonCls> personRes = this.hasPerson();
		String personStr;
		String addressStr;
		if (personRes == null) {
			personStr = "";
			addressStr = this.getAddress();
		}
		else {
			personStr = personRes.b.getShort();
			addressStr = this.getAddress().substring(0, 8);
			if (personRes.a == -2) personStr = "[-]" + personStr;
			else if (personRes.a == -1) personStr = "[?]" + personStr;
			else if (personRes.a == 0) personStr = "[++]" + personStr;
			else if (personRes.a == 1) personStr = "[+]" + personStr;
		}
		return NumberAsString.getInstance().numberAsString(this.getConfirmedBalance(key))
				+ " {" + NumberAsString.getInstance().numberAsString(this.getConfirmedBalance(FEE_KEY)) + "}"
				+ " " + addressStr + " " + personStr;
	}
	
	//////////
	public String viewPerson() {
		Tuple2<Integer, PersonCls> personRes = this.hasPerson();
		if (personRes == null) {
			return "";
		} else {
			String personStr = personRes.b.toString();
			if (personRes.a == -2) personStr = "[-]" + personStr;
			else if (personRes.a == -1) personStr = "[?]" + personStr;
			//else if (personRes.a == 0) personStr = "[+]" + personStr; // default is permanent ACTIVE
			else if (personRes.a == 1) personStr = "[+]" + personStr;
			return personStr;
		}
		
	}
	
	public String asPerson()
	{
		Tuple2<Integer, PersonCls> personRes = this.hasPerson();
		if (personRes == null) {
			return this.getAddress();
		}
		else {
			String personStr = personRes.b.getShort();
			String addressStr = this.getAddress().substring(1, 6);
			if (personRes.a == -2) personStr = "[-]" + personStr;
			else if (personRes.a == -1) personStr = "[?]" + personStr;
			//else if (personRes.a == 0) personStr = "[+]" + personStr; // default is permanent ACTIVE
			else if (personRes.a == 1) personStr = "[+]" + personStr;
			return addressStr + ": " + personStr;
		}
	}

	public String asPerson_01(boolean shrt)
	{
		Tuple2<Integer, PersonCls> personRes = this.hasPerson();
		if (personRes == null) {
			return "";
		}
		else {
			return shrt? personRes.b.getShort(): personRes.b.getName();
		}
	}

	@Override
	public int hashCode()
	{
		return this.getAddress().hashCode();
	}
	
	//EQUALS
	@Override
	public boolean equals(Object b)
	{
		if(b instanceof Account)
		{
			return this.getAddress().equals(((Account) b).getAddress());
		}
		
		return false;	
	}

	// personKey, days, block, reference
	public static Tuple4<Long, Integer, Integer, Integer> getPersonDuration(DBSet db, String address) {
		return db.getAddressPersonMap().getItem(address);				
	}
	public Tuple4<Long, Integer, Integer, Integer> getPersonDuration(DBSet db) {
		return getPersonDuration(db, this.address);
	}
	
	public boolean isPerson(DBSet db) {
		
		// IF DURATION ADDRESS to PERSON IS ENDED
		Tuple4<Long, Integer, Integer, Integer> addressDuration = this.getPersonDuration(db);
		if (addressDuration == null) return false;
		// TEST TIME and EXPIRE TIME
		long current_time = NTP.getTime();
		
		// TEST TIME and EXPIRE TIME for PERSONALIZE address
		int days = addressDuration.b;
		if (days < 0 ) return false;
		if (days * (long)86400000 < current_time ) return false;

		// IF PERSON ALIVE
		Long personKey = addressDuration.a;
		Tuple5<Long, Long, byte[], Integer, Integer> personDuration = db.getPersonStatusMap().getItem(personKey, ALIVE_KEY);
		// TEST TIME and EXPIRE TIME for ALIVE person
		Long end_date = personDuration.b;
		if (end_date == null ) return true; // permanent active
		if (end_date < current_time + 86400000 ) return false; // - 1 day
		
		return true;
		
	}
	public Tuple2<Integer, PersonCls> hasPerson(DBSet db) {
		
		// IF DURATION ADDRESS to PERSON IS ENDED
		Tuple4<Long, Integer, Integer, Integer> addressDuration = this.getPersonDuration(db);
		if (addressDuration == null) return null;
		// TEST TIME and EXPIRE TIME
		long current_time = NTP.getTime();
		
		// get person
		Long personKey = addressDuration.a;
		PersonCls person = (PersonCls)Controller.getInstance().getItem(db, ItemCls.PERSON_TYPE, personKey);
		
		// TEST ADDRESS is ACTIVE?
		int days = addressDuration.b;
		// TODO x 1000 ?
		if (days < 0 || days * (long)86400000 < current_time )
			return new Tuple2<Integer, PersonCls>(-1, person);

		// IF PERSON is ALIVE
		Tuple5<Long, Long, byte[], Integer, Integer> personDuration = db.getPersonStatusMap().getItem(personKey, ALIVE_KEY);
		// TEST TIME and EXPIRE TIME for ALIVE person
		if (personDuration == null)
			return new Tuple2<Integer, PersonCls>(-2, person);
		Long end_date = personDuration.b;
		if (end_date == null )
			// permanent active
			return new Tuple2<Integer, PersonCls>(0, person);
		/*
		else if (personDuration.c[0] == (byte)2 )
			// is DEAD
			return new Tuple2<Integer, PersonCls>(-2, person);
			*/
		else if (end_date < current_time + 86400000 )
			// ALIVE expired
			return new Tuple2<Integer, PersonCls>(-1, person);
		
		return new Tuple2<Integer, PersonCls>(1, person);
		
	}
	public Tuple2<Integer, PersonCls> hasPerson() {
		return hasPerson(DBSet.getInstance());
	}
	
	// previous forging block
	public Integer getForgingData(DBSet db, int height) {
		return db.getAddressForging().get(this.address, height);
	}
	public void setForgingData(DBSet db, int height, int prevHeight) {
		db.getAddressForging().set(this.address, height, prevHeight);
	}
	public void delForgingData(DBSet db, int height) {
		db.getAddressForging().delete(this.address, height);
	}
	public Integer getLastForgingData(DBSet db) {
		return db.getAddressForging().getLast(this.address);
	}
	public void setLastForgingData(DBSet db, int prevHeight) {
		db.getAddressForging().setLast(this.address, prevHeight);
	}
	
	public long getWinValueHeight2(int heightThis, int heightStart)
	{
		int len = heightThis - heightStart;
		if (len < 1)
			len = 1;
			
		if (len < 20)
			len += 20;
		else
			len *= 2;
		
		int MAX_LEN = 333;
		int MAX_LEN_2 = MAX_LEN * 100;
		if (len < MAX_LEN ) {
			//return (long)(len * Math.pow(len, 0.3));
			return (long)Math.pow(len, 1.5);
		} else if ( len < MAX_LEN_2 )
			return (long)Math.pow(MAX_LEN, 1.5) + (len - MAX_LEN);
		//return (long)(len * Math.pow(MAX_LEN, 0.3));
		return (long)Math.pow(MAX_LEN, 1.5) + (MAX_LEN_2 - MAX_LEN);
	}

	public int calcWinValueHeight(DBSet dbSet, int height, int previousForgingHeight)
	{

		/*
		findTransactionsKeys(String address, String sender, String recipient,
				final int minHeight, final int maxHeight,
				int type, final int service,
				boolean desc, int offset, int limit)
				*/
		
		long incomed_amount = 0l;
		long win_value = 0l;
		long amount;
		//List<Transaction> txs = dbSet.getTransactionFinalMap()
		//		.getTransactionsByTypeAndAddress(this.address, (int)ERM_KEY, 0);
		
		List<Transaction> txs = dbSet.getTransactionFinalMap().findTransactions(null, null, address, previousForgingHeight,
				0, 0, 0, false, 0, 0);

		
		for(Transaction transaction: txs)
		{
			
			if ( transaction.getAssetKey() == ERM_KEY
					// || transaction.getRecipientAccounts().contains(this.address)
					) {
				amount = transaction.getAmount().longValue();
				incomed_amount += amount;
				
				win_value += getWinValueHeight2(height, transaction.getBlockHeight(dbSet)) * amount;
			}

		}
		
		// TODO начальные блоки тоже списывает переводы
		// если сделать перевод со счета то не правильно пересчитывает
		// blockNo, forgingAmount, ...
		//Integer previousForgingBlockHeightThis = this.getForgingData(dbSet, height);
		if (previousForgingHeight > 1) {
			// IF exist previous forged BLOCK
			//
			
			// TODO - на любой HEIGT не пашет потому что берет последний баланс а не на тут дату
			// поидее надо запоминать этот баланс в блоке и в базе данных чтобы потом не считать
			win_value += (this.getConfirmedBalance(ERM_KEY, dbSet).longValue() - incomed_amount)
					* getWinValueHeight2(height, previousForgingHeight);
		}

		if (height < 10000)
			win_value >>= 16;
		else if (height < 100000)
			win_value >>= 18;
		else if (height < 1000000)
			win_value >>= 20;
		else
			win_value >>= 22;
		
		return (int)win_value;

	}

	public long calcWinValue(DBSet dbSet, int height)
	{
		
		/*
		int previousForgingHeight;
		int lastHeight = this.getLastForgingData(dbSet);
		if (lastHeight > height) {
			previousForgingHeight = this.getForgingData(dbSet, height);			
		} else {
			previousForgingHeight = this.getForgingData(dbSet, lastHeight);
		}
		*/

		// LAST HEIGHT for this Height
		int previousForgingHeight = this.getForgingData(dbSet, height);
		// LAST 
		int lastHeight = this.getLastForgingData(dbSet);
		// IF BLOCK not inserted in MAP
		//int lastHeight = creator.getLastForgingData(dbSet);
		if (lastHeight <= height) {
			previousForgingHeight = lastHeight;
		} else if (previousForgingHeight < lastHeight) {
			// select
			previousForgingHeight = lastHeight;
		}

		//if (lastHeight == previousForgingHeight)
		//	previousForgingHeight -= 1;
		return calcWinValueHeight(dbSet, height, previousForgingHeight);
	}
}
