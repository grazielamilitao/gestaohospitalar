package br.com.vertigo.five.gestaohospitalar.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.vertigo.five.gestaohospitalar.model.Atendimento;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long>{
	List<Atendimento> findAll();
	
	//@Query(value = "SELECT * FROM atendimento a WHERE a.data_atendimento>=dateInicio && a.data_atendimento<=dateFim")
    //List<Atendimento> findPeriod(Date dateInicio, Date dateFim);
}