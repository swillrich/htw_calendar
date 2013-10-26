package de.svenwillrich.htw.spezprog.app.logik;

public interface IDataReceiverAttampter {
	public void onFailure(int counter);

	public void onSuccess();

	public final static int ATTAMPTS = 3;
}
