package fr.upem.rmirest.bilmancamp.bankdatabase;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class BankDatabaseImpl implements BankDatabase {

	private static long NUM_ACCOUNT = 0;
	private final Map<Long, BankAccount> accounts = new ConcurrentHashMap<>();

	@Override
	public long createAccount(String currency, String password) {
		// Use a synchronized block here to avoid multiple account creation at
		// the same time.
		// Only needed here because of the index increment. For the other
		// methods, the implementation of the map is a ConcurrentHashMap which
		// is thread safe.
		synchronized (accounts) {
			NUM_ACCOUNT++;
			accounts.put(NUM_ACCOUNT,
					new BankAccount(Objects.requireNonNull(currency), Objects.requireNonNull(password)));
			return NUM_ACCOUNT;
		}
	}

	@Override
	public boolean accessValid(long id, String password) {
		BankAccount bankAccount = accounts.get(id);
		if (bankAccount == null) {
			return false;
		}
		return bankAccount.getPassword().equals(password);
	}

	@Override
	public boolean deposit(long id, double amount) {
		BankAccount bankAccount = accounts.get(id);
		if (bankAccount != null) {
			bankAccount.deposit(amount);
			return true;
		}
		return false;
	}

	@Override
	public boolean withdraw(long id, double amount) {
		BankAccount bankAccount = accounts.get(id);
		if (bankAccount != null) {
			bankAccount.withdraw(amount);
			return true;
		}
		return false;
	}

	@Override
	public double balance(long id) {
		BankAccount account = accounts.get(new Long(id));
		if (account == null) {
			throw new IllegalArgumentException("Account " + id + " doesn't exists.");
		}
		return account.getBalance();
	}

	@Override
	public String getAccountCurrency(long id) {
		BankAccount account = accounts.get(new Long(id));
		if (account == null) {
			throw new IllegalArgumentException("Account " + id + " doesn't exists.");
		}
		return account.getCurrency();
	}

}
