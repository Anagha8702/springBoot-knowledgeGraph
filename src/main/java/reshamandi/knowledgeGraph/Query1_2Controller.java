package reshamandi.knowledgeGraph;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;

@Controller
public class Query1_2Controller {

    @GetMapping("/greeting")
    public String greetingForm(Model model) {
        model.addAttribute("q12", new Query1_2());
        String[] states = {"Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "National Capital Territory of Delhi", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"};
        String[] years = {"2016", "2017"};
        String[] seasons = {"Summer", "Winter"};
        model.addAttribute("states", states);
        model.addAttribute("years", years);
        model.addAttribute("seasons", seasons);

        return "index";
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute Query1_2 q12, Model model) {
         String data[][] = {{"Type","Blore","Chennai","Hyderabad"},{"Accessore","23","433","134"},{"Saree","334","34","22"}};
        String param2[] = new String[data[0].length];
        for(int i=1;i<data[0].length;i++) param2[i-1]=data[0][i];
        System.out.println(q12.getRole());
//        String data[][];
//        try (Neo4j neo = new Neo4j("neo4j+s://21679c7c.databases.neo4j.io", "neo4j", "mhTn8Mxnjax-SQ9Yj3et3Go49TxZKcJtBLNP3dyarqU")) {
//            data = neo.typeOrCategoryOrWeaveSoldInWhichSeasonByWeaver(q12.getSeason(), q12.getParameter(), q12.getSoldOrBoughtMonth());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        model.addAttribute("q12", q12);
        model.addAttribute("data", data);
        model.addAttribute("tableDisplay", 1);
        model.addAttribute("data_row", data.length);
        model.addAttribute("data_col", data[0].length);
        model.addAttribute("param2", param2);
        return "index";
    }

}
