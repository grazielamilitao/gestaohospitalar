package br.com.vertigo.five.gestaohospitalar.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Paciente {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;
	
	private String CPF;
	
	private Date dataNasc;
	
	private String sexo;

	public String getNome() {
		return nome;
	}
	
	public Paciente() {

	}

	
	public Paciente(String nome, String CPF, Date dataNasc, String sexo) {
		this.nome = nome;
		this.CPF = CPF;
		this.dataNasc = dataNasc;
		this.sexo = sexo;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCPF() {
		return CPF;
	}

	public void setCPF(String cPF) {
		CPF = cPF;
	}

	public Date getDataNasc() {
		return dataNasc;
	}

	public void setDataNasc(Date dataNasc) {
		this.dataNasc = dataNasc;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	@Override
	public String toString(){
		return ""+getNome()+" "+getCPF()+" "+getDataNasc()+" "+getSexo();
	}
}
