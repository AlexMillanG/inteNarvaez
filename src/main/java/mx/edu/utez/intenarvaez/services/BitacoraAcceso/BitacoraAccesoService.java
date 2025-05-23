package mx.edu.utez.intenarvaez.services.BitacoraAcceso;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import mx.edu.utez.intenarvaez.models.BitacoraAcceso.BitacoraAccesoEntity;
import mx.edu.utez.intenarvaez.models.BitacoraAcceso.BitacoraAccesoRepository;
import mx.edu.utez.intenarvaez.models.user.UserEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BitacoraAccesoService {
    private final BitacoraAccesoRepository bitacoraRepository;

    public void guardarBitacoraAcceso(HttpServletRequest request, UserEntity user, boolean exito, String mensaje) {
        BitacoraAccesoEntity bitacora = new BitacoraAccesoEntity();
        bitacora.setUser(user);
        bitacora.setUsuario(user.getEmail());
        bitacora.setIpOrigen(request.getRemoteAddr());
        bitacora.setUserAgent(request.getHeader("User-Agent"));
        bitacora.setExito(exito);
        bitacora.setMensaje(mensaje);
        bitacoraRepository.save(bitacora);
    }

}
