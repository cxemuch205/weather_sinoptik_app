package ua.maker.sinopticua.structs;

public class ItemTown {
	
	private String nameTown;
	private String urlTown;
	private String urlEndTown;
	private String detailLocation;
	
	private int idInDB;
	
	public ItemTown(){};
	
	public ItemTown(String nameTown, String urlTown){
		this.nameTown = nameTown;
		this.urlTown = urlTown;
	}
	
	public String getNameTown() {
		return nameTown;
	}
	public void setNameTown(String nameTown) {
		this.nameTown = nameTown;
	}
	public String getUrlTown() {
		return urlTown;
	}
	public void setUrlTown(String urlTown) {
		this.urlTown = urlTown;
	}

	public int getIdInDB() {
		return idInDB;
	}

	public void setIdInDB(int idInDB) {
		this.idInDB = idInDB;
	}

	public String getUrlEndTown() {
		return urlEndTown;
	}

	public void setUrlEndTown(String urlEndTown) {
		this.urlEndTown = urlEndTown;
	}

	public String getDetailLocation() {
		return detailLocation;
	}

	public void setDetailLocation(String detailLocation) {
		this.detailLocation = detailLocation;
	}
}
