package org.cardanofoundation.conversions.exceptioni;

/**
 * This Exception might be raised in conversions that should be implemented but the specific
 * conversion is not available. An example could be a slot or time conversion that is only available
 * for Shelley onwards and not Byron.
 */
public class UnsupportedConversionException extends Exception {

  public UnsupportedConversionException() {}

  public UnsupportedConversionException(String message) {
    super(message);
  }

  public UnsupportedConversionException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnsupportedConversionException(Throwable cause) {
    super(cause);
  }

  public UnsupportedConversionException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
