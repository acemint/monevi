package com.monevi.controller;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.monevi.converter.Converter;
import com.monevi.converter.WalletToWalletResponseConverter;
import com.monevi.dto.response.BaseResponse;
import com.monevi.dto.response.WalletResponse;
import com.monevi.entity.Wallet;
import com.monevi.enums.GeneralLedgerAccountType;
import com.monevi.exception.ApplicationException;
import com.monevi.service.WalletService;

@RestController
@RequestMapping(ApiPath.BASE + ApiPath.WALLET)
public class WalletController {

  @Autowired
  private WalletService walletService;

  @Autowired
  @Qualifier(WalletToWalletResponseConverter.COMPONENT_NAME + Converter.SUFFIX_BEAN_NAME)
  private Converter<Wallet, WalletResponse> walletToWalletResponseConverter;

  @GetMapping(value = ApiPath.CALCULATE)
  public BaseResponse<WalletResponse> calculateTransactionByGeneralLedgerAccountType(
      @RequestParam GeneralLedgerAccountType type,
      @RequestParam @NotBlank String organizationRegionId) throws ApplicationException {
    Wallet wallet = this.walletService
        .findWalletByGeneralLedgerAccountTypeAndOrganizationRegion(type, organizationRegionId);
    return BaseResponse.<WalletResponse>builder()
        .value(this.walletToWalletResponseConverter.convert(wallet)).build();
  }
}
