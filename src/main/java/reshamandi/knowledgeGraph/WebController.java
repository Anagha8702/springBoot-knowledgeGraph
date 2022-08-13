package reshamandi.knowledgeGraph;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;

@Controller
public class WebController {

    static String[] states = {"Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "National Capital Territory of Delhi", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"};
    static String[] years = {"2016", "2017", "2018", "2019", "2020"};
    static String[] seasons = {"Monsoon", "Spring", "Summer", "Winter"};
    static String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
//    type = 12
//    category=16
//    weave=5
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
//        System.out.println(q12.getRole());

//        if(Arrays.deepToString(q12.getStates())== null) System.out.println("ds");
//        System.out.println(q12.getFilter());
//        System.out.println(q12.getProductSpec());
//        System.out.println(Arrays.toString(q12.getSeasons()));
//        System.out.println(Arrays.toString(q12.getYears()));







    //    String data[][] = {{"Type","Blore","Chennai","Hyderabad"},{"Accessore","23","433","134"},{"Saree","334","34","22"}};
        String data[][];
        try (Neo4j neo = new Neo4j("neo4j+s://ba6b34f2.databases.neo4j.io", "neo4j", "yjPkJyIuUi65j4p5yNUK4Tua1ZzgK3z4VPGc0iU_7rU")) {
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
        String param2[] = new String[data[0].length];
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
