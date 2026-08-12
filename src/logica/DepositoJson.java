package logica;

public class DepositoJson {
    public int id;
    public String nombre;
    public boolean auditado;
    public int[] conexiones;


}

class DepositosWrapper {
    public DepositoJson[] depositos;
}
