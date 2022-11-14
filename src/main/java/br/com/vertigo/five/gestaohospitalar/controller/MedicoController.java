package br.com.vertigo.five.gestaohospitalar.controller;

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
import br.com.vertigo.five.gestaohospitalar.model.Medico;
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
	public Medico createPaciente(@RequestBody Medico medico) {
		return medicoRepository.save(medico);
	}
	
	@PutMapping("/update/{id}")
    public ResponseEntity<Medico> updatePaciente(@PathVariable(value = "id") Long medicoId,
        @Validated @RequestBody Medico medico) throws ResourceNotFoundException {
		Medico m = medicoRepository.findById(medicoId)
				.orElseThrow(() -> new ResourceNotFoundException("Medico not found for this id: " + medicoId));

		m.setNome(medico.getNome());
		m.setCPF(medico.getCPF());
		m.setDataNasc(medico.getDataNasc());
		m.setCRM(medico.getCRM());
        final Medico updatedMedico = medicoRepository.save(m);
        return ResponseEntity.ok(updatedMedico);
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> deleteMedico(@PathVariable(value = "id") Long medicoId)
    throws ResourceNotFoundException{
        Medico medico = medicoRepository.findById(medicoId)
            .orElseThrow(() -> new ResourceNotFoundException("Medico not found for this id :: " + medicoId));

        medicoRepository.delete(medico);
        Map < String, Boolean > response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
