package com.example.cherishnewszyw109.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsDBAdapter {

	final static int VERSON = 1;
	private final static String DBNAME = "collect.db";
	private final static String TABLE = "collect";
	private final static String ID="id";
	private final static String TITLE="title";
	private final static String TIME="time";
	private final static String CONTENT="content";
	private final static String PIC="pic";

	private Context context;
	private OpenHelper openHelper;
	private SQLiteDatabase database;
	private static NewsDBAdapter dbAdapter;

	public static NewsDBAdapter getInstance(Context context){
		if(dbAdapter==null){
			dbAdapter=new NewsDBAdapter(context);
		}
		return dbAdapter;
	}

	private NewsDBAdapter(Context context) {
		this.context = context;
		openHelper = new OpenHelper(context);

	}

	public void openDB() {
		if(database==null){
			database = openHelper.getWritableDatabase();
		}
		
	}

	public void closeDB() {
		if (database!=null){
			database.close();
			database = null;
		}

	}

	public long insert(Map  news) {
		long id = -1 ;
		ContentValues contentValues = new ContentValues();
		contentValues.put(TITLE,news.get("title").toString());
		contentValues.put(TIME,news.get("time").toString());
		contentValues.put(CONTENT,news.get("content").toString());
		contentValues.put(PIC,news.get("picurl").toString());
		id = database.insert(TABLE,null,contentValues);		//失败返回行号，成功返回-1
		return id;
	}

	public int queryBytitle(String s){
		Cursor cnt ;
		if ("".equals(s))
			return -1;
		String selection = "title="+"'"+s+"'";
		cnt = database.query(TABLE,null,selection,null,null,null,null);
//		cnt.moveToFirst();
		if (cnt.getCount() == 0)
			return 1;
		else{
		//	System.out.println("adapter的值为");
			return -1;
		}
	}



	public long deleteById(String id){
		int cnt = 0;
		String[] whereArags ={id};
		//这里database出现空指针
		if (database!=null)
			cnt = database.delete(TABLE,ID+"=?",whereArags);
		return cnt;
	}

	
	public  List<Map> queryAll(){
		Cursor cursor ;
		cursor=null;
		//cursor=
		System.out.println("zhangyuwei");
		cursor = database.query(TABLE,null,null,null,null,null,null);
		System.out.println("--cur"+cursor);
		if (cursor.getCount() != 0)
			return convertToContactBean(cursor);
		else {
			return null;
		}
	}

	public List<Map> convertToContactBean(Cursor cursor){
		List<Map> contactBeans=new ArrayList<Map>();
		cursor.moveToFirst();
		for(int i=0;i<cursor.getCount();i++){
			Map contactBean = new HashMap();
			contactBean.put("id",cursor.getInt(cursor.getColumnIndex(ID)));
	     	contactBean.put("title",cursor.getString(cursor.getColumnIndex(TITLE)));
			contactBean.put("time",cursor.getString(cursor.getColumnIndex(TIME)));
			contactBean.put("content",cursor.getString(cursor.getColumnIndex(CONTENT)));
			contactBean.put("pic",cursor.getString(cursor.getColumnIndex(PIC)));
			contactBeans.add(contactBean);
			cursor.moveToNext();
		}
		return contactBeans;
	}
	
	public class OpenHelper extends SQLiteOpenHelper {

		public OpenHelper(Context context) {
			super(context, DBNAME, null, VERSON);
			System.out.println(context+"--");
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			String sql = "create  table  if not exists collect( id integer primary key autoincrement, title  varchar(256), time varchar(64), content varchar(256)" +
					", pic varchar(256))";
			System.out.println("--"+sql);
			db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

	}
}
