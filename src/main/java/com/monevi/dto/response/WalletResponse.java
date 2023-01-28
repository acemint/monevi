package com.monevi.dto.response;

import com.monevi.enums.GeneralLedgerAccountType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletResponse {

  private String id;
  private String organizationRegionId;
  private GeneralLedgerAccountType name;
  private Double total;
}
