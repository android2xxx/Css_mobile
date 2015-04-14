package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HieuHT on 04/14/2015.
 */
public class EGroup {
    public String string;
    public final List<String> children = new ArrayList<String>();

    public EGroup(String string) {
        this.string = string;
    }
}
