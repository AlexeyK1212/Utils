import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {


    @Test
    void cache() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Fraction fr = new Fraction(1,2);
        Utils ut =new Utils();
        Fractionable num = ut.cache(fr);


        assertEquals(num.doubleValue (),0.5);
        assertEquals(outContent.toString().replaceAll("[\n\r]",""),"invoke double value");
        outContent.reset();

        assertEquals(num.doubleValue (),0.5);
        assertEquals(outContent.toString(),"");
        outContent.reset();

        assertEquals(num.doubleValue (),0.5);
        assertEquals(outContent.toString(),"");
        outContent.reset();

        num.setNum(5);
        assertEquals(num.doubleValue (),2.5);
        assertEquals(outContent.toString().replaceAll("[\n\r]",""),"invoke double value");
        outContent.reset();

        assertEquals(num.doubleValue (),2.5);
        assertEquals(outContent.toString(),"");
        outContent.reset();

        System.setOut(originalOut);
    }




}