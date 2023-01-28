package com.monevi.converter;

import org.springframework.stereotype.Component;

import com.monevi.dto.response.WalletResponse;
import com.monevi.entity.Wallet;

@Component(value = WalletToWalletResponseConverter.COMPONENT_NAME + Converter.SUFFIX_BEAN_NAME)
public class WalletToWalletResponseConverter implements Converter<Wallet, WalletResponse> {

  public static final String COMPONENT_NAME = "WalletToWalletResponse";

  @Override
  public WalletResponse convert(Wallet source) {
    return WalletResponse.builder()
        .id(source.getId())
        .organizationRegionId(source.getOrganizationRegion().getId())
        .name(source.getName())
        .total(source.getTotal())
        .build();
  }
}
