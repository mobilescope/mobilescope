package com.mobilescope.database.auth;

/**
 * Created by IntelliJ IDEA.
 * User: Jude.Fernando
 * Date: 11/21/11
 * Time: 6:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class TournamentDetail {
       private double _prizePool;
       private int  _totalEntrants;
       private String _structure;
       private String _state;
       private double _stake;
       private double _rake;
       private String _network;
       private String _id;
       private String _game;
       private String _flags;
       private String _currency;
       private double _prize;
       private int _position;
       private String _playerName;


    public int get_totalEntrants() {
        return _totalEntrants;
    }

    public void set_totalEntrants(int _totalEntrants) {
        this._totalEntrants = _totalEntrants;
    }

    public double get_prizePool() {
        return _prizePool;
    }

    public void set_prizePool(double _prizePool) {
        this._prizePool = _prizePool;
    }

    public String get_structure() {
        return _structure;
    }

    public void set_structure(String _structure) {
        this._structure = _structure;
    }

    public String get_state() {
        return _state;
    }

    public void set_state(String _state) {
        this._state = _state;
    }

    public double get_stake() {
        return _stake;
    }

    public void set_stake(double _stake) {
        this._stake = _stake;
    }

    public double get_rake() {
        return _rake;
    }

    public void set_rake(double _rake) {
        this._rake = _rake;
    }

    public String get_network() {
        return _network;
    }

    public void set_network(String _network) {
        this._network = _network;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_game() {
        return _game;
    }

    public void set_game(String _game) {
        this._game = _game;
    }

    public String get_flags() {
        return _flags;
    }

    public void set_flags(String _flags) {
        this._flags = _flags;
    }

    public String get_currency() {
        return _currency;
    }

    public void set_currency(String _currency) {
        this._currency = _currency;
    }

    public double get_prize() {
        return _prize;
    }

    public void set_prize(double _prize) {
        this._prize = _prize;
    }

    public int get_position() {
        return _position;
    }

    public void set_position(int _position) {
        this._position = _position;
    }

    public String get_playerName() {
        return _playerName;
    }

    public void set_playerName(String _playerName) {
        this._playerName = _playerName;
    }
}
