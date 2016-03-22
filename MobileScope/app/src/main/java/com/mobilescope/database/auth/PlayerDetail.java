package com.mobilescope.database.auth;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Jude.Fernando
 * Date: 11/21/11
 * Time: 6:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerDetail {
    private String _network;
    private String _displayCurrency;
    private String _count;
    private String _avProfit;
    private String _avStake;
    private String _avROI;
    private String _Profit;
    private String _stake;
    private String _Cashes;
    private String _rake;
    private String _totalROI;
    private String _itm;
    private String _ability;
    private String _maxWinningStreak;
    private String _maxLosingStreak;
    private String _WinningDays;
    private String _LosingDays;
    private String _RemainingSearches;
    public Number[] _lCumulativeProfitSeries;
    public Number[] _lCumulativeProfitGrossSeries;
    public Number[] _lxValue;
    private ArrayList<RecentTournament> _recentTournaments;
    private boolean _isOptValue;
    
    public void set_count(String _count){
        this._count=_count;
    }

    public String get_count(){
        return  this._count;
    }

    public void set_network(String _network){
        this._network=_network;
    }

    public String get_network(){
        return  this._network;
    }

     public void set_displayCurrency(String _displayCurrency){
        this._displayCurrency=_displayCurrency;
    }

    public String get_displayCurrency(){
        return  this._displayCurrency;
    }

    public String get_avProfit() {
        return _avProfit;
    }

    public void set_avProfit(String _avProfit) {
        this._avProfit = _avProfit;
    }

    public String get_avROI() {
        return _avROI;
    }

    public void set_avROI(String _avROI) {
        this._avROI = _avROI;
    }

    public String get_avStake() {
        return _avStake;
    }

    public void set_avStake(String _avStake) {
        this._avStake = _avStake;
    }

    public String get_Cashes() {
        return _Cashes;
    }

    public void set_Cashes(String _Cashes) {
        this._Cashes = _Cashes;
    }

    public String get_stake() {
        return _stake;
    }

    public void set_stake(String _stake) {
        this._stake = _stake;
    }

    public String get_Profit() {
        return _Profit;
    }

    public void set_Profit(String _Profit) {
        this._Profit = _Profit;
    }

    public String get_rake() {
        return _rake;
    }

    public void set_rake(String _rake) {
        this._rake = _rake;
    }

    public String get_totalROI() {
        return _totalROI;
    }

    public void set_totalROI(String _totalROI) {
        this._totalROI = _totalROI;
    }

    public String get_itm() {
        return _itm;
    }

    public void set_itm(String _itm) {
        this._itm = _itm;
    }

    public String get_ability() {
        return _ability;
    }

    public void set_ability(String _ability) {
        this._ability = _ability;
    }

    public String get_maxWinningStreak() {
        return _maxWinningStreak;
    }

    public void set_maxWinningStreak(String _maxWinningStreak) {
        this._maxWinningStreak = _maxWinningStreak;
    }

    public String get_maxLosingStreak() {
        return _maxLosingStreak;
    }

    public void set_maxLosingStreak(String _maxLosingStreak) {
        this._maxLosingStreak = _maxLosingStreak;
    }

    public String get_WinningDays() {
        return _WinningDays;
    }

    public void set_WinningDays(String _WinningDays) {
        this._WinningDays = _WinningDays;
    }

    public String get_LosingDays() {
        return _LosingDays;
    }

    public void set_LosingDays(String _LosingDays) {
        this._LosingDays = _LosingDays;
    }

    public Number[] get_lCumulativeProfitGrossSeries() {
        return _lCumulativeProfitGrossSeries;
    }

    public void set_lCumulativeProfitGrossSeries(Number[] _lCumulativeProfitGrossSeries) {
        this._lCumulativeProfitGrossSeries = _lCumulativeProfitGrossSeries;
    }

    public Number[] get_lCumulativeProfitSeries() {
        return _lCumulativeProfitSeries;
    }

    public void set_lCumulativeProfitSeries(Number[] _lCumulativeProfitSeries) {
        this._lCumulativeProfitSeries = _lCumulativeProfitSeries;
    }

    public Number[] get_lxValue() {
        return _lxValue;
    }

    public void set_lxValue(Number[] _lxValue) {
        this._lxValue = _lxValue;

    }

    public void set_recentTournaments(int capacity){
       this._recentTournaments=new ArrayList<RecentTournament>(capacity);
    }

    public  void add_recentTournament(RecentTournament rd){
        this._recentTournaments.add(rd);
    }

    public ArrayList<RecentTournament> get_recentTournaments(){
        return this._recentTournaments;
    }

    public RecentTournament get_recentTournaments(int position){
        return this._recentTournaments.get(position);
    }

	public void set_RemainingSearches(String _RemainingSearches) {
		this._RemainingSearches = _RemainingSearches;
	}

	public String get_RemainingSearches() {
		return _RemainingSearches;
	}

	/**
	 * @return the _isOptValue
	 */
	public boolean get_isOptValue() {
		return _isOptValue;
	}

	/**
	 * @param _isOptValue the _isOptValue to set
	 */
	public void set_isOptValue(boolean _isOptValue) {
		this._isOptValue = _isOptValue;
	}
}
