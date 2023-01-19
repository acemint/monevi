package com.monevi.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.monevi.constant.ErrorMessages;
import com.monevi.dto.request.CreateTransactionRequest;
import com.monevi.dto.response.ConvertExcelResponse;
import com.monevi.entity.OrganizationRegion;
import com.monevi.enums.EntryPosition;
import com.monevi.enums.GeneralLedgerAccountType;
import com.monevi.enums.TransactionType;
import com.monevi.exception.ApplicationException;
import com.monevi.repository.OrganizationRegionRepository;
import com.monevi.service.ExcelService;
import com.monevi.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExcelServiceImpl implements ExcelService {

  private static final String FILE_TYPE =
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

  @Autowired
  private OrganizationRegionRepository organizationRegionRepository;

  @Override
  public ConvertExcelResponse processExcelFile(String organizationRegionId, MultipartFile file)
      throws ApplicationException, IOException {
    int success = 0, row = 1;
    List<Integer> skippedRows = new ArrayList<>();
    List<CreateTransactionRequest> processedTransaction = new ArrayList<>();
    if (isExcelFile(file.getContentType())) {
      OrganizationRegion organizationRegion =
          this.organizationRegionRepository.findByIdAndMarkForDeleteIsFalse(organizationRegionId)
              .orElseThrow(() -> new ApplicationException(HttpStatus.BAD_REQUEST,
                  ErrorMessages.ORGANIZATION_REGION_DOES_NOT_EXISTS));
      XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
      XSSFSheet page = Optional.ofNullable(this.getFirstSheet(workbook)).orElseThrow(
          () -> new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.NO_DATA_IN_EXCEL));

      while (checkFirstCellDataInRow(row, page)) {
        XSSFRow rowData = page.getRow(row);
        try {
          CreateTransactionRequest transaction =
              this.convertToTransaction(rowData, organizationRegion);
          processedTransaction.add(transaction);
          success += 1;
        } catch (IllegalArgumentException | NullPointerException e) {
          log.warn("processExcelFile ERROR! data at row {} will be skipped", (row + 1));
          skippedRows.add(row + 1);
        }
        row += 1;
      }
      return this.toConvertExcelResponse(skippedRows, success, processedTransaction);
    } else {
      throw new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_FILE_TYPE);
    }
  }

  private Boolean isExcelFile(String uploadedFileType) {
    return FILE_TYPE.equals(uploadedFileType);
  }

  private XSSFSheet getFirstSheet(XSSFWorkbook file) {
    try {
      return file.getSheetAt(0);
    } catch (Exception e) {
      return null;
    }
  }

  private boolean checkFirstCellDataInRow(int row, XSSFSheet page) {
    return Objects.nonNull(page.getRow(row).getCell(0).getLocalDateTimeCellValue());
  }

  private CreateTransactionRequest convertToTransaction(XSSFRow rowData,
      OrganizationRegion organizationRegion)
      throws ApplicationException, IllegalArgumentException, NullPointerException {
    return CreateTransactionRequest.builder().organizationRegionId(organizationRegion.getId())
        .transactionDate(this.getDateStringCellValue(rowData, 0))
        .generalLedgerAccountType(
            GeneralLedgerAccountType.convertExcelValue(this.getStringCellValue(rowData, 1)))
        .entryPosition(EntryPosition.convertExcelValue(this.getStringCellValue(rowData, 2)))
        .name(this.getStringCellValue(rowData, 3))
        .type(TransactionType.convertExcelValue(this.getStringCellValue(rowData, 4)))
        .description(this.getStringCellValue(rowData, 5))
        .amount(this.getAndValidateAmount(rowData, 6)).build();
  }

  private String getDateStringCellValue(XSSFRow rowData, int cellNumber)
      throws ApplicationException {
    return DateUtils.convertToDefaultPattern(
        rowData.getCell(cellNumber).getLocalDateTimeCellValue().toLocalDate().toString());
  }

  private String getStringCellValue(XSSFRow rowData, int cellNumber) {
    String data = rowData.getCell(cellNumber).getStringCellValue();
    if (StringUtils.isBlank(data)) {
      throw new NullPointerException(ErrorMessages.NO_DATA_IN_EXCEL);
    }
    return data;
  }

  private Double getAndValidateAmount(XSSFRow rowData, int cellNumber) {
    Double data = rowData.getCell(cellNumber).getNumericCellValue();
    if (data <= 0) {
      throw new IllegalArgumentException(ErrorMessages.AMOUNT_MUST_BE_GREATER_THAN_ZERO);
    }
    return data;
  }

  private ConvertExcelResponse toConvertExcelResponse(List<Integer> skippedRows, Integer success,
      List<CreateTransactionRequest> processedTransaction) {
    return ConvertExcelResponse.builder()
        .skippedRow(skippedRows.size())
        .skippedRowList(skippedRows)
        .success(success)
        .processedTransaction(processedTransaction).build();
  }
}
