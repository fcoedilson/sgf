package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TB_LOGUSUARIO", schema = "SGF")
@NamedQueries({
	@NamedQuery(name = "LogUsuario.findByUsuarioAndData", query = "SELECT ul FROM LogUsuario ul WHERE ul.usuario.id = ? AND ul.dataLogin BETWEEN ? AND ?"),
	@NamedQuery(name = "LogUsuario.findUltimoLogin", query = "SELECT ul FROM LogUsuario ul WHERE ul.usuario.id = ? and ul.dataLogin = (SELECT max(u.dataLogin) FROM LogUsuario u WHERE u.usuario.id = ?)")
})
public class LogUsuario implements Serializable {

	private static final long serialVersionUID = -2849037649279176622L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID", nullable = false)
	private Integer id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_LOGIN", nullable = true)
	private Date dataLogin;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_LOGOUT", nullable = true)
	private Date dataLogout;
	
	@Column(name="IP_CON")
	private String ipcon;

	@ManyToOne
	@JoinColumn(name="ID_USUARIO", nullable = false)
	private User usuario;

	@Column(name="OBSERVACAO", nullable = true, length = 250)
	private String observacao;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDataLogin() {
		return dataLogin;
	}

	public void setDataLogin(Date dataLogin) {
		this.dataLogin = dataLogin;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}
	
	public Date getDataLogout() {
		return dataLogout;
	}

	public void setDataLogout(Date dataLogout) {
		this.dataLogout = dataLogout;
	}

	public String getIpcon() {
		return ipcon;
	}

	public void setIpcon(String ipcon) {
		this.ipcon = ipcon;
	}

	public int hashCode() {
		int result = 1;
		result = 31 * result + ((id == null) ? 0 : id.hashCode());
		result = 31 * result + ((dataLogin == null) ? 0 : dataLogin.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogUsuario other = (LogUsuario) obj;
		return ((id == null && other.id == null) || (id != null && id.equals(other.id))) &&
			((dataLogin == null && other.dataLogin == null) || (dataLogin != null && dataLogin.equals(other.dataLogin)));
	}
}