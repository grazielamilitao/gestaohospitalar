package br.com.vertigo.five.gestaohospitalar.relatorio;

import java.util.Date;

public class Periodo {
	private Date dateInicio;
	private Date dateFim;

	public Date getDateInicio() {
		return dateInicio;
	}
	public void setDateInicio(Date dateInicio) {
		this.dateInicio = dateInicio;
	}
	public Date getDateFim() {
		return dateFim;
	}
	public void setDateFim(Date dateFim) {
		this.dateFim = dateFim;
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
