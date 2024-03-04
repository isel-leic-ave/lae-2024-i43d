package apps;

import java.time.LocalDate;


class Constants{
   static final int BITS_OF_10KB = 8*10*1024;
}


public class Amber {
   private short id;
   private String title;
   private String author;
   private static final String DEFAULT_AUTHOR = "";
   private LocalDate checkedOut;
   private static final LocalDate DEFAULT_CHECKED = LocalDate.now();
   private int pageCount;
   private boolean isAvailable;
   public Amber() {
      title = "MSC";
   }
}