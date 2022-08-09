package reshamandi.knowledgeGraph;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.neo4j.driver.Values.parameters;

public class Neo4j implements AutoCloseable {
    private final Driver driver;

    public Neo4j(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public String[][] typeOrCategoryOrWeaveSoldInWhichSeasonByWeaver(String season, String parameter, String soldOrBoughtMonth) {
        String[][] tableData1;
        try (Session session = driver.session()) {
            String seasonQueryPart = "";
            switch(season.equals("Summer") ? 0 : (season.equals("Spring") ? 1 : (season.equals("Monsoon") ? 2 : 3))){
                case 0: seasonQueryPart = "m.month='April' or m.month='May' or m.month='June'";
                        break;
                case 1: seasonQueryPart = "m.month='February' or m.month='March'";
                    break;
                case 2: seasonQueryPart = "m.month='July' or m.month='August' or m.month='September'";
                    break;
                case 3: seasonQueryPart = "m.month='October' or m.month='November' or m.month='December' or m.month='January'";
                    break;
            }
            String sOrBMonth = "(m)-[:" + soldOrBoughtMonth + "]->(p:Product)";
            String par = "collect(p." + parameter + ")";
            String query = "MATCH (m:Month) WHERE " + seasonQueryPart + " MATCH" + sOrBMonth + " WITH " + par + "AS paramList UNWIND paramList AS param return param,count(param)";
            tableData1 = session.writeTransaction(tx -> {
                Result result = tx.run(query);
                List<Record> list = new ArrayList<Record>(result.list());
                String[][] tableData = new String [list.size()][2];
                for(int i=0;i <list.size(); i++){
                    tableData[i][0] = String.valueOf(list.get(i).get("param")).substring(1,String.valueOf(list.get(i).get("param")).length()-1);
                    tableData[i][1] = String.valueOf(list.get(i).get("count(param)"));
                }
//                System.out.println(Arrays.deepToString(tableData));
                return tableData;
            });
        }
        return tableData1;
    }
    public static void main(String... args){
//        try (Neo4j neo = new Neo4j("neo4j+s://ba6b34f2.databases.neo4j.io", "neo4j", "yjPkJyIuUi65j4p5yNUK4Tua1ZzgK3z4VPGc0iU_7rU")) {
//            neo.printGreeting("Saree", "Tussar silk");
//        }
//        catch(Exception e){
//            System.out.println("Exception");
//        }
    }
}
