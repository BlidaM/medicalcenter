package med.voll.api1.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api1.domain.direccion.DatosDireccion;
import med.voll.api1.domain.medico.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired  // no es recomendable para hacer pruebas unitarias
    private MedicoRepository medicoRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaMedico> registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico,
                                          UriComponentsBuilder uriComponentsBuilder){

        Medico medico=medicoRepository.save(new Medico(datosRegistroMedico));
        DatosRespuestaMedico datosRespuestaMedico=new DatosRespuestaMedico(medico.getId(),medico.getNombre(),medico.getEmail(),
                medico.getTelefono(), medico.getDocumento(), medico.getEspecialidad(),
                new DatosDireccion(medico.getDireccion().getCalle(),medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(), medico.getDireccion().getNumero(),
                        medico.getDireccion().getComplemento()));

        URI url=uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaMedico);

        // REtunr 201 created
        // URL donde encontrar al medico
        //http://localhost:8080/medicos/xx--> hacer para cunplir buenas practicas
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoMedico>> listadoMedicos(@PageableDefault(size=2) Pageable paginacion) {
        //return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new);
        return ResponseEntity.ok(medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedico::new));
    }

    @PutMapping
    @Transactional
    public ResponseEntity actualizarMedico(@RequestBody @Valid DatosActualizarMedico datosActualizarMedico){

        Medico medico=medicoRepository.getReferenceById(datosActualizarMedico.id());
        medico.actualizarDatos(datosActualizarMedico);
        return ResponseEntity.ok(new DatosRespuestaMedico(medico.getId(),medico.getNombre(),medico.getEmail(),
                medico.getTelefono(), medico.getDocumento(), medico.getEspecialidad(),
                new DatosDireccion(medico.getDireccion().getCalle(),medico.getDireccion().getDistrito(),
                medico.getDireccion().getCiudad(), medico.getDireccion().getNumero(),
                medico.getDireccion().getComplemento())));
    }

        //DELETE LÓGICO
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarMedico(@PathVariable Long id){
        Medico medico=medicoRepository.getReferenceById(id);
        medico.desactivarMedico();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaMedico> retornaDatosMedico(@PathVariable Long id){

        Medico medico=medicoRepository.getReferenceById(id);
        var datosMedicos=new DatosRespuestaMedico(medico.getId(),medico.getNombre(),medico.getEmail(),
                medico.getTelefono(), medico.getDocumento(), medico.getEspecialidad(),
                new DatosDireccion(medico.getDireccion().getCalle(),medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(), medico.getDireccion().getNumero(),
                        medico.getDireccion().getComplemento()));
        return ResponseEntity.ok(datosMedicos);
    }





//DELETEE EN BASE DE DATOS
  /*  public void eliminarMedico(@PathVariable Long id){
        Medico medico=medicoRepository.getReferenceById(id);
        medicoRepository.delete(medico);
    }*/

}


