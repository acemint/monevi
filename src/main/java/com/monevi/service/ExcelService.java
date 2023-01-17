package com.monevi.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.monevi.dto.response.ConvertExcelResponse;
import com.monevi.exception.ApplicationException;

public interface ExcelService {

  ConvertExcelResponse processExcelFile(String organizationRegionId, MultipartFile file)
      throws ApplicationException, IOException;
}
