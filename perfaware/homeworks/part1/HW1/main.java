import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class main {

  //write each pair to file
  //write + print filegg
  //nasm your output and compare with orignal binary
  public enum OP_CODE {
   mov
  }

  public enum REG_CODE {
        al,
        cl,
        dl,
        bl,
        ah,
        ch,
        dh,
        bh,
        ax,
        cx,
        dx,
        bx,
        sp,
        bp,
        si,  
        di

  }

  public static final HashMap<Integer,OP_CODE> opCodeMapping = new HashMap<>(Map.ofEntries(
        Map.entry(0b00100010,OP_CODE.mov)
        ));

  public static final HashMap<Integer,REG_CODE> regCodeMapping = new HashMap<>(Map.ofEntries(
        Map.entry(0b00000000, REG_CODE.al),
        Map.entry(0b00000001, REG_CODE.cl),
        Map.entry(0b00000010, REG_CODE.dl),
        Map.entry(0b00000011, REG_CODE.bl),
        Map.entry(0b00000100, REG_CODE.ah),
        Map.entry(0b00000101, REG_CODE.ch),
        Map.entry(0b00000110, REG_CODE.dh),
        Map.entry(0b00000111, REG_CODE.bh),
        Map.entry(0b00001000, REG_CODE.ax),
        Map.entry(0b00001001, REG_CODE.cx),
        Map.entry(0b00001010, REG_CODE.dx),
        Map.entry(0b00001011, REG_CODE.bx),
        Map.entry(0b00001100, REG_CODE.sp),
        Map.entry(0b00001101, REG_CODE.bp),
        Map.entry(0b00001110, REG_CODE.si),
        Map.entry(0b00001111, REG_CODE.di)
        ));

  private static boolean DEBUG = false;

  private static void debug(String msg){
    if (DEBUG) System.out.println(msg);
  }


    public static void main(String[] args) {
        if(args.length < 1) {
          System.err.println("need args");
          System.exit(1);
        } 
        Path filePath = Path.of(args[0]);
        try {

            System.out.println("bits 16");  
          byte[] content = Files.readAllBytes(filePath);
            debug("#######");                    
        for (int i = 0; i < content.length; i+=2) {
          byte first = content[i];
          byte second = content[i+1];
         //get the operator - leftmost bits of first
          //capture d flag 
          int operator = (first & 0xFF) >>> 2; //Shift right two, setting top 2 bits to 0 
          int D_FLAG = first & (1 << 1); // grab the second least significant
          int W_FLAG = first & (1 << 0); // grab the least significant
         //[2MOD,3REG,3R_M]
          int MOD = (second & 0xFF) >>> 6;
          int REG = (((second & 0xFF) << 2) & 0xFF) >>> 5;
          int R_M = (((second & 0xFF) << 5) & 0xFF) >>> 5; //first shift left by N then shift back right
         OP_CODE opCode = opCodeMapping.get(operator);
          debug(String.valueOf(opCode));
          int source = 0;
          int destination = 0;
          if(D_FLAG == 1) {
            destination = REG;
            source = R_M;
          } else if (D_FLAG == 0) {
            destination = R_M;
            source = REG;
          } else {
            System.err.println("ERROR: D_FLAG has non-binary value of : " + Integer.toBinaryString(D_FLAG));
          }
          //append W to front and call to register map to get register
          REG_CODE sourceRegister = null;
          REG_CODE destinationRegister = null;
          if(W_FLAG == 1) {
            int wideSource = source | (1 << 3);
            int wideDestination = destination | (1 << 3);
            sourceRegister = regCodeMapping.get(wideSource);
            destinationRegister = regCodeMapping.get(wideDestination);
          } else {
            sourceRegister = regCodeMapping.get(source);
            destinationRegister = regCodeMapping.get(destination);
          }
 
          debug("1st Byte : " + Integer.toBinaryString(first & 0xFF));
          debug("2nd Byte : " + Integer.toBinaryString(second & 0xFF));
          debug("OP_CODE = " + Integer.toBinaryString(operator));
          debug("D_FLAG = " + Integer.toBinaryString(D_FLAG));
          debug("W_FLAG = " + Integer.toBinaryString(W_FLAG));
           debug("#######");         
          debug("MOD = " + Integer.toBinaryString(MOD));
          debug("REG = " + Integer.toBinaryString(REG));
          debug("R_M = " + Integer.toBinaryString(R_M));
          debug("#######"); 
          
        System.out.println(opCode + " " + destinationRegister + ", " + sourceRegister); 
        }

        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
        }


    }

    private static void printBitArray(byte[] bytes) {
      String[] bitArray = new String[bytes.length];
      for (int i = 0; i < bytes.length; i++) {
        // & 0xFF prevents negative sign extension; %8s pads to 8 characters
        bitArray[i] = String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF))
                                .replace(' ', '0');
      }
        debug(Arrays.toString(bitArray));
    }
}

