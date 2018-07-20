package hu.gaborbalazs.enums;

public enum Restaurant {

	DEZSOBA("dezsoba", "Dezső bá"), TENMINUTES("10minutes", "10 minutes"), DAGOBA("dagoba", "Dagoba");

	private String id;
	private String name;

	private Restaurant(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
