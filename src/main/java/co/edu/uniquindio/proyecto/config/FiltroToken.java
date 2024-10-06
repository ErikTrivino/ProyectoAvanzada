package co.edu.uniquindio.proyecto.config;

import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.modelo.enums.Rol;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthorities;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FiltroToken extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Configuración de cabeceras para CORS
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, Content-Type, Authorization");

        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {

            // Obtener la URI de la petición que se está realizando
            String requestURI = request.getRequestURI();

            // Obtener el token de la petición del encabezado del mensaje HTTP
            String token = getToken(request);
            boolean error = true;

            try {

                // Si la petición es para la ruta /api/cliente se verifica que el token exista y que el rol sea CLIENTE
                if (requestURI.startsWith("/api/cliente")) {
                    error = validarToken(token, Rol.CLIENTE);
                } else if (requestURI.startsWith("/api/admin")) {
                    error = validarToken(token, Rol.ADMIN);
                } else {
                    error = false;
                }

                // Verificar si el usuario está autenticado
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && !authentication.getAuthorities().isEmpty()) {
                    Collection<GrantedAuthorities> authorities = authentication.getAuthorities();
                    for (GrantedAuthorities authority : authorities) {
                        if (authority.getAuthority().equals("CLIENTE")) {
                            error = false;
                        } else if (authority.getAuthority().equals("ADMIN")) {
                            error = false;
                        }
                    }
                }

            } catch (MalformedJwtException | SignatureException e) {
                crearRespuestaError("El token es incorrecto", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
            } catch (ExpiredJwtException e) {
                crearRespuestaError("El token está vencido", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
            } catch (Exception e) {
                crearRespuestaError(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
            }

            // Si hay un error se crea una respuesta con el mensaje del error
            if (error) {
                crearRespuestaError("No tiene permisos para acceder a este recurso", HttpServletResponse.SC_FORBIDDEN, response);
            }

        }

        // Si no hay errores se continúa con la petición
        if (!error) {
            filterChain.doFilter(request, response);
        }
    }

    private String getToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        return header != null && header.startsWith("Bearer ") ? header.replace("Bearer ", "") : null;
    }

    private void crearRespuestaError(String mensaje, int codigoError, HttpServletResponse response) throws IOException {
        MensajeDTO<String> dto = new MensajeDTO<>(true, mensaje);

        response.setContentType("application/json");
        response.setStatus(codigoError);
        response.getWriter().write(new ObjectMapper().writeValueAsString(dto));
        response.getWriter().flush();
        response.getWriter().close();
    }

    private boolean validarToken(String token, Rol rol) {
        boolean error = true;
        if (token != null) {
            Jws<Claims> jws = jwtUtils.parseJwt(token);
            if (jws.getPayload().get("rol").toString().equals(rol.toString())) {
                error = false;
            }
        }
        return error;
    }

}