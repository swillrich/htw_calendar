package de.svenwillrich.htw.spezprog.android.control.cal;

public interface IDataReceiverAttampter {
	public void onFailure(int counter);

	public void onSuccess();

	public final static int ATTAMPTS = 3;

	public void onFailureBadPassword();
}
