import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/*
 * Contrato comun de las publicaciones.
 */

interface Publicable {

    ResultadoPublicacion publicar();

    Long getId();

    String getAutor();
}


/*
 * ==================================================
 * DTO BASE
 * ==================================================
 *
 * Contiene solamente la informacion comun recibida
 * desde HackerRank.
 */

abstract class PublicacionDTO {

    private final Long id;
    private final String autor;
    private final String titulo;

    private final int meGusta;
    private final int comentarios;
    private final int compartidos;


    protected PublicacionDTO(
            Long id,
            String autor,
            String titulo,
            int meGusta,
            int comentarios,
            int compartidos
    ) {

        this.id = id;
        this.autor = autor;
        this.titulo = titulo;
        this.meGusta = meGusta;
        this.comentarios = comentarios;
        this.compartidos = compartidos;
    }


    public Long getId() {
        return id;
    }

    public String getAutor() {
        return autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getMeGusta() {
        return meGusta;
    }

    public int getComentarios() {
        return comentarios;
    }

    public int getCompartidos() {
        return compartidos;
    }
}


/*
 * ==================================================
 * DTO TEXTO
 * ==================================================
 */

final class PublicacionTextoDTO
        extends PublicacionDTO {

    private final String texto;


    public PublicacionTextoDTO(
            Long id,
            String autor,
            String titulo,
            int meGusta,
            int comentarios,
            int compartidos,
            String texto
    ) {

        super(
                id,
                autor,
                titulo,
                meGusta,
                comentarios,
                compartidos
        );

        this.texto = texto;
    }


    public String getTexto() {
        return texto;
    }
}


/*
 * ==================================================
 * DTO IMAGEN
 * ==================================================
 */

final class PublicacionImagenDTO
        extends PublicacionDTO {

    private final String descripcionImagen;
    private final String formato;


    public PublicacionImagenDTO(
            Long id,
            String autor,
            String titulo,
            int meGusta,
            int comentarios,
            int compartidos,
            String descripcionImagen,
            String formato
    ) {

        super(
                id,
                autor,
                titulo,
                meGusta,
                comentarios,
                compartidos
        );

        this.descripcionImagen =
                descripcionImagen;

        this.formato =
                formato;
    }


    public String getDescripcionImagen() {
        return descripcionImagen;
    }

    public String getFormato() {
        return formato;
    }
}


/*
 * ==================================================
 * DTO VIDEO
 * ==================================================
 */

final class PublicacionVideoDTO
        extends PublicacionDTO {

    private final int duracionSegundos;
    private final int visualizaciones;


    public PublicacionVideoDTO(
            Long id,
            String autor,
            String titulo,
            int meGusta,
            int comentarios,
            int compartidos,
            int duracionSegundos,
            int visualizaciones
    ) {

        super(
                id,
                autor,
                titulo,
                meGusta,
                comentarios,
                compartidos
        );

        this.duracionSegundos =
                duracionSegundos;

        this.visualizaciones =
                visualizaciones;
    }


    public int getDuracionSegundos() {
        return duracionSegundos;
    }

    public int getVisualizaciones() {
        return visualizaciones;
    }
}

/*
 * Implementa aqui toda la solucion de Vibe.
 *
 * Debes construir las clases y enumeraciones
 * solicitadas en la practica.
 *
 * No modifiques Publicable ni los DTO proporcionados.
 */

// ==========================================
// ENUMERACIONES Y CLASE DE RESULTADO
// ==========================================

enum FormatoImagen {
    JPG, PNG, WEBP
}

enum EstadoPublicacion {
    RECHAZADA, PUBLICADA, DESTACADA, VIRAL
}

class ResultadoPublicacion {
    private final Long idPublicacion;
    private final String autor;
    private final String descripcion;
    private final double relevancia;
    private final EstadoPublicacion estado;
    private final LocalDateTime fechaProcesamiento;

    public ResultadoPublicacion(Long idPublicacion, String autor, String descripcion, double relevancia, EstadoPublicacion estado) {
        this.idPublicacion = idPublicacion;
        this.autor = autor;
        this.descripcion = descripcion;
        this.relevancia = relevancia;
        this.estado = estado;
        this.fechaProcesamiento = LocalDateTime.now();
    }

    public Long getIdPublicacion() { return idPublicacion; }
    public String getAutor() { return autor; }
    public String getDescripcion() { return descripcion; }
    public double getRelevancia() { return relevancia; }
    public EstadoPublicacion getEstado() { return estado; }
    public LocalDateTime getFechaProcesamiento() { return fechaProcesamiento; }
}

// ==========================================
// CLASE ABSTRACTA BASE (TEMPLATE METHOD)
// ==========================================

abstract class Publicacion implements Publicable {
    protected Long id;
    protected String autor;
    protected String titulo;
    protected int meGusta;
    protected int comentarios;
    protected int compartidos;

