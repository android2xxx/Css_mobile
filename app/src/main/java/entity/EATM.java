package entity;

import java.util.Date;
import java.util.List;

/**
 * Created by HieuHT on 04/06/2015.
 */
public class EATM {

    private String atm_serial_id;
    private String atm_serial;


    public String getAtmId() {
        return atm_serial_id;
    }

    public void setAtmId(String atmId) {
        this.atm_serial_id = atmId;
    }

    public String getSerial() {
        return atm_serial;
    }

    public void setSerial(String serial) {
        atm_serial = serial;
    }

    @Override
    public String toString() {
        return atm_serial_id + " - " + atm_serial;
    }
}

