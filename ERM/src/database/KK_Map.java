package database;

import java.util.Stack;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.mapdb.BTreeKeySerializer;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.Fun.Tuple4;

//import core.block.Block;
//import core.item.statuses.StatusCls;
//import utils.ObserverMessage;
import database.DBSet;

// key to key_Stack for End_Date Map
// in days
public class KK_Map extends DBMap<
			Long, // item1 Key
			TreeMap<Long, // item2 Key
				Stack<Tuple4<
					Long, // beg_date
					Long, // end_date
					/*
public Block getBlockByHeight(int parseInt) {
		byte[] b = DBSet.getInstance().getHeightMap().getBlockByHeight(parseInt);
		return DBSet.getInstance().getBlockMap().get(b);
	}
					 */
					Integer, // block.getHeight() -> db.getBlockMap(db.getHeightMap().getBlockByHeight(index))
					Integer // block.getTransaction(transaction.getSignature()) -> block.getTransaction(index)
				>>>>
{
	
	private Map<Integer, Integer> observableData = new HashMap<Integer, Integer>();
	private String name;
		
	public KK_Map(DBSet databaseSet, DB database,
			String name, int observerMessage_add, int observerMessage_remove)
	{
		super(databaseSet, database);
		
		this.name = name;
		this.observableData.put(DBMap.NOTIFY_ADD, observerMessage_add);
		this.observableData.put(DBMap.NOTIFY_REMOVE, observerMessage_remove);
		//this.observableData.put(DBMap.NOTIFY_LIST, ObserverMessage.LIST_PERSON_STATUSTYPE);

	}

	public KK_Map(KK_Map parent) 
	{
		super(parent);
	}

	
	protected void createIndexes(DB database){}

	@Override
	protected Map<Long, TreeMap<Long, Stack<Tuple4<Long, Long, Integer, Integer>>>> getMap(DB database) 
	{
		//OPEN MAP
		BTreeMap<Long, TreeMap<Long, Stack<Tuple4<Long, Long, Integer, Integer>>>> map =  database.createTreeMap(name)
				.keySerializer(BTreeKeySerializer.BASIC)
				.counterEnable()
				.makeOrGet();
				
		//RETURN
		return map;
	}

	@Override
	protected Map<Long, TreeMap<Long, Stack<Tuple4<Long, Long, Integer, Integer>>>> getMemoryMap() 
	{
		// HashMap ?
		return new TreeMap<Long, TreeMap<Long, Stack<Tuple4<Long, Long, Integer, Integer>>>>();
	}

	@Override
	protected TreeMap<Long, Stack<Tuple4<Long, Long, Integer, Integer>>> getDefaultValue() 
	{
		return new TreeMap<Long, Stack<Tuple4<Long, Long, Integer, Integer>>>();
	}
	
	@Override
	protected Map<Integer, Integer> getObservableData() 
	{
		return this.observableData;
	}

	public void addItem(Long key, Long itemKey, Tuple4<Long, Long, Integer, Integer> item)
	{

		TreeMap<Long, Stack<Tuple4<Long, Long, Integer, Integer>>> value = this.get(key);
		Stack<Tuple4<Long, Long, Integer, Integer>> stack = value.get(itemKey);
		if (stack == null) {
			stack = new Stack<Tuple4<Long, Long, Integer, Integer>>();
			stack.add(item);
		}
		else {
			if (item.a == null || item.b == null) {
				// item has NULL values id dates - reset it by last values
				Long valA;
				Long valB;
				Tuple4<Long, Long, Integer, Integer> lastItem = stack.peek();
				if (item.a == null) {
					// if input item Begin Date = null - take date from stack (last value)
					valA = lastItem.a;
				} else {
					valA = item.a;					
				}
				if (item.b == null) {
					// if input item End Date = null - take date from stack (last value)
					valB = lastItem.b;
				} else {
					valB = item.b;					
				}
				stack.add(new Tuple4<Long, Long, Integer, Integer>(valA, valB, item.c, item.d));
			} else {
				stack.add(item);
			}
		}
		
		value.put(itemKey, stack);
		
		this.set(key, value);
	}
	
	public Tuple4<Long, Long, Integer, Integer> getItem(Long key, Long itemKey)
	{
		TreeMap<Long, Stack<Tuple4<Long, Long, Integer, Integer>>> value = this.get(key);
		Stack<Tuple4<Long, Long, Integer, Integer>> stack = value.get(itemKey);
		return stack != null? stack.size()> 0? stack.peek(): null : null;
	}
	
	// remove only last item from stack for this key of itemKey
	public void removeItem(Long key, Long itemKey)
	{
		TreeMap<Long, Stack<Tuple4<Long, Long, Integer, Integer>>> value = this.get(key);
		Stack<Tuple4<Long, Long, Integer, Integer>> stack = value.get(itemKey);
		if (stack==null) return;

		if (stack != null && stack.size() > 0 )
			stack.pop();
		value.put(itemKey, stack);
		this.set(key, value);
	}
}