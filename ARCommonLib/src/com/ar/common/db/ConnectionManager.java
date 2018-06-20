package com.ar.common.db;

public interface ConnectionManager {
	public void open();
	public void open(boolean autoCommit);
	public void openBatch();
	public void openBatch(boolean autoCommit);
	public <T> T getSession();
	public <T> T getBatchSession();
	public void close();
	public void commit();
	public void rollback();
	public void getSecondarySession();
} // interface ConnectionManager
