package med.voll.api1.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api1.domain.paciente.DatosListaPaciente;
import med.voll.api1.domain.paciente.DatosRegistroPaciente;
import med.voll.api1.domain.paciente.Paciente;
import med.voll.api1.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;
    @PostMapping
    @Transactional
    public void registrar(@RequestBody @Valid DatosRegistroPaciente datosRegistroPaciente){
        repository.save(new Paciente(datosRegistroPaciente));
    }

    @GetMapping
    public Page<DatosListaPaciente> listar(@PageableDefault(page = 0, size = 10, sort = {"nombre"}) Pageable paginacion) {
        return repository.findAll(paginacion).map(DatosListaPaciente::new);
    }


}
