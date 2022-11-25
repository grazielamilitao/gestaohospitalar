package br.com.vertigo.five.gestaohospitalar.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.vertigo.five.gestaohospitalar.api.ResourceNotFoundException;
import br.com.vertigo.five.gestaohospitalar.model.Atendimento;
import br.com.vertigo.five.gestaohospitalar.model.Medico;
import br.com.vertigo.five.gestaohospitalar.model.Paciente;
import br.com.vertigo.five.gestaohospitalar.relatorio.Periodo;
import br.com.vertigo.five.gestaohospitalar.repository.AtendimentoRepository;

@RestController
@RequestMapping("/atendimento")
public class AtendimentoController {
	
	@Autowired
	private AtendimentoRepository atendimentoRepository;
	
	@GetMapping
	public List<Atendimento> findAll(){
		return atendimentoRepository.findAll();
	}

	@GetMapping(path = {"/{id}"})
	public ResponseEntity<Atendimento> findById(@PathVariable Long id){
		return atendimentoRepository.findById(id)
		           .map(record -> ResponseEntity.ok().body(record))
		           .orElse(ResponseEntity.notFound().build());
	}
	
	@RequestMapping(path = "/relatorio/periodo", method = RequestMethod.GET)
	public List<Atendimento> findPeriodo(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.ms") Date dateInicio, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.ms") Date dateFim){
		List<Atendimento> atendPeriodo = new ArrayList<Atendimento>();
		List<Atendimento> atends = atendimentoRepository.findAll();
	
		for(Atendimento atendimento : atends) {
			if(atendimento.getDataAtendimento().equals(dateInicio) || 
					atendimento.getDataAtendimento().equals(dateFim) ||
					(atendimento.getDataAtendimento().after(dateInicio) && atendimento.getDataAtendimento().before(dateFim) )) {
				atendPeriodo.add(atendimento);
			}
		}
		return atendPeriodo;
	}
	
	@RequestMapping(path = "/relatorio/pacientespormedico", method = RequestMethod.GET)
	public List<Paciente> findPeriodo(@RequestParam String crmMedico){
		List<Paciente> pacientesMedico = new ArrayList<Paciente>();
		List<Atendimento> atends = atendimentoRepository.findAll();
		
		for(Atendimento atendimento : atends) {
			System.out.println("a"+atendimento.getMedico().getCRM());
			if(atendimento.getMedico().getCRM().equals(crmMedico)) {
				pacientesMedico.add(atendimento.getPaciente());
			}
		}
		
		return pacientesMedico;
	}
	
	@PostMapping("/cadastrar")
	public Atendimento createAtendimento(@RequestBody Atendimento atendimento) {
		return atendimentoRepository.save(atendimento);
	}
	
	@PutMapping("/update/{id}")
    public ResponseEntity<Atendimento> updateAtendimento(@PathVariable(value = "id") Long atendId,
        @Validated @RequestBody Atendimento atendimento) throws ResourceNotFoundException {
		Atendimento atend = atendimentoRepository.findById(atendId)
				.orElseThrow(() -> new ResourceNotFoundException("Atendimento not found for this id: " + atendId));

		atend.setMedico(atendimento.getMedico());
		atend.setPaciente(atendimento.getPaciente());
		atend.setObservacoes(atendimento.getObservacoes());
		atend.setStatus(atendimento.getStatus());
        final Atendimento updatedAtendimento = atendimentoRepository.save(atend);
        return ResponseEntity.ok(updatedAtendimento);
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> deleteAtendimento(@PathVariable(value = "id") Long atendId)
    throws ResourceNotFoundException{
    	Atendimento atend = atendimentoRepository.findById(atendId)
            .orElseThrow(() -> new ResourceNotFoundException("Atendimento not found for this id :: " + atendId));

    	atendimentoRepository.delete(atend);
        Map < String, Boolean > response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
