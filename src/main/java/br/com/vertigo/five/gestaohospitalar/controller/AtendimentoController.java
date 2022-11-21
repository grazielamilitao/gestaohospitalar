package br.com.vertigo.five.gestaohospitalar.controller;

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

import br.com.vertigo.five.gestaohospitalar.api.ResourceNotFoundException;
import br.com.vertigo.five.gestaohospitalar.model.Atendimento;
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
	
	@GetMapping(path = {"/{inicio}/{fim}"})
	public ResponseEntity<Atendimento> findByPeriodo(@PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") Date inicio, Date fim) {
		List<Atendimento> atends = atendimentoRepository.findAll();
		List<Atendimento> atendsPeriodo = null;
		
		for(Atendimento atendimento : atends) {
			if((inicio.after(atendimento.getDataAtendimento()) && fim.before(atendimento.getDataAtendimento())) 
					|| inicio.equals(atendimento.getDataAtendimento())
					|| fim.equals(atendimento.getDataAtendimento()) ) {
				
				atendsPeriodo.add(atendimento);
			}
		}
		
		return (ResponseEntity<Atendimento>) atendsPeriodo;
	}
	
	@Transactional
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
