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
import br.com.vertigo.five.gestaohospitalar.repository.AtendimentoRepository;

@RestController
@RequestMapping("/atendimento")
public class AtendimentoController {
	
	@Autowired
	private AtendimentoRepository atendimentoRepository;
	
	//listar todos os atendimentos
	@GetMapping
	public List<Atendimento> findAll(){
		return atendimentoRepository.findAll();
	}
	
	//selecionar um atendimento colocando o id na url
	@GetMapping(path = {"/{id}"})
	public ResponseEntity<Atendimento> findById(@PathVariable Long id){
		return atendimentoRepository.findById(id)
		           .map(record -> ResponseEntity.ok().body(record))
		           .orElse(ResponseEntity.notFound().build());
	}
	
	//cadastrar atendimento
	@PostMapping("/cadastrar")
	public Atendimento createAtendimento(@RequestBody Atendimento atendimento) {
		return atendimentoRepository.save(atendimento);
	}
	
	//alterar atendimento
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

	//deletar atendimento
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

	//Listar os atendimentos entre um período de datas
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
	
	//Listar médicos que trabalharam em um período de datas
	@RequestMapping(path = "/relatorio/medicos/periodo", method = RequestMethod.GET)
	public List<Medico> findMedicosPorPeriodo(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.ms") Date dateInicio, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.ms") Date dateFim){
		List<Medico> medPeriodo = new ArrayList<Medico>();
		List<Atendimento> atends = atendimentoRepository.findAll();
		
		for(Atendimento atendimento : atends) {
			if(atendimento.getDataAtendimento().equals(dateInicio) || 
					atendimento.getDataAtendimento().equals(dateFim) ||
					(atendimento.getDataAtendimento().after(dateInicio) && atendimento.getDataAtendimento().before(dateFim) )) {
				medPeriodo.add(atendimento.getMedico());
			}
		}
		
		return medPeriodo;
	}
	
	//Listar os pacientes de um determinado médico
	@RequestMapping(path = "/relatorio/pacientespormedico", method = RequestMethod.GET)
	public List<Paciente> findPacientesPorMedico(@RequestParam String crmMedico){
		List<Paciente> pacientesMedico = new ArrayList<Paciente>();
		List<Atendimento> atends = atendimentoRepository.findAll();
		
		for(Atendimento atendimento : atends) {
			if(atendimento.getMedico().getCRM().equals(crmMedico)) {
				pacientesMedico.add(atendimento.getPaciente());
			}
		}
		
		return pacientesMedico;
	}
	
	//Listar todos médicos que atenderam um determinado paciente;
	@RequestMapping(path = "/relatorio/medicosporpaciente", method = RequestMethod.GET)
	public List<Medico> findMedicosPorPaciente(@RequestParam String cpfPaciente){
		List<Medico> medicosPaciente = new ArrayList<Medico>();
		List<Atendimento> atends = atendimentoRepository.findAll();
		
		for(Atendimento atendimento : atends) {
			if(atendimento.getPaciente().getCPF().equals(cpfPaciente)) {
				medicosPaciente.add(atendimento.getMedico());
			}
		}
		
		return medicosPaciente;
	}
	
}
