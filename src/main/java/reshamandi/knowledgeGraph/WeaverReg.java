package reshamandi.knowledgeGraph;

public class WeaverReg {
    private int id;
    private long phone;
    private String name; 
    private String state; 
    private String type;
    private String twisted_type;
    private String denier;
    private String yarn_cocoon_type;


    public String getid() {
        return String.valueOf(id);
    }
    public void setid(int id) {
        this.id = id;
    }
    public String getphone() {
        return String.valueOf(phone);
    }
    public void setphone(long phone) {
        this.phone = phone;
    }
    public String getname() {
        return name;
    }
    public void setname(String name) {
        this.name = name;
    }
    public String getstate() {
        return state;
    }
    public void setstate(String state) {
        this.state = state;
    }
    public String gettype() {
        return type;
    }
    public void settype(String type) {
        this.type = type;
    }
    public String gettwisted_type() {
        return twisted_type;
    }
    public void settwisted_type(String twisted_type) {
        this.twisted_type = twisted_type;
    }
    public String getdenier() {
        return denier;
    }
    public void setdenier(String denier) {
        this.denier = denier;
    }
    public String getyarn_cocoon_type() {
        return yarn_cocoon_type;
    }
    public void setyarn_cocoon_type(String yarn_cocoon_type) {
        this.yarn_cocoon_type = yarn_cocoon_type;
    }
}