    public Publicacion(Long id, String autor, String titulo, int meGusta, int comentarios, int compartidos) {
        this.id = id;
        this.autor = autor;
        this.titulo = titulo;
        this.meGusta = meGusta;
        this.comentarios = comentarios;
        this.compartidos = compartidos;
    }

    @Override
    public Long getId() { return id; }

    @Override
    public String getAutor() { return autor; }

    protected abstract boolean validarContenido();
    protected abstract double calcularRelevancia();
    protected abstract String getDescripcion();

    @Override
    public final ResultadoPublicacion publicar() {
        boolean contenidoValido = validarContenido();
        EstadoPublicacion estado;
        double relevancia = 0.0;

        if (!contenidoValido) {
            estado = EstadoPublicacion.RECHAZADA;
        } else {
            relevancia = calcularRelevancia();
            if (relevancia < 100) {
                estado = EstadoPublicacion.PUBLICADA;
            } else if (relevancia < 300) {
                estado = EstadoPublicacion.DESTACADA;
            } else {
                estado = EstadoPublicacion.VIRAL;
            }
        }

        String descripcion = getDescripcion();
        return new ResultadoPublicacion(id, autor, descripcion, relevancia, estado);
    }
}

class PublicacionTexto extends Publicacion {
    private String texto;

    public PublicacionTexto(Long id, String autor, String titulo, int meGusta, int comentarios, int compartidos, String texto) {
        super(id, autor, titulo, meGusta, comentarios, compartidos);
        this.texto = texto;
    }

    @Override
    protected boolean validarContenido() {
        if (texto == null) return false;
        int len = texto.length();
        return len >= 10 && len <= 280;
    }

    @Override
    protected double calcularRelevancia() {
        return meGusta + (comentarios * 3.0) + (compartidos * 5.0);
    }

    @Override
    protected String getDescripcion() {
        return "Texto | " + titulo + " | " + autor;
    }
}

class PublicacionImagen extends Publicacion {
    private String descripcionImagen;
    private FormatoImagen formato;

    public PublicacionImagen(Long id, String autor, String titulo, int meGusta, int comentarios, int compartidos, String descripcionImagen, FormatoImagen formato) {
        super(id, autor, titulo, meGusta, comentarios, compartidos);
        this.descripcionImagen = descripcionImagen;
        this.formato = formato;
    }

    @Override
    protected boolean validarContenido() {
        return descripcionImagen != null && !descripcionImagen.trim().isEmpty() && formato != null;
    }

    @Override
    protected double calcularRelevancia() {
        return (meGusta * 2.0) + (comentarios * 3.0) + (compartidos * 5.0);
    }

    @Override
    protected String getDescripcion() {
        return "Imagen | " + titulo + " | " + formato + " | " + autor;
    }
}

class PublicacionVideo extends Publicacion {
    private int duracionSegundos;
    private int visualizaciones;

    public PublicacionVideo(Long id, String autor, String titulo, int meGusta, int comentarios, int compartidos, int duracionSegundos, int visualizaciones) {
        super(id, autor, titulo, meGusta, comentarios, compartidos);
        this.duracionSegundos = duracionSegundos;
        this.visualizaciones = visualizaciones;
    }

    @Override
    protected boolean validarContenido() {
        return duracionSegundos > 0 && duracionSegundos <= 600;
    }

    @Override
    protected double calcularRelevancia() {
        return (meGusta * 2.0) + (comentarios * 4.0) + (compartidos * 6.0) + (visualizaciones / 100.0);
    }

    @Override
    protected String getDescripcion() {
        return "Video | " + titulo + " | " + duracionSegundos + " s | " + autor;
    }
}

// ==========================================
// ADMINISTRADOR FEED
// ==========================================

class Feed {
    private List<ResultadoPublicacion> publicaciones = new ArrayList<>();

    public ResultadoPublicacion procesar(Publicable publicable) {
        ResultadoPublicacion resultado = publicable.publicar();
        publicaciones.add(resultado);
        return resultado;
    }

    public List<ResultadoPublicacion> procesar(List<Publicable> listaPublicables) {
        List<ResultadoPublicacion> resultados = new ArrayList<>();
        for (Publicable p : listaPublicables) {
            resultados.add(procesar(p));
        }
        return resultados;
    }

