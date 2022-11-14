package br.com.vertigo.five.gestaohospitalar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.vertigo.five.gestaohospitalar.model.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>{

	//@Query("select p from Paciente p where p.id = :id")
	Optional<Paciente> findById(Long id);
	List<Paciente> findAll();
	
}
