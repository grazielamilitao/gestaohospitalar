package br.com.vertigo.five.gestaohospitalar.controller;


import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.vertigo.five.gestaohospitalar.api.ResourceNotFoundException;
import br.com.vertigo.five.gestaohospitalar.model.Paciente;
import br.com.vertigo.five.gestaohospitalar.repository.PacienteRepository;

@RestController
@RequestMapping("/paciente")
public class PacienteController {
	@Autowired
	private PacienteRepository pacienteRepository;
	
	@GetMapping
	public List<Paciente> findAll(){
		return pacienteRepository.findAll();
	}

	@GetMapping(path = {"/{id}"})
	public ResponseEntity<Paciente> findById(@PathVariable Long id){
		return pacienteRepository.findById(id)
		           .map(record -> ResponseEntity.ok().body(record))
		           .orElse(ResponseEntity.notFound().build());
		
	}
	
	@PostMapping("/cadastrar")
	public Paciente createPaciente(@RequestBody Paciente paciente) {
		return pacienteRepository.save(paciente);
	}
	
	@PutMapping("/update/{id}")
    public ResponseEntity<Paciente> updatePaciente(@PathVariable(value = "id") Long pacienteId,
        @Validated @RequestBody Paciente paciente) throws ResourceNotFoundException {
		Paciente p = pacienteRepository.findById(pacienteId)
				.orElseThrow(() -> new ResourceNotFoundException("Paciente not found for this id: " + pacienteId));

		p.setNome(paciente.getNome());
		p.setCPF(paciente.getCPF());
		p.setDataNasc(paciente.getDataNasc());
		p.setSexo(paciente.getSexo());
        final Paciente updatedṔaciente = pacienteRepository.save(p);
        return ResponseEntity.ok(updatedṔaciente);
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> deletePaciente(@PathVariable(value = "id") Long pacienteId)
    throws ResourceNotFoundException{
        Paciente paciente = pacienteRepository.findById(pacienteId)
            .orElseThrow(() -> new ResourceNotFoundException("Paciente not found for this id: " + pacienteId));

        pacienteRepository.delete(paciente);
        Map < String, Boolean > response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
