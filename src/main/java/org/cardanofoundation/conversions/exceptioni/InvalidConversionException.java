package org.cardanofoundation.conversions.exceptioni;

/**
 * Exception that signals the conversions is not possible. An example is trying to convert into a
 * slot a date which is antecedent the start of the blockchain.
 */
public class InvalidConversionException extends Exception {

  public InvalidConversionException() {}

  public InvalidConversionException(String message) {
    super(message);
  }

  public InvalidConversionException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidConversionException(Throwable cause) {
    super(cause);
  }

  public InvalidConversionException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
