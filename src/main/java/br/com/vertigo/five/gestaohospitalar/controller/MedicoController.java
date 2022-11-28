package br.com.vertigo.five.gestaohospitalar.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.vertigo.five.gestaohospitalar.api.ResourceNotFoundException;
import br.com.vertigo.five.gestaohospitalar.model.Atendimento;
import br.com.vertigo.five.gestaohospitalar.model.Medico;
import br.com.vertigo.five.gestaohospitalar.model.Paciente;
import br.com.vertigo.five.gestaohospitalar.repository.MedicoRepository;

@RestController
@RequestMapping("/medico")
public class MedicoController {
	@Autowired
	private MedicoRepository medicoRepository;
	
	@GetMapping
	public List<Medico> findAll(){
		return medicoRepository.findAll();
	}

	@GetMapping(path = {"/{id}"})
	public ResponseEntity<Medico> findById(@PathVariable Long id){
		return medicoRepository.findById(id)
		           .map(record -> ResponseEntity.ok().body(record))
		           .orElse(ResponseEntity.notFound().build());
		
	}
	
	@PostMapping("/cadastrar")
	public Medico createMedico(@RequestBody Medico medico) {
		return medicoRepository.save(medico);
	}
	
	@PutMapping("/update/{id}")
    public ResponseEntity<Medico> updateMedico(@PathVariable(value = "id") Long medicoId,
        @Validated @RequestBody Medico medico) throws ResourceNotFoundException {
		Medico m = medicoRepository.findById(medicoId)
				.orElseThrow(() -> new ResourceNotFoundException("Medico id: "+medicoId+" não encontrado."));

		m.setNome(medico.getNome());
		m.setCPF(medico.getCPF());
		m.setDataNasc(medico.getDataNasc());
		m.setCRM(medico.getCRM());
        final Medico updatedMedico = medicoRepository.save(m);
        return ResponseEntity.ok(updatedMedico);
    }

    /*@DeleteMapping("/delete/{id}")
    public Map<String, Boolean> deleteMedico(@PathVariable(value = "id") Long medicoId)
    throws ResourceNotFoundException{
        Medico medico = medicoRepository.findById(medicoId)
            .orElseThrow(() -> new ResourceNotFoundException("Medico not found for this id :: " + medicoId));

        List<Atendimento> atendimentos = new ArrayList<Atendimento>();
        
        Map < String, Boolean > response = new HashMap<>();
        
        for(Atendimento atend : atendimentos) {
        	if(atend.getMedico() == medico) {
        		response.put("Não é possível deletar um médico que já realizou uma atendimento.", Boolean.FALSE);
        		return response;
        	}
        }
        
        medicoRepository.delete(medico);
        response.put("deleted", Boolean.TRUE);
        return response;
    }*/
	
	@DeleteMapping("/delete/{id}")
	public String deleteMedico(@PathVariable(value = "id") Long medicoId) throws SQLIntegrityConstraintViolationException{
		List<Medico> medicos = medicoRepository.findAll();
		Medico medico = new Medico();
		
		for(Medico m : medicos) {
				if(m.getId() == medicoId) {
					medico = m;
				}
		}
		
		if(medico.getId()!=null){
			try {
				medicoRepository.delete(medico);
			    return "Medico id: "+medico.getId()+" excluido com sucesso!";
			    
			} catch (Exception e) {
				return "Não é possível deletar o medico id: "+medico.getId()+". Medicos que realizaram atendimentos não podem ser deletados.";
			}
		} else {
			return "Medico não encontrado.";
		}
	}
}
