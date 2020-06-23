package com.github.wkod.dnd.overlay.client.appconfig;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Configuration.
 * @author cpawek
 * @version 1.0
 */
@Accessors(prefix = "s")
public final class Configuration {
   
   private Configuration() {
   }
   
   public static void readArgs(String[] pArgs) {
      if (pArgs.length > 0) {
         sUrl = "http://" + pArgs[0] + ":8080/api/overlay";
      }
   }
   
   @Getter
   private static double sMinImageSize = 60;

   @Getter
   private static String sUrl = "http://localhost:8080/api/overlay";
}
