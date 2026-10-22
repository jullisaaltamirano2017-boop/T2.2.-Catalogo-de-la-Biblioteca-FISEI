package app;

import enums.TipoUnidad;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import modelo.Libro;

/**
 * Carga la estructura inicial de la Biblioteca FISEI indicada en el deber:
 *
 * BIB Biblioteca FISEI
 *  |- SW   Software          -> SW-E1: 60,35,80,20,45,70,90,40,75 | SW-E2: 15,10,25
 *  |- TI   Tecnologias de la Informacion -> TI-E1: 50
 *  |- TEL Telecomunicaciones
 */
public class DatosPrueba {

    public static ArbolBiblioteca cargarBibliotecaInicial() {
        ArbolBiblioteca arbol = new ArbolBiblioteca(
                new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));

        arbol.agregar("BIB", new UnidadBiblioteca("SW", "Software", TipoUnidad.AREA));
        arbol.agregar("BIB", new UnidadBiblioteca("TI", "Tecnologías de la Información", TipoUnidad.AREA));
        arbol.agregar("BIB", new UnidadBiblioteca("TEL", "Telecomunicaciones", TipoUnidad.AREA));

        arbol.agregar("SW", new UnidadBiblioteca("SW-E1", "Estante 1 de Software", TipoUnidad.ESTANTE));
        arbol.agregar("SW", new UnidadBiblioteca("SW-E2", "Estante 2 de Software", TipoUnidad.ESTANTE));
        arbol.agregar("TI", new UnidadBiblioteca("TI-E1", "Estante 1 de TI", TipoUnidad.ESTANTE));

        int[] librosSW1 = {60, 35, 80, 20, 45, 70, 90, 40, 75};
        CatalogoABB cat1 = arbol.buscar("SW-E1").getCatalogo();
        for (int codigo : librosSW1) {
            cat1.insertar(crearLibro(codigo));
        }

        int[] librosSW2 = {15, 10, 25};
        CatalogoABB cat2 = arbol.buscar("SW-E2").getCatalogo();
        for (int codigo : librosSW2) {
            cat2.insertar(crearLibro(codigo));
        }

        CatalogoABB cat3 = arbol.buscar("TI-E1").getCatalogo();
        cat3.insertar(crearLibro(50));

        return arbol;
    }

    /** Crea un libro con datos de ejemplo a partir de su codigo. */
    public static Libro crearLibro(int codigo) {
        return new Libro(codigo, "Libro " + codigo, "Autor " + codigo, 2000 + (codigo % 27));
    }
}
