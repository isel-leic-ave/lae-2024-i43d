package pt.isel;

public class SavingsAccount {
    /**
     * Static FIELD
     */
    private static int accountsCount; // count the number of instances created
    /**
     * Instance (non-static) FIELDS
     */
    private short accountCode; // 2 bytes
    private String holderName; // 8 bytes
    private long balance;      // 8 bytes
    private boolean isActive;  // 1 byte
    private final double interestRate = 0.02; // 8 bytes
    
    public SavingsAccount() {
        accountCode++;
    }
    
}
