package asint;

import java.io.FileReader;

public class Main {
    public static void main(String[] args) throws Exception {
      try{
         ConstructorASTsTiny parser = new ConstructorASTsTiny(new FileReader(args[0]));
         parser.disable_tracing();
         parser.analiza();
         System.out.println("OK");
      } catch (TokenMgrError e) {
         System.err.println("ERROR LEXICO: " + e.getMessage());
      } catch (ParseException e) {
         System.err.println("ERROR SINTACTICO: " + e.getMessage());
      } catch (Exception e) {
         System.err.println("OTRO ERROR: " + e.getMessage());
      }
   }
}