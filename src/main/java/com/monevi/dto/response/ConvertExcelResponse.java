package com.monevi.dto.response;

import java.util.List;

import com.monevi.dto.request.CreateTransactionRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConvertExcelResponse {

  private Integer skippedRow;
  private List<Integer> skippedRowList;
  private Integer success;
  private List<CreateTransactionRequest> processedTransaction;
}
