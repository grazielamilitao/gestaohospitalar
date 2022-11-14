package br.com.vertigo.five.gestaohospitalar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.vertigo.five.gestaohospitalar.model.Medico;
@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long>{

	Optional<Medico> findById(Long id);
	List<Medico> findAll();

}