    public List<ResultadoPublicacion> obtenerVirales() {
        List<ResultadoPublicacion> virales = new ArrayList<>();
        for (ResultadoPublicacion r : publicaciones) {
            if (r.getEstado() == EstadoPublicacion.VIRAL) {
                virales.add(r);
            }
        }
        return virales;
    }

    public List<ResultadoPublicacion> generarRanking() {
        List<ResultadoPublicacion> copia = new ArrayList<>(publicaciones);
        copia.sort((r1, r2) -> {
            int cmp = Double.compare(r2.getRelevancia(), r1.getRelevancia());
            if (cmp != 0) {
                return cmp;
            }
            return Integer.compare(publicaciones.indexOf(r1), publicaciones.indexOf(r2));
        });
        return copia;
    }
}

/*
 * ==================================================
 * ADAPTADOR ENTRE HACKERRANK Y TU DOMINIO
 * ==================================================
 *
 * Convierte los DTO recibidos desde HackerRank
 * en objetos de tu modelo.
 *
 * No coloques aqui reglas de negocio.
 */

class Result {


    public static Publicable convertir(
            PublicacionTextoDTO entrada
    ) {

        /*
         * Construye una PublicacionTexto
         * utilizando tu propio diseno.
         */
         return new PublicacionTexto(entrada.getId(), entrada.getAutor(), entrada.getTitulo(), entrada.getMeGusta(), entrada.getComentarios(), entrada.getCompartidos(), entrada.getTexto());
    }


    public static Publicable convertir(
            PublicacionImagenDTO entrada
    ) {

        /*
         * Construye una PublicacionImagen.
         *
         * Convierte entrada.getFormato()
         * al tipo que utilice tu modelo.
         */
         FormatoImagen formato = FormatoImagen.valueOf(entrada.getFormato());
         return new PublicacionImagen(entrada.getId(), entrada.getAutor(), entrada.getTitulo(), entrada.getMeGusta(), entrada.getComentarios(), entrada.getCompartidos(), entrada.getDescripcionImagen(), formato);
    }


    public static Publicable convertir(
            PublicacionVideoDTO entrada
    ) {

        /*
         * Construye una PublicacionVideo
         * utilizando tu propio diseno.
         */
         return new PublicacionVideo(entrada.getId(), entrada.getAutor(), entrada.getTitulo(), entrada.getMeGusta(), entrada.getComentarios(), entrada.getCompartidos(), entrada.getDuracionSegundos(), entrada.getVisualizaciones());
    }
}

final class HackerRankVibe {

    private HackerRankVibe() {
    }


    public static void ejecutar()
            throws Exception {

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                System.in
                        )
                );


        String primeraLinea =
                reader.readLine();


        if (primeraLinea == null
                || primeraLinea.isBlank()) {

            return;
        }


        int numeroPublicaciones =
                Integer.parseInt(
                        primeraLinea.trim()
                );


        List<Publicable> publicaciones =
                new ArrayList<>();


        /*
         * ==============================================
         * ENTRADA
         * ==============================================
         */

        for (int i = 0;
             i < numeroPublicaciones;
             i++) {

            String linea =
                    reader.readLine();


            if (linea == null) {

                throw new IllegalArgumentException(
                        "Faltan publicaciones"
                );
            }


            Publicable publicacion =
                    convertirEntrada(
                            linea
                    );


            if (publicacion == null) {

                throw new IllegalStateException(
                        "La conversion regreso null"
                );
            }


            publicaciones.add(
                    publicacion
            );
        }


        /*
         * ==============================================
         * PROCESAMIENTO INDIVIDUAL
         * ==============================================
         */

        Feed feedIndividual =
                new Feed();


        for (Publicable publicacion
                : publicaciones) {

            ResultadoPublicacion resultado =
                    feedIndividual.procesar(
                            publicacion
                    );


            imprimirResultado(
                    resultado
            );
        }


        /*
         * ==============================================
         * PROCESAMIENTO DE COLECCION
         * ==============================================
         */

        Feed feed =
                new Feed();


        feed.procesar(
                publicaciones
        );


        /*
         * ==============================================
         * RANKING
         * ==============================================
         */

        System.out.println(
                "RANKING"
        );


        List<ResultadoPublicacion> ranking =
                feed.generarRanking();


        for (ResultadoPublicacion resultado
                : ranking) {

            imprimirRanking(
                    resultado
            );
        }


        /*
         * ==============================================
         * VIRALES
         * ==============================================
         */

        System.out.println(
                "VIRALES"
        );


        List<ResultadoPublicacion> virales =
                feed.obtenerVirales();


