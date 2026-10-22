package negocio;

import enums.TipoRecorrido;
import modelo.Libro;
import java.util.ArrayList;
import java.util.List;

/**
 * ABB (Arbol Binario de Busqueda) que almacena los libros de un estante,
 * ordenados por el codigo del libro (entero positivo, sin repetidos).
 * Todas las operaciones se implementan mediante recursividad.
 */
public class CatalogoABB {
    private NodoLibro raiz;

    public NodoLibro getRaiz() { return raiz; }

    public void setRaiz(NodoLibro raiz) { this.raiz = raiz; }

    // ------------------------------------------------------------------
    // Insercion
    // ------------------------------------------------------------------

    /** Inserta un libro por codigo. Devuelve false si el codigo ya existe. */
    public boolean insertar(Libro libro) {
        if (libro == null) {
            return false;
        }
        if (buscar(libro.getCodigo()) != null) {
            return false;
        }
        raiz = insertar(raiz, libro);
        return true;
    }

    private NodoLibro insertar(NodoLibro nodo, Libro libro) {
        if (nodo == null) {
            return new NodoLibro(libro);
        }
        if (libro.getCodigo() < nodo.getDato().getCodigo()) {
            nodo.setIzquierdo(insertar(nodo.getIzquierdo(), libro));
        } else {
            nodo.setDerecho(insertar(nodo.getDerecho(), libro));
        }
        return nodo;
    }

    // ------------------------------------------------------------------
    // Busqueda
    // ------------------------------------------------------------------

    /** Busca un libro por codigo. Devuelve null si no existe. */
    public Libro buscar(int codigo) {
        NodoLibro nodo = buscarNodo(raiz, codigo);
        return (nodo == null) ? null : nodo.getDato();
    }

    private NodoLibro buscarNodo(NodoLibro nodo, int codigo) {
        if (nodo == null || nodo.getDato().getCodigo() == codigo) {
            return nodo;
        }
        if (codigo < nodo.getDato().getCodigo()) {
            return buscarNodo(nodo.getIzquierdo(), codigo);
        }
        return buscarNodo(nodo.getDerecho(), codigo);
    }

    // ------------------------------------------------------------------
    // Conteo y recorridos
    // ------------------------------------------------------------------

    /** Numero de libros del catalogo. */
    public int contar() {
        return contar(raiz);
    }

    private int contar(NodoLibro nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contar(nodo.getIzquierdo()) + contar(nodo.getDerecho());
    }

    /** Recorrido en preorden, inorden o posorden segun el tipo indicado. */
    public List<Libro> recorrer(TipoRecorrido tipo) {
        List<Libro> resultado = new ArrayList<>();
        recorrer(raiz, tipo, resultado);
        return resultado;
    }

    private void recorrer(NodoLibro nodo, TipoRecorrido tipo, List<Libro> resultado) {
        if (nodo == null) {
            return;
        }
        if (tipo == TipoRecorrido.PREORDEN) {
            resultado.add(nodo.getDato());
        }
        recorrer(nodo.getIzquierdo(), tipo, resultado);
        if (tipo == TipoRecorrido.INORDEN) {
            resultado.add(nodo.getDato());
        }
        recorrer(nodo.getDerecho(), tipo, resultado);
        if (tipo == TipoRecorrido.POSORDEN) {
            resultado.add(nodo.getDato());
        }
    }

    // ------------------------------------------------------------------
    // Traza del recorrido
    // ------------------------------------------------------------------

    /**
     * Traza del recorrido con el formato: "entra X" al entrar al nodo,
     * "VISITA X" al procesarlo y "null -> regresa" en un hijo vacio;
     * 2 espacios por nivel; ultima linea "Secuencia TIPO: ...".
     */
    public String trazaRecorrido(TipoRecorrido tipo) {
        StringBuilder sb = new StringBuilder();
        List<Libro> secuencia = new ArrayList<>();
        trazar(raiz, tipo, 0, sb, secuencia);
        sb.append("Secuencia ").append(tipo).append(": ");
        for (int i = 0; i < secuencia.size(); i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(secuencia.get(i).getCodigo());
        }
        sb.append("\n");
        return sb.toString();
    }

