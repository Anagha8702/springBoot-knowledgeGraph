package reshamandi.knowledgeGraph;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.engine.AttributeName;

import java.util.Arrays;
import io.github.cdimascio.dotenv.Dotenv;

@Controller
public class WebController {
    static final Dotenv dotenv = Dotenv.load();
    static final Neo4j neo = new Neo4j(dotenv.get("NEO4J_URI"), dotenv.get("NEO4j_AUTH_USER"), dotenv.get("NEO4j_AUTH_PASSWORD"));
    static String[] states = {"Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh ", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "National Capital Territory of Delhi", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"};
    static String[] years = {"2016", "2017", "2018", "2019", "2020"};
    static String[] seasons = {"Monsoon", "Spring", "Summer", "Winter"};
    static String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
//    type = 12
//    category=16
//    weave=5
    
    //Retailer declarations
    static String[] status2= {"New","Sold"};
    static String[] skuListingStatus= {"Sampled","Purchased"};
    static String[] uom2 = {"Kgs","Meters","Pieces"};
    static String[] businessType= {"ECD","D2R"};

    @GetMapping("/retailer")
    public String retailerForm(Model model){
        model.addAttribute("ret", new Retailer());
        model.addAttribute("status",status2);
        model.addAttribute("skuListingStatus",skuListingStatus);
        model.addAttribute("uom",uom2);
        model.addAttribute("business_type",businessType);
        return "updateDatabase";
    }

    @PostMapping("/retailer")
    public String retailerSubmit(@ModelAttribute Retailer ret, Model model){
        System.out.println(ret.getid());
        System.out.println(ret.getcreated_by());
        System.out.println(ret.getcreated_date());
        System.out.println(ret.getcategory());
        System.out.println(ret.getcost_price());
        System.out.println(ret.getquantity());
        System.out.println(ret.getselling_price());
        System.out.println(ret.getstatus());
        System.out.println(ret.gettype());
        System.out.println(ret.getwarehouseid());
        System.out.println(ret.getsold_quantity());
        System.out.println(ret.getsku_listing_status());
        System.out.println(ret.getlanding_price());
        System.out.println(ret.getuom());
        System.out.println(ret.getgst_amount());
        System.out.println(ret.getgst_percentage());
        System.out.println(ret.getlogistics_amount());
        System.out.println(ret.getweave());
        System.out.println(ret.getcst());
        System.out.println(ret.getigst());
        System.out.println(ret.gettotal_amount());
        System.out.println(ret.gettotal_pre_tax_price());
        System.out.println(ret.getsku_count());
        System.out.println(ret.getsku_total_quantity());
        System.out.println(ret.getstate());
        System.out.println(ret.getretailer_id());
        System.out.println(ret.getdiscount());
        System.out.println(ret.getgross_amount());
        System.out.println(ret.getdiscount_amount());
        System.out.println(ret.getbusiness_type());
        System.out.println(ret.gettransaction_id());
        
        model.addAttribute("ret", new Retailer());
        model.addAttribute("status",status2);
        model.addAttribute("sku_listing_status",skuListingStatus);
        model.addAttribute("uom",uom2);
        model.addAttribute("business_type",businessType);
        return "updateDatabase";
    }
    @GetMapping("/query")
    public String greetingForm(Model model) {
        model.addAttribute("q12", new Query1_2());
        model.addAttribute("states", states);
        model.addAttribute("years", years);
        model.addAttribute("seasons", seasons);
        return "index";
    }

    @PostMapping("/query")
    public String greetingSubmit(@ModelAttribute Query1_2 q12, Model model) {

    //    String data[][] = {{"Type","Blore","Chennai","Hyderabad"},{"Accessore","23","433","134"},{"Saree","334","34","22"}};
        String data[][];
        try{
            data = neo.statistics(q12.getRole(), q12.getProductSpec(), q12.getFilter(), q12.getStates(), q12.getSeasons(), q12.getYears());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String headData[] = data[0];
        String data1[][] = new String[data.length-1][data[0].length];
        for(int i=1;i<data.length;i++){
            for(int j=0;j<data[0].length;j++)
                data1[i-1][j] = data[i][j];
        }
        String param2[] = new String[data[0].length - 1];
        for(int i=1;i<data[0].length;i++) param2[i-1]=data[0][i];
        System.out.println(Arrays.deepToString(data));
        System.out.println(Arrays.deepToString(data1));
        System.out.println(Arrays.deepToString(param2));
        model.addAttribute("q12", q12);
        model.addAttribute("data", data);
        model.addAttribute("data1", data1);
        model.addAttribute("headData", headData);
        model.addAttribute("tableDisplay", 1);
        model.addAttribute("data_row", data.length);
        model.addAttribute("data_col", data[0].length);
        model.addAttribute("param2", param2);
        model.addAttribute("states", states);
        model.addAttribute("years", years);
        model.addAttribute("seasons", seasons);
        return "index";
    }

}