        for (ResultadoPublicacion resultado
                : virales) {

            imprimirViral(
                    resultado
            );
        }
    }


    /*
     * ==============================================
     * IDENTIFICACION DEL TIPO DE ENTRADA
     * ==============================================
     */

    private static Publicable convertirEntrada(
            String linea
    ) {

        if (linea == null
                || linea.isBlank()) {

            throw new IllegalArgumentException(
                    "La linea no puede estar vacia"
            );
        }


        String[] partes =
                linea.split(
                        "\\|",
                        -1
                );


        String tipo =
                partes[0]
                        .trim()
                        .toUpperCase();


        switch (tipo) {

            case "TEXTO":
                return convertirTexto(
                        partes
                );

            case "IMAGEN":
                return convertirImagen(
                        partes
                );

            case "VIDEO":
                return convertirVideo(
                        partes
                );

            default:
                throw new IllegalArgumentException(
                        "Tipo de publicacion invalido"
                );
        }
    }


    /*
     * ==============================================
     * TEXTO
     * ==============================================
     */

    private static Publicable convertirTexto(
            String[] partes
    ) {

        if (partes.length != 8) {

            throw new IllegalArgumentException(
                    "Formato TEXTO invalido"
            );
        }


        PublicacionTextoDTO entrada =
                new PublicacionTextoDTO(

                        Long.parseLong(
                                partes[1].trim()
                        ),

                        partes[2].trim(),

                        partes[3].trim(),

                        Integer.parseInt(
                                partes[4].trim()
                        ),

                        Integer.parseInt(
                                partes[5].trim()
                        ),

                        Integer.parseInt(
                                partes[6].trim()
                        ),

                        partes[7].trim()
                );


        return Result.convertir(
                entrada
        );
    }


    /*
     * ==============================================
     * IMAGEN
     * ==============================================
     */

    private static Publicable convertirImagen(
            String[] partes
    ) {

        if (partes.length != 9) {

            throw new IllegalArgumentException(
                    "Formato IMAGEN invalido"
            );
        }


        PublicacionImagenDTO entrada =
                new PublicacionImagenDTO(

                        Long.parseLong(
                                partes[1].trim()
                        ),

                        partes[2].trim(),

                        partes[3].trim(),

                        Integer.parseInt(
                                partes[4].trim()
                        ),

                        Integer.parseInt(
                                partes[5].trim()
                        ),

                        Integer.parseInt(
                                partes[6].trim()
                        ),

                        partes[7].trim(),

                        partes[8]
                                .trim()
                                .toUpperCase()
                );


        return Result.convertir(
                entrada
        );
    }


    /*
     * ==============================================
     * VIDEO
     * ==============================================
     */

    private static Publicable convertirVideo(
            String[] partes
    ) {

        if (partes.length != 9) {

            throw new IllegalArgumentException(
                    "Formato VIDEO invalido"
            );
        }


        PublicacionVideoDTO entrada =
                new PublicacionVideoDTO(

                        Long.parseLong(
                                partes[1].trim()
                        ),

                        partes[2].trim(),

                        partes[3].trim(),

                        Integer.parseInt(
                                partes[4].trim()
                        ),

                        Integer.parseInt(
                                partes[5].trim()
                        ),

                        Integer.parseInt(
                                partes[6].trim()
                        ),

                        Integer.parseInt(
                                partes[7].trim()
                        ),

                        Integer.parseInt(
                                partes[8].trim()
                        )
                );


        return Result.convertir(
                entrada
        );
    }


    /*
     * ==============================================
     * SALIDA PRINCIPAL
     * ==============================================
     */

    private static void imprimirResultado(
            ResultadoPublicacion resultado
    ) {

        System.out.printf(
                "%d|%s|%.2f|%s%n",

                resultado.getIdPublicacion(),

                resultado.getDescripcion(),

                resultado.getRelevancia(),

                resultado.getEstado()
        );
    }


    /*
     * ==============================================
     * RANKING
     * ==============================================
     */

    private static void imprimirRanking(
            ResultadoPublicacion resultado
    ) {

        System.out.printf(
                "%d|%.2f|%s%n",

                resultado.getIdPublicacion(),

                resultado.getRelevancia(),

                resultado.getEstado()
        );
    }


    /*
     * ==============================================
     * VIRALES
     * ==============================================
     */

    private static void imprimirViral(
            ResultadoPublicacion resultado
    ) {

        System.out.printf(
                "%d|%s|%.2f%n",

                resultado.getIdPublicacion(),

                resultado.getDescripcion(),

                resultado.getRelevancia()
        );
    }
}


/*
 * ==============================================
 * SOLUTION
 * ==============================================
 */

public class Solution {

    public static void main(String[] args)
            throws Exception {

        HackerRankVibe.ejecutar();
    }
}