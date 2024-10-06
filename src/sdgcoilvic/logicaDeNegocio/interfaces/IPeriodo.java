package sdgcoilvic.logicaDeNegocio.interfaces;

    import java.sql.SQLException;
    import java.util.List;
    import sdgcoilvic.logicaDeNegocio.clases.Periodo;

    public interface IPeriodo {
    int agregarPeriodo(Periodo periodo) throws SQLException;
    public List<List<String>> obtenerListaDePeriodos() ;
    }
