package br.com.vertigo.five.gestaohospitalar.controller;


import java.io.IOException;
import java.net.URI;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.lowagie.text.DocumentException;

import br.com.vertigo.five.gestaohospitalar.api.ResourceNotFoundException;
import br.com.vertigo.five.gestaohospitalar.model.Atendimento;
import br.com.vertigo.five.gestaohospitalar.model.Paciente;
import br.com.vertigo.five.gestaohospitalar.pdf.PacientePDF;
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
				.orElseThrow(() -> new ResourceNotFoundException("Paciente id: "+pacienteId+" n??o encontrado."));

		p.setNome(paciente.getNome());
		p.setCPF(paciente.getCPF());
		p.setDataNasc(paciente.getDataNasc());
		p.setSexo(paciente.getSexo());
        final Paciente updated???aciente = pacienteRepository.save(p);
        return ResponseEntity.ok(updated???aciente);
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
        		response.put("N??o ?? poss??vel deletar um paciente que possui um atendimento.", Boolean.FALSE);
        		return response;
        	}
        }
        System.out.println("n??o");
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
				return "N??o ?? poss??vel deletar o paciente id: "+paciente.getId()+". Pacientes que possuem atendimentos n??o podem ser deletados.";
			}
		} else {
			return "Paciente n??o encontrado.";
		}
	}
    
    @RequestMapping(value="/getpdf", method=RequestMethod.GET)
    public ResponseEntity<List<Paciente>> getPDF() {
        List<Paciente> p = new ArrayList<Paciente>();
        p = pacienteRepository.findAll();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        ResponseEntity<List<Paciente>> response = new ResponseEntity<>(p, headers, HttpStatus.OK);
        return response;
    }
    
    @GetMapping("/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
         
        List<Paciente> listPacientes = findAll();
         
        PacientePDF exporter = new PacientePDF(listPacientes);
        exporter.export(response);
         
    }

}
