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
    MOV
  }

  public enum REG_CODE {
        AL,
        CL,
        DL,
        BL,
        AH,
        CH,
        DH,
        BH,
        AX,
        CX,
        DX,
        BX,
        SP,
        BP,
        SI,  
        DI

  }

  public static final HashMap<Integer,OP_CODE> opCodeMapping = new HashMap<>(Map.ofEntries(
        Map.entry(0b00100010,OP_CODE.MOV)
        ));

  public static final HashMap<Integer,REG_CODE> regCodeMapping = new HashMap<>(Map.ofEntries(
        Map.entry(0b00000000, REG_CODE.AL),
        Map.entry(0b00000001, REG_CODE.CL),
        Map.entry(0b00000010, REG_CODE.DL),
        Map.entry(0b00000011, REG_CODE.BL),
        Map.entry(0b00000100, REG_CODE.AH),
        Map.entry(0b00000101, REG_CODE.CH),
        Map.entry(0b00000110, REG_CODE.DH),
        Map.entry(0b00000111, REG_CODE.BH),
        Map.entry(0b00001000, REG_CODE.AX),
        Map.entry(0b00001001, REG_CODE.CX),
        Map.entry(0b00001010, REG_CODE.DX),
        Map.entry(0b00001011, REG_CODE.BX),
        Map.entry(0b00001100, REG_CODE.SP),
        Map.entry(0b00001101, REG_CODE.BP),
        Map.entry(0b00001110, REG_CODE.SI),
        Map.entry(0b00001111, REG_CODE.DI)
        ));


    public static void main(String[] args) {
        Path filePath = Path.of("listing_0037_single_register_mov");

        try {
            byte[] content = Files.readAllBytes(filePath);
            System.out.println("#######");                    
        for (int i = 0; i < content.length; i+=2) {

          byte first = content[i];
          byte second = content[i+1];
          System.out.println("1st Byte : " + Integer.toBinaryString(first & 0xFF));
          System.out.println("2nd Byte : " + Integer.toBinaryString(second & 0xFF));
          //get the operator - leftmost bits of first
          //capture d flag 
          int operator = (first & 0xFF) >>> 2; //Shift right two, setting top 2 bits to 0 
          int D_FLAG = first & (1 << 1); // grab the second least significant
          int W_FLAG = first & (1 << 0); // grab the least significant
          System.out.println("OP_CODE = " + Integer.toBinaryString(operator));
          System.out.println("D_FLAG = " + Integer.toBinaryString(D_FLAG));
          System.out.println("W_FLAG = " + Integer.toBinaryString(W_FLAG));
           System.out.println("#######");         
          //[2MOD,3REG,3R_M]
          int MOD = (second & 0xFF) >>> 6;
          int REG = (((second & 0xFF) << 2) & 0xFF) >>> 5;
          int R_M = (((second & 0xFF) << 5) & 0xFF) >>> 5; //first shift left by N then shift back right
          System.out.println("MOD = " + Integer.toBinaryString(MOD));
          System.out.println("REG = " + Integer.toBinaryString(REG));
          System.out.println("R_M = " + Integer.toBinaryString(R_M));
          System.out.println("#######"); 

          OP_CODE opCode = opCodeMapping.get(operator);
          System.out.println(opCode);

          int source = 0;
          int destination = 0;
          if(D_FLAG == 1) {
            destination = REG;
            source = R_M;
          } else if (D_FLAG == 0) {
            destination = R_M;
            source = REG;
          } else {
            System.out.println("ERROR: D_FLAG has non-binary value of : " + Integer.toBinaryString(D_FLAG));
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

        System.out.println("FINAL | " + opCode + " " + destinationRegister + ", " + sourceRegister);
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
        System.out.println(Arrays.toString(bitArray));
    }
}

