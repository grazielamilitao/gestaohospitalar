package br.com.vertigo.five.gestaohospitalar.controller;


import java.net.URI;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.vertigo.five.gestaohospitalar.api.ResourceNotFoundException;
import br.com.vertigo.five.gestaohospitalar.model.Atendimento;
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
				.orElseThrow(() -> new ResourceNotFoundException("Paciente id: "+pacienteId+" não encontrado."));

		p.setNome(paciente.getNome());
		p.setCPF(paciente.getCPF());
		p.setDataNasc(paciente.getDataNasc());
		p.setSexo(paciente.getSexo());
        final Paciente updatedṔaciente = pacienteRepository.save(p);
        return ResponseEntity.ok(updatedṔaciente);
    }

   /* @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> deletePaciente(@PathVariable(value = "id") Long pacienteId)
    throws ResourceNotFoundException{
        Paciente paciente = pacienteRepository.findById(pacienteId)
            .orElseThrow(() -> new ResourceNotFoundException("Paciente not found for this id: " + pacienteId));

        List<Atendimento> atendimentos = new ArrayList<Atendimento>();
        Map < String, Boolean > response = new HashMap<>();
        
        for(Atendimento atend : atendimentos) {
        	if(atend.getPaciente() == paciente) {
        		response.put("Não é possível deletar um paciente que possui um atendimento.", Boolean.FALSE);
        		return response;
        	}
        }
        System.out.println("não");
        pacienteRepository.delete(paciente);
        response.put("deleted", Boolean.TRUE);
        return response;
    }*/
    
    @DeleteMapping("/delete/{id}")
	public String deletePaciente(@PathVariable(value = "id") Long pacienteId) throws SQLIntegrityConstraintViolationException{
		List<Paciente> pacientes = pacienteRepository.findAll();
		Paciente paciente = new Paciente();
		
		for(Paciente p : pacientes) {
				if(p.getId() == pacienteId) {
					paciente = p;
				}
		}
		
		if(paciente.getId()!=null){
			try {
				pacienteRepository.delete(paciente);
			    return "Paciente id: "+paciente.getId()+" excluido com sucesso!";
			    
			} catch (Exception e) {
				return "Não é possível deletar o paciente id: "+paciente.getId()+". Pacientes que possuem atendimentos não podem ser deletados.";
			}
		} else {
			return "Paciente não encontrado.";
		}
	}
}
