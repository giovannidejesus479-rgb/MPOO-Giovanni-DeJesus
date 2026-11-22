import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

class Result {

    /*
     * Complete the 'comprimir' function below.
     *
     * The function is expected to return a STRING.
     * The function accepts STRING datos as parameter.
     */
     
     public static String comprimir(String datos) {
    if (datos == null || datos.isEmpty()) {
        return "";
    }

    datos = datos.toUpperCase();

    StringBuilder resultado = new StringBuilder();
    int contador = 1;

    for (int i = 0; i < datos.length(); i++) {
        if (i + 1 < datos.length() && datos.charAt(i) == datos.charAt(i + 1)) {
            contador++;
        } else {
            
            while (contador > 9) {
                resultado.append(9).append(datos.charAt(i));
                contador -= 9;
            }
            resultado.append(contador).append(datos.charAt(i));
            contador = 1;
        }
    }

    return resultado.toString();
}

}

public class RLE {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String datos = bufferedReader.readLine();

        String result = Result.comprimir(datos);

        bufferedWriter.write(result);
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
