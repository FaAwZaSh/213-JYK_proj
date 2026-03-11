/*
 * Decompiled with CFR 0.152.
 */
package simulation;

import banking.Balances;
import banking.Card;
import banking.Message;
import banking.Money;
import banking.Status;

public class SimulatedBank {
    private static final int[] PIN;
    private static final int[][] ACCOUNT_NUMBER;
    private static Money[] WITHDRAWALS_TODAY;
    private static final Money DAILY_WITHDRAWAL_LIMIT;
    private Money[] BALANCE = new Money[]{new Money(0), new Money(100), new Money(1000), new Money(5000)};
    private Money[] AVAILABLE_BALANCE = new Money[]{new Money(0), new Money(100), new Money(1000), new Money(5000)};

    static {
        int[] nArray = new int[3];
        nArray[1] = 42;
        nArray[2] = 1234;
        PIN = nArray;
        int[][] nArrayArray = new int[3][];
        nArrayArray[0] = new int[3];
        int[] nArray2 = new int[3];
        nArray2[0] = 1;
        nArray2[1] = 2;
        nArrayArray[1] = nArray2;
        int[] nArray3 = new int[3];
        nArray3[0] = 1;
        nArray3[2] = 3;
        nArrayArray[2] = nArray3;
        ACCOUNT_NUMBER = nArrayArray;
        WITHDRAWALS_TODAY = new Money[]{new Money(0), new Money(0), new Money(0)};
        DAILY_WITHDRAWAL_LIMIT = new Money(300);
    }

    public Status handleMessage(Message message, Balances balances) {
        int cardNumber = message.getCard().getNumber();
        if (cardNumber < 1 || cardNumber > PIN.length) {
            return new Failure("Invalid card");
        }
        if (message.getPIN() != PIN[cardNumber]) {
            return new InvalidPIN();
        }
        switch (message.getMessageCode()) {
            case 0: {
                return this.withdrawal(message, balances);
            }
            case 1: {
                return this.initiateDeposit(message);
            }
            case 2: {
                return this.completeDeposit(message, balances);
            }
            case 3: {
                return this.transfer(message, balances);
            }
            case 4: {
                return this.inquiry(message, balances);
            }
        }
        return null;
    }

    private Status withdrawal(Message message, Balances balances) {
        int cardNumber = message.getCard().getNumber();
        int accountNumber = ACCOUNT_NUMBER[cardNumber][message.getFromAccount()];
        if (accountNumber == 0) {
            return new Failure("Invalid account type");
        }
        Money amount = message.getAmount();
        Money limitRemaining = new Money(DAILY_WITHDRAWAL_LIMIT);
        limitRemaining.subtract(WITHDRAWALS_TODAY[cardNumber]);
        if (!amount.lessEqual(limitRemaining)) {
            return new Failure("Daily withdrawal limit exceeded");
        }
        if (!amount.lessEqual(this.AVAILABLE_BALANCE[accountNumber])) {
            return new Failure("Insufficient available balance");
        }
        WITHDRAWALS_TODAY[cardNumber].add(amount);
        this.BALANCE[accountNumber].subtract(amount);
        this.AVAILABLE_BALANCE[accountNumber].subtract(amount);
        balances.setBalances(this.BALANCE[accountNumber], this.AVAILABLE_BALANCE[accountNumber]);
        return new Success();
    }

    private Status initiateDeposit(Message message) {
        int cardNumber = message.getCard().getNumber();
        int accountNumber = ACCOUNT_NUMBER[cardNumber][message.getToAccount()];
        if (accountNumber == 0) {
            return new Failure("Invalid account type");
        }
        return new Success();
    }

    private Status completeDeposit(Message message, Balances balances) {
        int cardNumber = message.getCard().getNumber();
        int accountNumber = ACCOUNT_NUMBER[cardNumber][message.getToAccount()];
        if (accountNumber == 0) {
            return new Failure("Invalid account type");
        }
        Money amount = message.getAmount();
        this.BALANCE[accountNumber].add(amount);
        this.BALANCE[accountNumber].subtract(new Money(10, 0));
        balances.setBalances(this.BALANCE[accountNumber], this.AVAILABLE_BALANCE[accountNumber]);
        return new Success();
    }

    private Status transfer(Message message, Balances balances) {
        int cardNumber = message.getCard().getNumber();
        int fromAccountNumber = ACCOUNT_NUMBER[cardNumber][message.getFromAccount()];
        if (fromAccountNumber == 0) {
            return new Failure("Invalid from account type");
        }
        int toAccountNumber = ACCOUNT_NUMBER[cardNumber][message.getToAccount()];
        if (toAccountNumber == 0) {
            return new Failure("Invalid to account type");
        }
        if (fromAccountNumber == toAccountNumber) {
            return new Failure("Can't transfer money from\nan account to itself");
        }
        Money amount = message.getAmount();
        if (!amount.lessEqual(this.AVAILABLE_BALANCE[fromAccountNumber])) {
            return new Failure("Insufficient available balance");
        }
        this.BALANCE[fromAccountNumber].subtract(amount);
        this.AVAILABLE_BALANCE[fromAccountNumber].subtract(amount);
        this.BALANCE[toAccountNumber].add(amount);
        this.AVAILABLE_BALANCE[toAccountNumber].add(amount);
        balances.setBalances(this.BALANCE[toAccountNumber], this.AVAILABLE_BALANCE[toAccountNumber]);
        return new Success();
    }

    private Status inquiry(Message message, Balances balances) {
        int cardNumber = message.getCard().getNumber();
        int accountNumber = ACCOUNT_NUMBER[cardNumber][message.getFromAccount()];
        if (accountNumber == 0) {
            return new Failure("Invalid account type");
        }
        balances.setBalances(this.BALANCE[accountNumber], this.AVAILABLE_BALANCE[accountNumber]);
        return new Success();
    }

    public Status checkPIN(Card card, int pin) {
        if (card.getNumber() < 1 || card.getNumber() > PIN.length) {
            return new Failure("Invalid card");
        }
        if (pin != PIN[card.getNumber()]) {
            return new InvalidPIN();
        }
        return new Success();
    }

    private static class Failure
    extends Status {
        private String message;

        public Failure(String message) {
            this.message = message;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean isInvalidPIN() {
            return false;
        }

        @Override
        public String getMessage() {
            return this.message;
        }
    }

    private static class InvalidPIN
    extends Failure {
        public InvalidPIN() {
            super("Invalid PIN");
        }

        @Override
        public boolean isInvalidPIN() {
            return true;
        }
    }

    private static class Success
    extends Status {
        private Success() {
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public boolean isInvalidPIN() {
            return false;
        }

        @Override
        public String getMessage() {
            return null;
        }
    }
}

