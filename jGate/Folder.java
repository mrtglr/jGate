package jGate;

public class Folder {
	
	public String _name, _path;
	public boolean _locked;

	public Folder (String name, String path, boolean locked) {
		this._name = name;
		this._path = path;
		this._locked = locked;
	}
	
	public String toString() {
		return this._name + " " + this._path + " " + this._locked;
	}
}
