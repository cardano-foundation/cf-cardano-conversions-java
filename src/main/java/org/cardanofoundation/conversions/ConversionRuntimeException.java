package org.cardanofoundation.conversions;

public class ConversionRuntimeException extends RuntimeException {

  public ConversionRuntimeException() {}

  public ConversionRuntimeException(String message) {
    super(message);
  }

  public ConversionRuntimeException(Throwable cause) {
    super(cause);
  }

  public ConversionRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }
}
