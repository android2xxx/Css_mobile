package entity;

/**
 * Created by HieuHT on 04/06/2015.
 */
public class EPart {
    private Integer PartId;
    private String PartName;
    private Integer Quantity;
    private String Store;

    public Integer getPartId() {
        return PartId;
    }

    public void setPartId(Integer partId) {
        PartId = partId;
    }

    public String getPartName() {
        return PartName;
    }

    public void setPartName(String partName) {
        PartName = partName;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer quantity) {
        Quantity = quantity;
    }

    public String getStore() {
        return Store;
    }

    public void setStore(String store) {
        Store = store;
    }
}
