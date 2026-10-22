package enums;

/**
 * Tipo de unidad dentro de la jerarquia de la Biblioteca FISEI.
 * Define que tipo de hijo puede tener cada unidad.
 */
public enum TipoUnidad {
    BIBLIOTECA, AREA, ESTANTE;

    /**
     * Regla de la jerarquia: BIBLIOTECA -> AREA -> ESTANTE.
     * @param hijo tipo de unidad que se desea agregar
     * @return true si esta unidad puede tener un hijo del tipo indicado
     */
    public boolean puedeTenerHijo(TipoUnidad hijo) {
        switch (this) {
            case BIBLIOTECA: return hijo == AREA;
            case AREA:       return hijo == ESTANTE;
            default:         return false; // un ESTANTE no puede tener hijos
        }
    }
}
