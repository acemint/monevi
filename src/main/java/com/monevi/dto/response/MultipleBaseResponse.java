package com.monevi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultipleBaseResponse<T> implements Serializable {

  private List<T> values;
  private Metadata metadata;

  @Data
  @Builder
  public static class Metadata {

    private int totalItems;
    private int totalPage;
    private int size;

  }

}
