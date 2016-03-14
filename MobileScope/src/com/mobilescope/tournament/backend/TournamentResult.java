package com.mobilescope.tournament.backend;

public class TournamentResult {
	public TournamentResult(String _id, String _date, String _network,
			String _stake) {
		super();
		this._id = _id;
		this._date = _date;
		this._network = _network;
		this._stake = _stake;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String get_date() {
		return _date;
	}
	public void set_date(String _date) {
		this._date = _date;
	}
	public String get_network() {
		return _network;
	}
	public void set_network(String _network) {
		this._network = _network;
	}
	public String get_stake() {
		return _stake;
	}
	public void set_stake(String _stake) {
		this._stake = _stake;
	}
	String _id;
	String _date;
	String _network;
	String _stake;
}