    private void trazar(NodoLibro nodo, TipoRecorrido tipo, int nivel, StringBuilder sb, List<Libro> secuencia) {
        String sangria = "  ".repeat(nivel);
        if (nodo == null) {
            return;
        }
        sb.append(sangria).append("entra ").append(nodo.getDato().getCodigo()).append("\n");
        if (tipo == TipoRecorrido.PREORDEN) {
            sb.append(sangria).append("VISITA ").append(nodo.getDato().getCodigo()).append("\n");
            secuencia.add(nodo.getDato());
            visitarHijo(nodo.getIzquierdo(), tipo, nivel, sb, secuencia, "izquierdo");
            visitarHijo(nodo.getDerecho(), tipo, nivel, sb, secuencia, "derecho");
        } else if (tipo == TipoRecorrido.INORDEN) {
            visitarHijo(nodo.getIzquierdo(), tipo, nivel, sb, secuencia, "izquierdo");
            sb.append(sangria).append("VISITA ").append(nodo.getDato().getCodigo()).append("\n");
            secuencia.add(nodo.getDato());
            visitarHijo(nodo.getDerecho(), tipo, nivel, sb, secuencia, "derecho");
        } else {
            visitarHijo(nodo.getIzquierdo(), tipo, nivel, sb, secuencia, "izquierdo");
            visitarHijo(nodo.getDerecho(), tipo, nivel, sb, secuencia, "derecho");
            sb.append(sangria).append("VISITA ").append(nodo.getDato().getCodigo()).append("\n");
            secuencia.add(nodo.getDato());
        }
    }

    private void visitarHijo(NodoLibro hijo, TipoRecorrido tipo, int nivel, StringBuilder sb,
                             List<Libro> secuencia, String lado) {
        String sangria = "  ".repeat(nivel + 1);
        if (hijo == null) {
            sb.append(sangria).append("null -> regresa (").append(lado).append(")\n");
        } else {
            trazar(hijo, tipo, nivel + 1, sb, secuencia);
        }
    }

    // ------------------------------------------------------------------
    // Modificacion
    // ------------------------------------------------------------------

    /** Modifica titulo, autor y anio de un libro (nunca el codigo). false si no existe. */
    public boolean modificar(int codigo, String titulo, String autor, int anio) {
        NodoLibro nodo = buscarNodo(raiz, codigo);
        if (nodo == null) {
            return false;
        }
        // Se construye un libro de validacion para reutilizar las reglas de negocio
        Libro validacion = new Libro(codigo, titulo, autor, anio);
        nodo.getDato().setTitulo(validacion.getTitulo());
        nodo.getDato().setAutor(validacion.getAutor());
        nodo.getDato().setAnio(validacion.getAnio());
        return true;
    }

    // ------------------------------------------------------------------
    // Eliminacion (hoja, un hijo, dos hijos con sucesor inorden)
    // ------------------------------------------------------------------

    /** Elimina un libro por codigo. Devuelve false si no existe. */
    public boolean eliminar(int codigo) {
        if (buscarNodo(raiz, codigo) == null) {
            return false;
        }
        raiz = eliminar(raiz, codigo);
        return true;
    }

    private NodoLibro eliminar(NodoLibro nodo, int codigo) {
        if (codigo < nodo.getDato().getCodigo()) {
            nodo.setIzquierdo(eliminar(nodo.getIzquierdo(), codigo));
        } else if (codigo > nodo.getDato().getCodigo()) {
            nodo.setDerecho(eliminar(nodo.getDerecho(), codigo));
        } else {
            // Caso 1: hoja
            if (nodo.getIzquierdo() == null && nodo.getDerecho() == null) {
                return null;
            }
            // Caso 2: un solo hijo
            if (nodo.getIzquierdo() == null) {
                return nodo.getDerecho();
            }
            if (nodo.getDerecho() == null) {
                return nodo.getIzquierdo();
            }
            // Caso 3: dos hijos -> reemplazo por el sucesor inorden
            NodoLibro sucesor = minimo(nodo.getDerecho());
            nodo.setDato(sucesor.getDato());
            nodo.setDerecho(eliminar(nodo.getDerecho(), sucesor.getDato().getCodigo()));
        }
        return nodo;
    }

