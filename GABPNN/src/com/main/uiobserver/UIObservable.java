package com.main.uiobserver;

public interface UIObservable {

	public abstract void notifyObserver();
	
	public abstract void addObserver(UIObserver observer);
}
