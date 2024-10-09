package co.edu.uniquindio.proyecto.modelo.dto.ValidacionDTO;

public record ValidacionDTO(String field, String defaultMessage) {
    public ValidacionDTO(String field, String defaultMessage) {
        this.field = field;
        this.defaultMessage = defaultMessage;
    }
}
