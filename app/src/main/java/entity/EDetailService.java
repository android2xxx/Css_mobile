package entity;

import java.util.Date;
import java.util.List;

/**
 * Created by HieuHT on 04/06/2015.
 */
public class EDetailService {
    private Integer ServiceId;
    private Integer AtmId;
    private String Serial;
    private String Branch;
    private String Contract;
    private Date StartTime;
    private String Solution;
    private String Bank;
    private String Location;
    private String Issue;
    private List<EPart> Parts;

    public Integer getAtmId() {
        return AtmId;
    }

    public void setAtmId(Integer atmId) {
        this.AtmId = atmId;
    }

    public String getBank() {
        return Bank;
    }

    public void setBank(String bank) {
        this.Bank = bank;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public String getIssue() {
        return Issue;
    }

    public void setIssue(String issue) {
        this.Issue = issue;
    }

    public Integer getServiceId() {
        return ServiceId;
    }

    public void setServiceId(Integer serviceId) {
        ServiceId = serviceId;
    }

    public String getSerial() {
        return Serial;
    }

    public void setSerial(String serial) {
        Serial = serial;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public String getContract() {
        return Contract;
    }

    public void setContract(String contract) {
        Contract = contract;
    }

    public Date getStartTime() {
        return StartTime;
    }

    public void setStartTime(Date startTime) {
        StartTime = startTime;
    }

    public String getSolution() {
        return Solution;
    }

    public void setSolution(String solution) {
        Solution = solution;
    }

    public List<EPart> getParts() {
        return Parts;
    }

    public void setParts(List<EPart> parts) {
        Parts = parts;
    }
}

