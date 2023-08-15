package med.voll.api1.domain.medico;

import med.voll.api1.domain.direccion.DatosDireccion;

public record DatosRespuestaMedico(Long id, String nombre, String email,
                                   String telefono, String documento,
                                   Especialidad especialidad, DatosDireccion direccion) {
}