    private NodoLibro minimo(NodoLibro nodo) {
        while (nodo.getIzquierdo() != null) {
            nodo = nodo.getIzquierdo();
        }
        return nodo;
    }

    // ------------------------------------------------------------------
    // Retos (Parte D)
    // ------------------------------------------------------------------

    /** Cuenta los codigos entre a y b, con poda de subarboles que no pueden contener el rango. */
    public int contarEnRango(int a, int b) {
        return contarEnRango(raiz, a, b);
    }

    private int contarEnRango(NodoLibro nodo, int a, int b) {
        if (nodo == null) {
            return 0;
        }
        int codigo = nodo.getDato().getCodigo();
        if (codigo < a) {
            // Todo el subarbol izquierdo esta fuera del rango: poda
            return contarEnRango(nodo.getDerecho(), a, b);
        }
        if (codigo > b) {
            // Todo el subarbol derecho esta fuera del rango: poda
            return contarEnRango(nodo.getIzquierdo(), a, b);
        }
        return 1 + contarEnRango(nodo.getIzquierdo(), a, b) + contarEnRango(nodo.getDerecho(), a, b);
    }

/**
 * Codigos visitados desde la raiz hasta el libro buscado. Si el libro no existe,
 * se registra la ruta hasta el ultimo nodo visitado (donde termina la busqueda).
 */
public List<Integer> caminoBusqueda(int codigo) {
    List<Integer> camino = new ArrayList<>();
    caminoBusqueda(raiz, codigo, camino);
    return camino;
}

private void caminoBusqueda(NodoLibro nodo, int codigo, List<Integer> camino) {
    if (nodo == null) {
        return;
    }
    camino.add(nodo.getDato().getCodigo());
    if (nodo.getDato().getCodigo() == codigo) {
        return;
    }
    if (codigo < nodo.getDato().getCodigo()) {
        caminoBusqueda(nodo.getIzquierdo(), codigo, camino);
    } else {
        caminoBusqueda(nodo.getDerecho(), codigo, camino);
    }
}

    /** Verifica que todo el arbol cumpla la propiedad ABB con limites minimo y maximo. */
    public boolean esABBValido() {
        return esABBValido(raiz, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private boolean esABBValido(NodoLibro nodo, int minimo, int maximo) {
        if (nodo == null) {
            return true;
        }
        int codigo = nodo.getDato().getCodigo();
        if (codigo <= minimo || codigo >= maximo) {
            return false;
        }
        return esABBValido(nodo.getIzquierdo(), minimo, codigo)
                && esABBValido(nodo.getDerecho(), codigo, maximo);
    }

    /**
     * Extra: reconstruye el ABB a partir de su recorrido en preorden,
     * sin insertar uno por uno (usa limites minimo/maximo y un indice compartido).
     */
    public static CatalogoABB desdePreorden(int[] preorden) {
        if (preorden == null || preorden.length == 0) {
            return new CatalogoABB();
        }
        int[] indice = {0};
        NodoLibro raiz = construirDesdePreorden(preorden, indice, Integer.MIN_VALUE, Integer.MAX_VALUE);
        CatalogoABB catalogo = new CatalogoABB();
        catalogo.setRaiz(raiz);
        return catalogo;
    }

    private static NodoLibro construirDesdePreorden(int[] preorden, int[] indice, int minimo, int maximo) {
        if (indice[0] >= preorden.length) {
            return null;
        }
        int codigo = preorden[indice[0]];
        if (codigo <= minimo || codigo >= maximo) {
            return null;
        }
        indice[0]++;
        NodoLibro nodo = new NodoLibro(new Libro(codigo, "Libro " + codigo, "Autor " + codigo, 2026));
        nodo.setIzquierdo(construirDesdePreorden(preorden, indice, minimo, codigo));
        nodo.setDerecho(construirDesdePreorden(preorden, indice, codigo, maximo));
        return nodo;
    }
}
