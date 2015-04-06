package entity;

/**
 * Created by HieuHT on 04/03/2015.
 */
public class ESummaryService {

    private String ServiceId;
    private String AtmId;
    private String Bank;
    private String Location;
    private String Issue;

    public String getAtmId() {
        return AtmId;
    }

    public void setAtmId(String atmId) {
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

    public String getServiceId() {
        return ServiceId;
    }

    public void setServiceId(String serviceId) {
        ServiceId = serviceId;
    }
}
