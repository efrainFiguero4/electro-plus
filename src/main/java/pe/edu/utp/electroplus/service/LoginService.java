package pe.edu.utp.electroplus.service;

public interface LoginService {
    boolean isAuthenticated();

    public void iniciarSesion(String email, String contrasena);
}
