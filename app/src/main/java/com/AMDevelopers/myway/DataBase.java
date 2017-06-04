package com.AMDevelopers.myway;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper{

	public DataBase(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS User ( "
				+ "ID INTEGER NOT NULL, "
				+ "UserName VARCHAR(30) NOT NULL, "
				+ "Password VARCHAR(1000) NOT NULL, "
				+ "Name VARCHAR(60) NOT NULL, "
				+ "Session VARCHAR(10) NOT NULL, "
				+ "PRIMARY KEY ( ID )); ");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

	public void LogIn (String UserName){
		SQLiteDatabase db = getReadableDatabase();
		db.execSQL("UPDATE User SET Session = 'Valid' WHERE UserName = '"+UserName+"';");
	}

	public void LogOut (String UserName){
		SQLiteDatabase db = getReadableDatabase();
		db.execSQL("UPDATE User SET Session = 'Invalid' WHERE UserName = '"+UserName+"';");
	}

	public String CheckSession(){
		SQLiteDatabase db = getReadableDatabase();
		Cursor Users = db.rawQuery("SELECT * FROM User;", null);
		while(Users.moveToNext()){
			if (Users.getString(4).equalsIgnoreCase("Valid")){
				return Users.getString(0)+","+Users.getString(1)+","+Users.getString(3);
			}
		}
		return "No Logged In User";
	}
	
	public void SignUp(int ID, String UserName, String Password, String Name){
		SQLiteDatabase db = getReadableDatabase();
		db.execSQL("INSERT INTO User (ID, UserName, Password, Name, Session) VALUES (" + ID + ",'" + UserName + "','" + Password + "','" + Name + "','Valid');");
	}
}
