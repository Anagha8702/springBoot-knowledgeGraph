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

    //Transactions.java implementation
    static String[] types_list={"Accessories","Art-silk","Bagru","Banarasi","Bhagalpuri","Chanderi-cotton","Cotton-linen","Cotton-tant","Cotton-voile","Tussar-silk","Linnen","Patola"};
    static String[] cat_list={"AC-Blanket(DOHAR)","Beads","Bedsheet","Bermuda","Shorts","Blouse","Chiffon","Crochet Lace","Fabric","Fusing","Cutting-Roll","Girls-Womens-suit","Saree","Shirt","Skirt","Dupatta"};
    static String[] weave_list={"ROYAL-OXFORD","Plain","JACQUARD","Yarn Dyed","Satin"};
    static String[] trans_months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    static String[] trans_states = {"Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh ", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "National Capital Territory of Delhi", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"};
    static String[] role_list={"Weaver","Retailer"};
    //This var is used to return all weaver/ retailer id collected in transactions
    String[] ID_list[]={};

    //Products.java implementation
    static String[] prod_cat_list={"AC-Blanket(DOHAR)","Beads","Bedsheet","Bermuda","Shorts","Blouse","Chiffon","Crochet Lace","Fabric","Fusing","Cutting-Roll","Girls-Womens-suit","Saree","Shirt","Skirt","Dupatta"};
    static String[] prod_type_list={"Accessories","Art-silk","Bagru","Banarasi","Bhagalpuri","Chanderi-cotton","Cotton-linen","Cotton-tant","Cotton-voile","Tussar-silk","Linnen","Patola"};
    static String[] prod_weave_list={"ROYAL-OXFORD","Plain","JACQUARD","Yarn Dyed","Satin"};
    static String[] filter_list={"Split","Total"};

    @GetMapping("/patch")
    public String retailerForm(Model model){
        model.addAttribute("ret", new Retailer());
        model.addAttribute("status",status2);
        model.addAttribute("skuListingStatus",skuListingStatus);
        model.addAttribute("uom",uom2);
        model.addAttribute("formdisp", 1);
        model.addAttribute("business_type",businessType);
         model.addAttribute("w", new Weaver());
        model.addAttribute("l1",status2);
        model.addAttribute("l2",skuListingStatus);
        model.addAttribute("l3",uom2);
        model.addAttribute("l4",businessType);
        return "form";
    }

    @PostMapping("/retailer")
    public String retailerSubmit(@ModelAttribute Retailer ret, Model model){
        neo.patchRetailerTransaction(ret);
        
        model.addAttribute("ret", new Retailer());
        model.addAttribute("status",status2);
        model.addAttribute("skuListingStatus",skuListingStatus);
        model.addAttribute("uom",uom2);
        model.addAttribute("business_type",businessType);
        return "updateDatabase";
    }
    @PostMapping("/weaver")
    public String retailerSubmit(@ModelAttribute Weaver w, Model model){
        neo.patchWeaverTransaction(w);
        
        model.addAttribute("w", new Weaver());
        model.addAttribute("formdisp", 1);
        model.addAttribute("l1",status2);
        model.addAttribute("l2",skuListingStatus);
        model.addAttribute("l3",uom2);
        model.addAttribute("l4",businessType);
        return "form";
    }

   @GetMapping("/register")
    public String registrationform(Model model) {
        model.addAttribute("wr", new WeaverReg());
        return "weavreg";
    }

    @PostMapping("/register")
    public String registrationsubmit(@ModelAttribute WeaverReg wr, Model model) {
        neo.patchWeaverRegistration(wr);
        // System.out.println(wr.getname()+ " " + wr.getid());
        model.addAttribute("wr", new WeaverReg());
        return "weavreg";
    }

    @GetMapping("/query")
    public String greetingForm(Model model) {
        //Statistics part
        model.addAttribute("q12", new Query1_2());
        model.addAttribute("states", states);
        model.addAttribute("years", years);
        model.addAttribute("seasons", seasons);

        //Transactions part
        model.addAttribute("trans", new Transactions());
        model.addAttribute("trans_states", trans_states);
        model.addAttribute("trans_months", trans_months);
        model.addAttribute("categories", cat_list);
        model.addAttribute("types", types_list);
        model.addAttribute("weaves", weave_list);
        model.addAttribute("role", role_list);
        model.addAttribute("ID", ID_list);

        //Products part
        model.addAttribute("prods", new Products());
        model.addAttribute("prod_cat", prod_cat_list);
        model.addAttribute("prod_type", prod_type_list);
        model.addAttribute("prod_weave", prod_weave_list);
        model.addAttribute("filter", filter_list);

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

    @PostMapping("/transactions")
    public String greetingTransactionSubmit(@ModelAttribute Transactions trans, Model model){
        //Transactions part
        model.addAttribute("trans", trans);
        model.addAttribute("trans_states", trans_states);
        model.addAttribute("trans_months", trans_months);
        model.addAttribute("categories", cat_list);
        model.addAttribute("types", types_list);
        model.addAttribute("weaves", weave_list);
        model.addAttribute("role", role_list);
        model.addAttribute("ID", ID_list);

        String data[][];
        try{
            data = neo.topTenProduct(trans);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        

        System.out.println(trans.getRole());
        System.out.println(Arrays.deepToString(trans.getID()));
        System.out.println(Arrays.deepToString(trans.getMonth()));
        System.out.println(Arrays.deepToString(trans.getState()));
        System.out.println(Arrays.deepToString(trans.getCategory()));
        System.out.println(Arrays.deepToString(trans.getWeave()));
        System.out.println(Arrays.deepToString(trans.getType()));

        return "index";
        
    }

    @PostMapping("/products")
    public String greetingProductsSubmit(@ModelAttribute Products prods, Model model){
        
        model.addAttribute("prods", prods);
        model.addAttribute("prod_cat", prod_cat_list);
        model.addAttribute("prod_type", prod_type_list);
        model.addAttribute("prod_weave", prod_weave_list);
        model.addAttribute("filter", filter_list);

        System.out.println(prods.getFilter());
        System.out.println(Arrays.deepToString(prods.getCategory()));
        System.out.println(Arrays.deepToString(prods.getType()));
        System.out.println(Arrays.deepToString(prods.getWeave()));

        return "index";
    }
}
