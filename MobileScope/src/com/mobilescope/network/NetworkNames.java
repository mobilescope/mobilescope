package com.mobilescope.network;

public class NetworkNames {
	String _parent;
	String[] _child;
	public String get_parent() {
		return _parent;
	}
	public void set_parent(String _parent) {
		this._parent = _parent;
	}
	public String[] get_child() {
		return _child;
	}
	public void set_child(String[] _child) {
		this._child = _child;
	}
	
	public NetworkNames(String _parent, String[] _child) {
		
		this._parent = _parent;
		this._child = _child;
	}

	
}
