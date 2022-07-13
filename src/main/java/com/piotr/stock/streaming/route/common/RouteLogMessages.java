package com.piotr.stock.streaming.route.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RouteLogMessages {

  public static final String STEP_START_RETRIEVE = "Start retrieve stock route";
  public static final String STEP_START_SAVE = "Start save stock route";
  public static final String STEP_FIND_STOCK = "Retrieve stock list from database";
  public static final String STEP_MAPPING_ENTITY = "Mapping stock entity to DTO";
  public static final String STEP_MAPPING_SAVED_ENTITY = "Mapping saved stock entity to DTO";
  public static final String STEP_MAPPING_DTO = "Mapping stock DTO to entity";
  public static final String STEP_SAVE_STOCK = "Saving stock in database";

  public static String stepDoneMessage(String message) {
    StringBuilder sb = new StringBuilder();
    sb.append("STEP DONE: ");
    sb.append(message);
    return sb.toString();
  }
}
