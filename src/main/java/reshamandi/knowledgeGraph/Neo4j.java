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
            tableData1 = session.writeTransaction(tx -> {
                Result result = tx.run("MATCH (m:Month)\n" +
                        "WITH collect(m) as monthList\n" +
                        "UNWIND monthList as months\n" +
                        "MATCH (months)-[r:boughtMonth]->(pdt:Product)\n" +
                        "WHERE(r.state=\"Karnataka\" or r.state=\"Odisha\") and (r.month=\"January\" or r.month=\"May\")\n" +
                        "WITH pdt,collect(r.month) AS outputList\n" +
                        "UNWIND outputList AS eachOutput\n" +
                        "WITH eachOutput,collect(pdt.category) AS filterList\n" +
                        "UNWIND filterList AS filters\n" +
                        "RETURN eachOutput,filters,count(filters)\n" +
                        "ORDER by eachOutput");
                List<Record> list = new ArrayList<Record>(result.list());
                String[][] tableData = new String [list.size()][3];
                System.out.println(list);
//                for(int i=0;i <list.size(); i++){
//                    tableData[i][0] = String.valueOf(list.get(i).get("param")).substring(1,String.valueOf(list.get(i).get("param")).length()-1);
//                    tableData[i][1] = String.valueOf(list.get(i).get("count(param)"));
//                }
//                System.out.println(Arrays.deepToString(tableData));
                return tableData;
            });
        }
        return tableData1;
    }
    public static void main(String... args){
//        try (Neo4j neo = new Neo4j("neo4j+s://21679c7c.databases.neo4j.io", "neo4j", "mhTn8Mxnjax-SQ9Yj3et3Go49TxZKcJtBLNP3dyarqU")) {
//            Arrays.deepToString(neo.typeOrCategoryOrWeaveSoldInWhichSeasonByWeaver("Summer", "type", "soldMonth"));
//        }
//        catch(Exception e){
//            System.out.println("Exception");
//        }
    }
}
