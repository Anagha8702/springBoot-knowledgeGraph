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
        return "index";
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute Query1_2 q12, Model model) {
        String data[][];
        try (Neo4j neo = new Neo4j("neo4j+s://21679c7c.databases.neo4j.io", "neo4j", "mhTn8Mxnjax-SQ9Yj3et3Go49TxZKcJtBLNP3dyarqU")) {
            data = neo.typeOrCategoryOrWeaveSoldInWhichSeasonByWeaver(q12.getSeason(), q12.getParameter(), q12.getSoldOrBoughtMonth());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("q12", q12);
        model.addAttribute("data", data);
        model.addAttribute("tableDisplay", 1);

        return "index";
    }

}
