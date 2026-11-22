import java.util.Scanner;

/**
 * Clase que representa la estructura de una cuenta bancaria individual.
 */
class CuentaBancaria {
    // Atributos privados para proteger la información de la cuenta
    private String numeroCuenta;
    private String titular;
    private double saldo;
    private boolean activa;

    // Constructor para inicializar los datos requeridos
    public CuentaBancaria(String numeroCuenta, String titular, double saldoInicial, boolean activa) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldoInicial;
        this.activa = activa;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    // El saldo no se modifica directamente, se usan métodos controlados
    public boolean retirar(double cantidad) {
        // Validación: La cuenta debe estar activa y no puede retirar más de su saldo
        if (!activa || cantidad > saldo) {
            return false;
        }

        saldo -= cantidad;
        return true; // Retorna true si la validación se cumple y se modifica el saldo
    }

    public boolean depositar(double cantidad) {
        // Validación: La cuenta destino debe estar activa
        if (!activa) {
            return false;
        }

        saldo += cantidad;
        return true;
    }

    public double getSaldo() {
        return saldo;
    }
}

/**
 * Clase que realiza las operaciones y administra el repositorio de cuentas.
 */
class CuentaBancariaService {
    // Arreglo como atributo que sirve como repositorio de información de las cuentas
    private CuentaBancaria[] cuentas;

    public CuentaBancariaService() {
        // Inicialización del arreglo con diferentes cuentas para pruebas
        cuentas = new CuentaBancaria[]{
            new CuentaBancaria("1001", "Karina", 5000.0, true),
            new CuentaBancaria("1002", "Daniela", 3000.0, true),
            new CuentaBancaria("1003", "Samantha", 1500.0, false) // Cuenta inactiva
        };
    }

    // Método privado para buscar cuentas iterando, sin usar posiciones fijas del arreglo
    private CuentaBancaria buscarCuenta(String numeroCuenta) {
        for (CuentaBancaria cuenta : cuentas) {
            // Si la cuenta existe, regresa la referencia; si no, al final regresará null
            if (cuenta != null && cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                return cuenta;
            }
        }
        return null;
    }

    // Operación 1: Transferir dinero
    public boolean transferir(String cuentaOrigen, String cuentaDestino, double cantidad) {
        // Validación: La cantidad debe ser mayor a cero y las cuentas deben ser diferentes
        if (cantidad <= 0 || cuentaOrigen.equals(cuentaDestino)) {
            return false;
        }

        // Búsqueda de cuentas en el repositorio
        CuentaBancaria origen = buscarCuenta(cuentaOrigen);
        CuentaBancaria destino = buscarCuenta(cuentaDestino);

        // Validación: Las cuentas involucradas deben existir (no ser null)
        if (origen == null || destino == null) {
            return false;
        }

        // Se intenta retirar de la cuenta origen (valida saldo suficiente internamente)
        if (origen.retirar(cantidad)) {
            // Si el retiro fue exitoso, se intenta depositar en el destino
            if (destino.depositar(cantidad)) {
                return true; // Movimiento completado correctamente
            } else {
                // Si el depósito falla (ej. cuenta destino inactiva), se regresa el dinero al origen
                origen.depositar(cantidad);
            }
        }

        // Retorna false si alguna validación no se cumple y ningún saldo final es modificado
        return false;
    }

    // Operación 2: Depositar dinero
    public boolean depositar(String cuentaDestino, double cantidad) {
        // Validación: La cantidad debe ser mayor que cero
        if (cantidad <= 0) {
            return false;
        }

        CuentaBancaria destino = buscarCuenta(cuentaDestino);
        // Validación: La cuenta debe existir y el depósito debe ser exitoso
        return destino != null && destino.depositar(cantidad);
    }

    // Operación 3: Retirar dinero
    public boolean retirar(String cuentaOrigen, double cantidad) {
        // Validación: La cantidad proporcionada debe ser válida (mayor a cero)
        if (cantidad <= 0) {
            return false;
        }

        CuentaBancaria origen = buscarCuenta(cuentaOrigen);
        // Validación: La cuenta debe existir y el retiro debe concretarse
        return origen != null && origen.retirar(cantidad);
    }
}

/**
 * Clase principal que contiene el menú de la terminal.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CuentaBancariaService servicio = new CuentaBancariaService();
        int opcion = 0;

        // El menú permanece ejecutándose en la terminal hasta seleccionar la opción 4
        while (opcion != 4) {
            System.out.println("\n====== SISTEMA BANCARIO ======");
            System.out.println("1. Transferir dinero");
            System.out.println("2. Depositar dinero");
            System.out.println("3. Retirar dinero");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer de entrada del scanner

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese número de cuenta de origen: ");
                    String origen = scanner.nextLine();
                    System.out.print("Ingrese número de cuenta de destino: ");
                    String destino = scanner.nextLine();
                    System.out.print("Ingrese la cantidad a transferir: ");
                    double cantTransferir = scanner.nextDouble();

                    if (servicio.transferir(origen, destino, cantTransferir)) {
                        System.out.println("Transferencia realizada con éxito.");
                    } else {
                        System.out.println("Error: No se pudo realizar la transferencia. Revise las cuentas, el estado o sus fondos.");
                    }
                    break;

                case 2:
                    System.out.print("Ingrese número de cuenta de destino: ");
                    String cuentaDep = scanner.nextLine();
                    System.out.print("Ingrese la cantidad a depositar: ");
                    double cantDep = scanner.nextDouble();

                    if (servicio.depositar(cuentaDep, cantDep)) {
                        System.out.println("Depósito realizado con éxito.");
                    } else {
                        System.out.println("Error: No se pudo realizar el depósito. Verifique la cuenta o la cantidad.");
                    }
                    break;

                case 3:
                    System.out.print("Ingrese número de cuenta de origen: ");
                    String cuentaRet = scanner.nextLine();
                    System.out.print("Ingrese la cantidad a retirar: ");
                    double cantRet = scanner.nextDouble();

                    if (servicio.retirar(cuentaRet, cantRet)) {
                        System.out.println("Retiro realizado con éxito.");
                    } else {
                        System.out.println("Error: No se pudo realizar el retiro. Verifique la cuenta, sus fondos o el estado de la misma.");
                    }
                    break;

                case 4:
                    System.out.println("Saliendo del sistema bancario...");
                    break;

                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción del 1 al 4.");
            }
        }

        scanner.close();
    }
}