package com.stepcounter.data;

public class ReportModel {
	
	String sno;
    String id;
	String level;
    String rounds;
    String date;
    String datesep;
    String hold;
    String exhale;
    String duration;
    String type;
    String data;
    String habit_name;
    String custom_habit_status;
    String color;
    int type1,hr,min,total_steps,max_steps,stts_yoga;
    Double total_level;

    int imageId;
    String title_pranayama;
    String subtitle_pranayama;
    int position;

    String yogasana_name,pranayama_name;
    int total_time,do_time;
    int delay_type,delay_time;

    public ReportModel(){

    }

    public ReportModel(int imageId, String title_pranayama, String subtitle_pranayama, int position) {
        this.imageId = imageId;
        this.title_pranayama = title_pranayama;
        this.subtitle_pranayama = subtitle_pranayama;
        this.position = position;
    }

    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getRounds() {
        return rounds;
    }
    public void setRounds(String rounds) {
        this.rounds = rounds;
    }
    public void setHold(String hold) {
        this.hold = hold;
    }
    public void setExhale(String exhale) {
        this.exhale = exhale;
    }
    public String getDate() {

        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getDatesep() {

        return datesep;
    }
    public void setDatesep(String datesep) {
        this.datesep = datesep;
    }
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
    public String getSno() {
        return sno;
    }
    public void setSno(String sno) {
        this.sno = sno;
    }
    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public void setData(String data){
        this.data=data;
    }
    public String getData(){
        return data;
    }
    public void setColor(String color){
        this.color=color;
    }
    public String getColor(){
        return color;
    }
    
    public void setType1(int type1){
        this.type1=type1;
    }
    public int getType1(){
        return type1;
    }

    public void setTotal_level(double total_level){
        this.total_level=total_level;
    }
    public double getTotal_level(){
        return total_level;
    }

    public String getHabit_name() {
        return habit_name;
    }
    public void setHabit_name(String habit_name) {
        this.habit_name = habit_name;
    }


    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public String getSubTitle() {
        return subtitle_pranayama;
    }
    public void setSubTitle(String subtitle_pranayama) {
        this.subtitle_pranayama = subtitle_pranayama;
    }
    public String getTitle() {
        return title_pranayama;
    }
    public void setTitle(String title_pranayama) {
        this.title_pranayama = title_pranayama;
    }
    public int getPosition(){
        return position;
    }
    public void setPosition(int position){
        this.position = position;
    }

    public String getYogasana_name(){
        return yogasana_name;
    }
    public void setYogasana_name(String yogasana_name){
        this.yogasana_name=yogasana_name;
    }

    public String getPranayama_name(){
        return pranayama_name;
    }
    public void setPranayama_name(String pranayama_name){
        this.pranayama_name=pranayama_name;
    }
    @Override
    public String toString() {
        return position+ "";
    }

    public String getCustom_habit_status() {
        return custom_habit_status;
    }
    public void setCustom_habit_status(String custom_habit_status) {
        this.custom_habit_status = custom_habit_status;
    }

    public void setHr(int hr){
        this.hr=hr;
    }
    public int getHr(){
        return hr;
    }

    public void setMin(int min){
        this.min=min;
    }
    public int getMin(){return min;
    }

    public int getTotal_time() {
        return total_time;
    }
    public void setTotal_time(int total_time) {
        this.total_time = total_time;
    }

    public int getDo_time() {
        return do_time;
    }
    public void setDo_time(int do_time) {
        this.do_time = do_time;
    }

    public int getDelay_type() {
        return delay_type;
    }
    public void setDelay_type(int delay_type) {
        this.delay_type = delay_type;
    }

    public int getDelay_time() {
        return delay_time;
    }
    public void setDelay_time(int delay_time) {
        this.delay_time = delay_time;
    }

    public int getTotal_steps() {
        return total_steps;
    }
    public void setTotal_steps(int total_steps) {
        this.total_steps = total_steps;
    }

    public int getMax_steps() {
        return max_steps;
    }
    public void setMax_steps(int max_steps) {
        this.max_steps = max_steps;
    }

    public int getStts_yoga() {
        return stts_yoga;
    }
    public void setStts_yoga(int stts_yoga) {
        this.stts_yoga = stts_yoga;
    }
}
