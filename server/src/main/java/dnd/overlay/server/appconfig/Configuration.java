package dnd.overlay.server.appconfig;

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
   
   @Getter
   private static double sMinImageSize = 60;

}
