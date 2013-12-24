package com.unistrong.uniteqlauncher.util;

/**
 * Constants
 * @author Baron.hu
 * */
public final class Constants {
	
	/* Database version number */
	public static final int VERSION = 1;
	
	/* Database name */
	public static final String DATABASE_NAME = "uniteqlauncher.db";
	
	/* Create table */
	public static final String CREATE_TABLE_SQL = "CREATE TABLE [launcher]("
			+ "[id] INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "[pkgname] VACHAR(50) NOT NULL,"
			+ "[clsname] VACHAR(50) NOT NULL,"
			+ "[appname] VACHAR(50) NOT NULL,"
			+ "[photo] BINARY)";
	
	/* Drop table */
	public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS[launcher]";
	
	/* SQL for inserting items */
	public static final String INSERT_SQL = "INSERT INTO launcher(pkgname,clsname,appname,photo) values(?,?,?,?)";
	
	/* SQL for deleting items */
	public static final String DEL_SQL = "DELETE FROM LAUNCHER";
	
	/* SQL for selecting items */
	public static final String SELECT_SQL = "SELECT pkgname,clsname,appname,photo FROM launcher";
	
	/* SD card root path */
	public static final String MEDIA_PATH = "/mnt/sdcard/";

}
