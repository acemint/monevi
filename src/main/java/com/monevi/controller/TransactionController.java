package com.monevi.controller;

import com.monevi.converter.Converter;
import com.monevi.converter.TransactionToTransactionResponseConverter;
import com.monevi.dto.request.CreateTransactionRequest;
import com.monevi.dto.request.UpdateTransactionRequest;
import com.monevi.dto.response.BaseResponse;
import com.monevi.dto.response.MultipleBaseResponse;
import com.monevi.dto.response.TransactionResponse;
import com.monevi.entity.Transaction;
import com.monevi.enums.EntryPosition;
import com.monevi.enums.GeneralLedgerAccountType;
import com.monevi.enums.TransactionType;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetTransactionFilter;
import com.monevi.service.TransactionService;
import com.monevi.util.TransactionUrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiPath.BASE + ApiPath.TRANSACTION)
public class TransactionController {

  @Autowired
  private TransactionService transactionService;

  @Autowired
  @Qualifier(TransactionToTransactionResponseConverter.TRANSACTION_TO_TRANSACTION_RESPONSE_BEAN_NAME + Converter.SUFFIX_BEAN_NAME)
  private Converter<Transaction, TransactionResponse> transactionToTransactionResponseConverter;

  @PostMapping(value = ApiPath.CREATE_NEW, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<TransactionResponse> createNewTransaction(
      @Valid @RequestBody CreateTransactionRequest createTransactionRequest) throws ApplicationException {
    Transaction newTransaction = this.transactionService.createNewTransaction(
        createTransactionRequest);
    return BaseResponse.<TransactionResponse>builder()
        .value(this.transactionToTransactionResponseConverter.convert(newTransaction))
        .build();
  }

  @PutMapping(value = ApiPath.EDIT)
  public BaseResponse<TransactionResponse> updateTransaction(
      @RequestParam @NotBlank String transactionId,
      @Valid @RequestBody UpdateTransactionRequest updateTransactionRequest)
      throws ApplicationException {
    Transaction newTransaction =
        this.transactionService.updateTransaction(transactionId, updateTransactionRequest);
    return BaseResponse.<TransactionResponse>builder()
        .value(this.transactionToTransactionResponseConverter.convert(newTransaction)).build();
  }

  @GetMapping(value = ApiPath.FIND_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
  public MultipleBaseResponse<TransactionResponse> getTransactions(
      @RequestParam(required = false) String organizationRegionId, 
      @RequestParam String startDate,
      @RequestParam String endDate,
      @RequestParam(required = false) String regionId,
      @RequestParam(required = false) EntryPosition entryPosition,
      @RequestParam(required = false) GeneralLedgerAccountType generalLedgerAccountType,
      @RequestParam(required = false) TransactionType transactionType,
      @RequestParam(defaultValue = "0", required = false) int page,
      @RequestParam(defaultValue = "1000", required = false) int size,
      @RequestParam(defaultValue = "transactionDate", required = false) String[] sortBy,
      @RequestParam(defaultValue = "true", required = false) String[] isAscending)
      throws ApplicationException {
    GetTransactionFilter filter = this.buildDefaultGetTransactionFilter(organizationRegionId,
        regionId, entryPosition,
        generalLedgerAccountType, transactionType, startDate, endDate, sortBy, isAscending, page, size);
    List<TransactionResponse> transactionResponses = this.transactionService.getTransactions(filter)
        .stream()
        .map(t -> this.transactionToTransactionResponseConverter.convert(t))
        .collect(Collectors.toList());
    return MultipleBaseResponse.<TransactionResponse>builder()
        .values(transactionResponses)
        .metadata(MultipleBaseResponse.Metadata
            .builder()
            .size(transactionResponses.size())
            .totalPage(0)
            .totalItems(transactionResponses.size())
            .build())
        .build();
  }

  private GetTransactionFilter buildDefaultGetTransactionFilter(String organizationRegionId,
      String regionId, EntryPosition entryPosition,
      GeneralLedgerAccountType generalLedgerAccountType, TransactionType transactionType,
      String startDate, String endDate, String[] sortBy, String[] isAscending, int page, int size)
      throws ApplicationException {
    List<Sort.Order> sortOrders = new ArrayList<>();
    int validSize = Math.min(sortBy.length, isAscending.length);
    for (int i = 0; i < validSize; i++) {
      TransactionUrlUtils.checkValidSortedBy(sortBy[i]);
      sortOrders
          .add(new Sort.Order(TransactionUrlUtils.getSortDirection(isAscending[i]), sortBy[i]));
    }
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortOrders));
    return GetTransactionFilter.builder()
        .organizationRegionId(organizationRegionId)
        .regionId(regionId)
        .startDate(startDate)
        .endDate(endDate)
        .entryPosition(entryPosition)
        .generalLedgerAccountType(generalLedgerAccountType)
        .transactionType(transactionType)
        .pageable(pageable)
        .build();
  }

}
