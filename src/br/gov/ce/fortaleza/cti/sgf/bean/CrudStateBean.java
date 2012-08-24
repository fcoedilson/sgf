package br.gov.ce.fortaleza.cti.sgf.bean;

public class CrudStateBean extends BaseStateBean {

	public static final String SEARCH = "SEARCH";
	public static final String SAVE = "SAVE";
	public static final String UPDATE = "UPDATE";
	public static final String DELETE = "DELETE";
	public static final String VIEW = "VIEW";
	public static final String IMPORT  = "IMPORT";
	public static final String EDIT = "EDIT";
	public static final String AREA_VEICULOS = "AREA_VEICULOS";

	public boolean isSearchState() {
		return SEARCH.equals(getCurrentState());
	}

	public boolean isSaveState() {
		return SAVE.equals(getCurrentState());
	}

	public boolean isUpdateState() {
		return UPDATE.equals(getCurrentState());
	}

	public boolean isDeleteState() {
		return DELETE.equals(getCurrentState());
	}

	public boolean isViewState() {
		return VIEW.equals(getCurrentState());
	}

	public boolean isImportState(){
		return IMPORT.equals(getCurrentState());
	}

	public boolean isEditState(){
		return EDIT.equals(getCurrentState());
	}

	public String prepareSave() {
		setCurrentBean(currentBeanName());
		setCurrentState(SAVE);
		return SUCCESS;
	}

	public String prepareImportSave() {
		setCurrentBean(currentBeanName());
		setCurrentState(IMPORT);
		return SUCCESS;
	}

	public String prepareUpdate() {
		setCurrentBean(currentBeanName());
		setCurrentState(UPDATE);
		return SUCCESS;
	}

	public String prepareDelete() {
		setCurrentBean(currentBeanName());
		setCurrentState(DELETE);
		return SUCCESS;
	}

	public String prepareView(){
		setCurrentBean(currentBeanName());
		setCurrentState(VIEW);
		return SUCCESS;
	}

	public String prepareEdit(){
		setCurrentBean(currentBeanName());
		setCurrentState(EDIT);
		return SUCCESS;
	}

	public String view(){
		setCurrentBean(currentBeanName());
		return search();
	}

	public String edit(){
		setCurrentBean(currentBeanName());
		return search();
	}

	public String save() {
		setCurrentBean(currentBeanName());
		return search();
	}

	public String update() {
		setCurrentBean(currentBeanName());
		return search();
	}

	public String delete() {
		setCurrentBean(currentBeanName());
		return search();
	}

	public String search() {
		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}

	public String searchSort() {
		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}

	public String searchStatus() {
		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}
}