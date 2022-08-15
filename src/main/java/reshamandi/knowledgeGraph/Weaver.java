package reshamandi.knowledgeGraph;

import java.util.*;

public class Weaver {
    private int id;//
    private String created_by;//
    private String created_date;
    private String category;
    private float cost_price;
    private float quantity;
    private float selling_price;
    private String status;
    private String type;
    //private int skubatchid;
    private int warehouseid;
    private float sold_quantity;
    private int weaver_id;
    private String sku_listing_status;
    private float defective_count;//
    private float landing_price;//
    private float gross_amount;
    private String uom;
    private float gst_amount;
    private float gst_percentage;
    private float discount_amount;
    private float logistics_amount;
    private float return_quantity;
    private String weave;
    private float returned_defective_quantity;
    private float cst;
    private float igst;
    private float total_amount;
    private int total_pre_tax_price;
    private int sku_count;
    private float sku_total_quantity;
    private String state;
    private float discount;
    private String business_type;
    private int transaction_id;

    public String getstate() {
        return state;
    }
    public void setstate(String a) {
        state = a;
    }
    public String getbusiness_type() {
        return business_type;
    }
    public void setbusiness_type(String a) {
        business_type = a;
    }

    public String gettotal_pre_tax_price() {
        return String.valueOf(total_pre_tax_price);
    }
    public void settotal_pre_tax_price(int a) {
        total_pre_tax_price = a;
    }
    public String getsku_count() {
        return  String.valueOf(sku_count);
    }
    public void setsku_count(int a) {
        sku_count = a;
    }
    public String gettransaction_id() {
        return String.valueOf(transaction_id);
    }
    public void settransaction_id(int a) {
        transaction_id = a;
    }
    public String getdiscount() {
        return String.valueOf(discount);
    }
    public void setdiscount(float a) {
        discount = a;
    }
    public String getcst() {
        return String.valueOf(cst);
    }
    public void setcst(float a) {
        cst = a;
    }
    public String getigst() {
        return String.valueOf(igst);
    }
    public void setigst(float a) {
        igst = a;
    }
    public String getsku_total_quantity() {
        return String.valueOf(sku_total_quantity);
    }
    public void setsku_total_quantity(float a) {
        sku_total_quantity = a;
    }
    public String gettotal_amount() {
        return String.valueOf(total_amount);
    }
    public void settotal_amount(float a) {
        total_amount = a;
    }
    public String getid() {
        return String.valueOf(id);
    }
    public void setid(int a) {
        id = a;
    }
    
    public String getwarehouseid() {
        return String.valueOf(warehouseid);
    }
    public void setwarehouseid(int a) {
        warehouseid = a;
    }
    public String getweaver_id() {
        return String.valueOf(weaver_id);
    }
    public void setweaver_id(int a) {
        weaver_id = a;
    }
    public String getcreated_by() {
        return created_by;
    }
    public void setcreated_by(String a) {
        created_by = a;
    }
    public String getcategory() {
        return category;
    }
    public void setcategory(String a) {
        category = a;
    }
    public String getstatus() {
        return status;
    }
    public void setstatus(String a) {
        status = a;
    }
    public String gettype() {
        return type;
    }
    public void settype(String a) {
        type = a;
    }
    public String getsku_listing_status() {
        return sku_listing_status;
    }
    public void setsku_listing_status(String a) {
        sku_listing_status = a;
    }
    public String getuom() {
        return uom;
    }
    public void setuom(String a) {
        uom = a;
    }
    public String getweave() {
        return weave;
    }
    public void setweave(String a) {
        weave = a;
    }
    public String getcost_price() {
        return String.valueOf(cost_price);
    }
    public void setcost_price(float a) {
        cost_price = a;
    }
    public String getquantity() {
        return String.valueOf(quantity);
    }
    public void setquantity(float a) {
        quantity = a;
    }
    public String getselling_price() {
        return String.valueOf(selling_price);
    }
    public void setselling_price(float a) {
        selling_price = a;
    }
    public String getsold_quantity() {
        return String.valueOf(sold_quantity);
    }
    public void setsold_quantity(float a) {
        sold_quantity = a;
    }
    public String getdefective_count() {
        return String.valueOf(defective_count);
    }
    public void setdefective_count(float a) {
        defective_count = a;
    }
    public String getlanding_price() {
        return String.valueOf(landing_price);
    }
    public void setlanding_price(float a) {
        landing_price = a;
    }
    public String getgross_amount() {
        return String.valueOf(gross_amount);
    }
    public void setgross_amount(float a) {
        gross_amount = a;
    }
    public String getgst_percentage() {
        return String.valueOf(gst_percentage);
    }
    public void setgst_percentage(float a) {
        gst_percentage = a;
    }
    public String getdiscount_amount() {
        return String.valueOf(discount_amount);
    }
    public void setdiscount_amount(float a) {
        discount_amount = a;
    }
    public String getgst_amount() {
        return String.valueOf(gst_amount);
    }
    public void setgst_amount(float a) {
        gst_amount = a;
    }
    public String getlogistics_amount() {
        return String.valueOf(logistics_amount);
    }
    public void setlogistics_amount(float a) {
        logistics_amount = a;
    }
    public String getreturn_quantity() {
        return String.valueOf(return_quantity);
    }
    public void setreturn_quantity(float a) {
        return_quantity = a;
    }
    public String getreturned_defective_quantity() {
        return String.valueOf(returned_defective_quantity);
    }
    public void setreturned_defective_quantity(float a) {
        returned_defective_quantity = a;
    }
    public String getcreated_date() {
        return created_date;
    }
    public void setcreated_date(String a) {
        created_date = a;
    }
}

