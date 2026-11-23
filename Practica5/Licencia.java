import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

class Result {

    enum TipoLicencia {
        BASICA, PROFESIONAL, EMPRESARIAL, TEMPORAL
    }

    enum EstadoLicencia {
        VIGENTE, PROXIMA_A_VENCER, VENCIDA, BLOQUEADA
    }

    public static String evaluarLicencia(String fechaActual, String fechaVencimiento, String tipoLicencia, int renovacionesPrevias) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date dateActual = sdf.parse(fechaActual.trim());
            Date dateVencimiento = sdf.parse(fechaVencimiento.trim());

            long diffMillis = dateVencimiento.getTime() - dateActual.getTime();
            long diasRest = diffMillis / (1000L * 60L * 60L * 24L);

            EstadoLicencia estado;
            if (diasRest > 30) {
                estado = EstadoLicencia.VIGENTE;
            } else if (diasRest >= 0) {
                estado = EstadoLicencia.PROXIMA_A_VENCER;
            } else if (diasRest >= -90) {
                estado = EstadoLicencia.VENCIDA;
            } else {
                estado = EstadoLicencia.BLOQUEADA;
            }

            if (estado == EstadoLicencia.BLOQUEADA) {
                return estado + " " + diasRest + " 0.00 NO_DISPONIBLE";
            }

            double costoBase = 0.0;
            int addYear = 0;
            int addMonth = 0;

            TipoLicencia tipo = TipoLicencia.valueOf(tipoLicencia.trim().toUpperCase(Locale.ROOT));

            switch (tipo) {
                case BASICA:
                    costoBase = 1000.00;
                    addYear = 1;
                    break;
                case PROFESIONAL:
                    costoBase = 1500.00;
                    addYear = 2;
                    break;
                case EMPRESARIAL:
                    costoBase = 2500.00;
                    addYear = 3;
                    break;
                case TEMPORAL:
                    costoBase = 600.00;
                    addMonth = 6;
                    break;
            }

            double costoFinal = costoBase;
            if (estado == EstadoLicencia.VIGENTE) {
                costoFinal *= 0.90;
            } else if (estado == EstadoLicencia.VENCIDA) {
                costoFinal *= 1.20;
            }

            if (renovacionesPrevias > 3) {
                costoFinal *= 0.95;
            }

            Calendar nuevaFechaCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            if (estado == EstadoLicencia.VIGENTE || estado == EstadoLicencia.PROXIMA_A_VENCER) {
                nuevaFechaCal.setTime(dateVencimiento);
            } else {
                nuevaFechaCal.setTime(dateActual);
            }

            if (addYear > 0) {
                nuevaFechaCal.add(Calendar.YEAR, addYear);
            }
            if (addMonth > 0) {
                nuevaFechaCal.add(Calendar.MONTH, addMonth);
            }

            String nuevaFechaStr = sdf.format(nuevaFechaCal.getTime());
            String costoStr = String.format(Locale.US, "%.2f", costoFinal);

            return estado + " " + diasRest + " " + costoStr + " " + nuevaFechaStr;

        } catch (ParseException | IllegalArgumentException e) {
            return "ERROR_FECHA";
        }
    }
}

public class Licencia {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String fechaActual = bufferedReader.readLine();
        String fechaVencimiento = bufferedReader.readLine();
        String tipoLicencia = bufferedReader.readLine();
        int renovacionesPrevias = Integer.parseInt(bufferedReader.readLine().trim());

        String result = Result.evaluarLicencia(
                fechaActual,
                fechaVencimiento,
                tipoLicencia,
                renovacionesPrevias
        );

        System.out.println(result);
        bufferedReader.close();
    }
}