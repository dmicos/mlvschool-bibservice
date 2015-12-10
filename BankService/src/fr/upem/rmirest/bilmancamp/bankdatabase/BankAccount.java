package fr.upem.rmirest.bilmancamp.bankdatabase;

import java.util.Objects;

public class BankAccount {

	private final String currency;
	private final String password;
	private double balance;

	public BankAccount(String currency, String password) {
		this.currency = Objects.requireNonNull(currency);
		this.password = Objects.requireNonNull(password);
		this.balance = 0;
	}

	public double getBalance() {
		return balance;
	}

	public String getCurrency() {
		return currency;
	}

	public String getPassword() {
		return password;
	}

	public void deposit(double amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Amount cannot be negative.");
		}
		balance += amount;
	}

	public void withdraw(double amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Amount cannot be negative.");
		}
		balance -= amount;
	}

}
