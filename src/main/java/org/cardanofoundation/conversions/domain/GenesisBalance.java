package org.cardanofoundation.conversions.domain;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class GenesisBalance {

  private String address;

  private String txnHash;

  private BigInteger balance;
}
