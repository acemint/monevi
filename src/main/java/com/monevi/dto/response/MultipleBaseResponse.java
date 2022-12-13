package com.monevi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultipleBaseResponse<T> implements Serializable {

  private Page<T> values;

  @Data
  public static class Metadata {

    private int totalItems;
    private int totalPage;
    private int size;

  }

}
