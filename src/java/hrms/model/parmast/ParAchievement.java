package hrms.model.parmast;

import org.springframework.web.multipart.MultipartFile;

public class ParAchievement {

    private int pacid;

    private String task;

    private String target;

    private String achievementpercent;

    private String achievement;

    private HWAttachements attid;

    private String achievementpersqualitative;

    private int slno;

    private String mode;

    private int hidparid;

    private int hidpacid;

    private String fiscalyear;

    private MultipartFile attchfile;

    private int hidattchid;

    private String hidattchfilename;

    private String hidparfrmdt;

    private String hidpartodt;

    private String PercentAchievement;

    private int parid;

    public int getPacid() {
        return pacid;
    }

    public void setPacid(int pacid) {
        this.pacid = pacid;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getAchievementpercent() {
        return achievementpercent;
    }

    public void setAchievementpercent(String achievementpercent) {
        this.achievementpercent = achievementpercent;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public String getAchievementpersqualitative() {
        return achievementpersqualitative;
    }

    public void setAchievementpersqualitative(String achievementpersqualitative) {
        this.achievementpersqualitative = achievementpersqualitative;
    }

    public int getSlno() {
        return slno;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getHidparid() {
        return hidparid;
    }

    public void setHidparid(int hidparid) {
        this.hidparid = hidparid;
    }

    public int getHidpacid() {
        return hidpacid;
    }

    public void setHidpacid(int hidpacid) {
        this.hidpacid = hidpacid;
    }

    public MultipartFile getAttchfile() {
        return attchfile;
    }

    public void setAttchfile(MultipartFile attchfile) {
        this.attchfile = attchfile;
    }

    public HWAttachements getAttid() {
        return attid;
    }

    public String getFiscalyear() {
        return fiscalyear;
    }

    public void setFiscalyear(String fiscalyear) {
        this.fiscalyear = fiscalyear;
    }

    public void setAttid(HWAttachements attid) {
        this.attid = attid;
    }

    public String getHidparfrmdt() {
        return hidparfrmdt;
    }

    public void setHidparfrmdt(String hidparfrmdt) {
        this.hidparfrmdt = hidparfrmdt;
    }

    public String getHidpartodt() {
        return hidpartodt;
    }

    public void setHidpartodt(String hidpartodt) {
        this.hidpartodt = hidpartodt;
    }

    public int getHidattchid() {
        return hidattchid;
    }

    public void setHidattchid(int hidattchid) {
        this.hidattchid = hidattchid;
    }

    public String getHidattchfilename() {
        return hidattchfilename;
    }

    public void setHidattchfilename(String hidattchfilename) {
        this.hidattchfilename = hidattchfilename;
    }

    public String getPercentAchievement() {
        return PercentAchievement;
    }

    public void setPercentAchievement(String PercentAchievement) {
        this.PercentAchievement = PercentAchievement;
    }

    public int getParid() {
        return parid;
    }

    public void setParid(int parid) {
        this.parid = parid;
    }

  

}
