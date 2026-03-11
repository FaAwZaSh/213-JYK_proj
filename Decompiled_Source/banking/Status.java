/*
 * Decompiled with CFR 0.152.
 */
package banking;

public abstract class Status {
    public String toString() {
        if (this.isSuccess()) {
            return "SUCCESS";
        }
        if (this.isInvalidPIN()) {
            return "INVALID PIN";
        }
        return "FAILURE " + this.getMessage();
    }

    public abstract boolean isSuccess();

    public abstract boolean isInvalidPIN();

    public abstract String getMessage();
}

