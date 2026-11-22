import java.util.Objects;
import java.util.Random;
import java.util.UUID;

class InfoPropietario {
    private String nombrePropietario;
    private String equipo;

    public InfoPropietario(String nombrePropietario, String equipo) {
        this.nombrePropietario = nombrePropietario;
        this.equipo = equipo;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public String getEquipo() {
        return equipo;
    }

    @Override
    public String toString() {
        return nombrePropietario + " (Equipo: " + equipo + ")";
    }
}

class Bateria {
    private String tipo;
    private double cargaPorcentaje;

    public Bateria(String tipo, double cargaInicial) {
        this.tipo = tipo;
        this.cargaPorcentaje = Math.max(0.0, Math.min(100.0, cargaInicial));
    }

    public void consumir(double cantidad) {
        if (cantidad > 0) {
            this.cargaPorcentaje = Math.max(0.0, this.cargaPorcentaje - cantidad);
        }
    }

    public void recargar(double cantidad) {
        if (cantidad > 0) {
            this.cargaPorcentaje = Math.min(100.0, this.cargaPorcentaje + cantidad);
        }
    }

    public double getCargaPorcentaje() {
        return cargaPorcentaje;
    }

    @Override
    public String toString() {
        return String.format("%s [%.1f%%]", tipo, cargaPorcentaje);
    }
}

class Robot {
    private static int totalRobots = 0;
    private static final Random RANDOM = new Random();

    private final UUID id;
    private String nombre;
    private String categoria;
    private double pesoKg;
    private double velocidadMaxima;
    private double velocidadActual;
    private double distanciaRecorrida;
    private InfoPropietario propietario;
    private Bateria bateria;

    public Robot(String nombre, String categoria, double pesoKg, double velocidadMaxima,
                 double cargaInicial, InfoPropietario propietario, String tipoBateria) {
        this.id = UUID.randomUUID();
        this.nombre = nombre;
        this.categoria = categoria;
        this.pesoKg = pesoKg;
        this.velocidadMaxima = velocidadMaxima;
        this.velocidadActual = 0.0;
        this.distanciaRecorrida = 0.0;
        this.propietario = propietario;
        this.bateria = new Bateria(tipoBateria, cargaInicial);
        totalRobots++;
    }

    public static int getTotalRobots() {
        return totalRobots;
    }

    public void ajustarVelocidad(double nuevaVelocidad) {
        if (bateria.getCargaPorcentaje() <= 0.0) {
            this.velocidadActual = 0.0;
            return;
        }
        this.velocidadActual = Math.max(0.0, Math.min(nuevaVelocidad, this.velocidadMaxima));
    }

    public double avanzar(double segundos) {
        if (segundos <= 0.0 || bateria.getCargaPorcentaje() <= 0.0) {
            return 0.0;
        }
        double factorVelocidad = 0.5 + RANDOM.nextDouble() * 0.5;
        double velocidadEfectiva = this.velocidadMaxima * factorVelocidad;
        ajustarVelocidad(velocidadEfectiva);

        double avance = this.velocidadActual * segundos;
        this.distanciaRecorrida += avance;

        double consumoBateria = 2.0 + RANDOM.nextDouble() * 8.0;
        this.bateria.consumir(consumoBateria);

        return avance;
    }

    public void detener() {
        this.velocidadActual = 0.0;
    }

    public double calcularIndiceCombate() {
        return (this.pesoKg * 0.4) + (this.velocidadMaxima * 1.5) + (this.bateria.getCargaPorcentaje() * 0.3);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("Robot{id=%s\n nombre='%s'\n categoria='%s'\n peso=%.1fkg\n velActual=%.2f\n velMax=%.2f\n dist=%.2f\n bateria=%s\n propietario=%s}\n", id, nombre, categoria, pesoKg, velocidadActual, velocidadMaxima, distanciaRecorrida, bateria, propietario);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Robot otro)) return false;
        return Objects.equals(this.id, otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

public class BattleBot {
    public static void main(String[] args) {
        InfoPropietario prop1 = new InfoPropietario("Ana López", "FI-01");
        InfoPropietario prop2 = new InfoPropietario("Carlos Ruiz", "FI-02");
        InfoPropietario prop3 = new InfoPropietario("Sofía Méndez", "FI-03");

        Robot robot1 = new Robot("Tritón", "Peso Medio", 25.0, 12.0, 95.0, prop1, "LiPo");
        Robot robot2 = new Robot("Valkiria", "Peso Medio", 24.5, 14.0, 100.0, prop2, "LiPo");
        Robot robot3 = new Robot("Ares", "Peso Ligero", 15.0, 18.0, 90.0, prop3, "Li-Ion");

        System.out.println("Robots participando en la batalla: " + Robot.getTotalRobots());

        System.out.println("\n--- Estado inicial ---");
        System.out.println(robot1);
        System.out.println(robot2);
        System.out.println(robot3);

        System.out.println("\n--- Simulando acciones ---");
        double av1 = robot1.avanzar(3.0);
        double av2 = robot2.avanzar(4.0);
        robot3.ajustarVelocidad(10.0);
        double av3 = robot3.avanzar(2.5);
        robot3.detener();

        System.out.printf("Avances -> Tritón: %.2f m | Valkiria: %.2f m | Ares: %.2f m%n", av1, av2, av3);
        System.out.printf("Índices de combate -> Tritón: %.2f | Valkiria: %.2f | Ares: %.2f%n", robot1.calcularIndiceCombate(), robot2.calcularIndiceCombate(), robot3.calcularIndiceCombate());

        System.out.println("\n--- Estado después de la batalla ---");
        System.out.println(robot1);
        System.out.println(robot2);
        System.out.println(robot3);

        System.out.println("\n--- Referencias e igualdad ---");
        // Segunda referencia al mismo objeto sin new
        Robot robotFavorito = robot1;

        //Modificar el objeto y comprobar al imprimir robot1 y robotFavorito
        robot1.setNombre("Tritón MK-II");
        robot1.avanzar(2.0);

        System.out.println("Imprimiendo robot1 tras modificarlo:");
        System.out.println(robot1);

        System.out.println("Imprimiendo robotFavorito (misma referencia):");
        System.out.println(robotFavorito);

        //Nuevo robot con los mismos datos iniciales que robot1, pero con su propio 'new'
        Robot robot2IgualInicial = new Robot("Tritón", "Peso Medio", 25.0, 12.0, 95.0, prop1, "LiPo");

        System.out.println("Prueba robot1.equals(robot2IgualInicial): " + robot1.equals(robot2IgualInicial));
        System.out.println("Prueba robot1.equals(robotFavorito): " + robot1.equals(robotFavorito));
        System.out.println("Total de robots creados al finalizar: " + Robot.getTotalRobots());
    }
}