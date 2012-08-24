package br.gov.ce.fortaleza.cti.sgf.bean;

import br.gov.ce.fortaleza.cti.sgf.util.SgfUtil;

public abstract class BaseStateBean extends BaseBean {

	private String currentState;

	public boolean isOpened() {
		Object tmp = SgfUtil.getSession().getAttribute("isOpen");
		if (tmp == null) {
			closePopup();
			return false;
		} else {
			return (Boolean) tmp;
		}
	}

	public boolean isOpenSession(){
		Object o = SgfUtil.getSession().getAttribute("sessionOpened");
		if(o == null){
			return false;
		} else {
			return (Boolean)o;
		}
	}

	public String openPopup() {
		SgfUtil.getSession().setAttribute("isOpen", Boolean.TRUE);
		return SUCCESS;
	}


	public String closePopup() {
		SgfUtil.getSession().setAttribute("isOpen", Boolean.FALSE);
		return SUCCESS;
	}
	
	public boolean isUserIn(){
		Object tmp = SgfUtil.getSession().getAttribute("userIn");
		if (tmp == null) {
			return false;
		} else {
			return (Boolean) tmp;
		}
	}

	protected String currentBeanName() {
		return this.getClass().getSimpleName();
	}

	protected void setCurrentBean(String bean) {
		SgfUtil.getSession().setAttribute("currentBean", bean);
	}

	protected void setCurrentState(String state) {
		this.currentState = state;
	}

	public String getCurrentBean() {
		return (String) SgfUtil.getSession().getAttribute("currentBean");
	}

	public String getCurrentState() {
		return this.currentState == null ? "" : this.currentState;
	}
}