package br.com.vertigo.five.gestaohospitalar.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.vertigo.five.gestaohospitalar.model.Atendimento;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long>{
	List<Atendimento> findAll();
}
