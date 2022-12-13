package com.monevi.converter;

public interface Converter<S, R> {

  public static final String SUFFIX_BEAN_NAME = "Converter";

  R convert(S source);

}
