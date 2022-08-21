package reshamandi.knowledgeGraph;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Neo4j implements AutoCloseable {
    dict d = new dict();

    private final Driver driver;

    public Neo4j(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public String monthFromSeason(String season){
        switch (season) {
            case "Summer":
                return ("r.month='April' or r.month='May' or r.month='June' or ");
            case "Spring":
                return ("r.month='February' or r.month='March' or ");
            case "Monsoon":
                return ("r.month='July' or r.month='August' or r.month='September' or ");
            default:
                return ("r.month='October' or r.month='November' or r.month='December' or r.month='January' or ");
        }
    }

    public String[] seasonToMonth(String[] seasons){
        ArrayList<String> months = new ArrayList<String>();
        for(int i=0;i<seasons.length;i++){
            switch (seasons[i]){
                case "Summer": months.add("April"); months.add("May"); months.add("June"); break;
                case "Spring": months.add("February"); months.add("March"); break;
                case "Monsoon": months.add("July"); months.add("August"); months.add("September"); break;
                default: months.add("October"); months.add("November"); months.add("December"); months.add("January"); break;
            }
        }
        String[] m = new String[0];
        m = months.toArray(m);
        return m;
    }

    public int columnsFromSeasons(String[] seasons){
        int sum = 0;
        for(int i=0;i<seasons.length;i++){
            switch (seasons[i]){
                case "Summer": sum+=3; break;
                case "Spring": sum+=2; break;
                case "Monsoon": sum+=3; break;
                default: sum+=4; break;
            }
        }
        return sum;
    }

    public static int findIndex(String arr[], String t)
    {
        int len = arr.length;
        int i = 0;
        while (i < len) {
            if (arr[i].equals(t)) {
                return i;
            }
            else {
                i = i + 1;
            }
        }
        return -1;
    }

    public String[][] statistics(String role, String productSpec, String filter, String[] states, String[] seasons, String[] years){
        if(productSpec == null) productSpec = "pdt.type";
        if(filter == null) filter= "r.month";
        String[][] tableData1;
        try (Session session = driver.session()) {
            String qrole = "MATCH (m:Month) WITH collect(m) as monthList UNWIND monthList as months MATCH (months)-[r:" +
                        role + "]->(pdt:Product) ";
            boolean flag = false;
            int i;
            StringBuilder qstates = new StringBuilder("");
            StringBuilder qseasons = new StringBuilder("");
            StringBuilder qyears = new StringBuilder("");

            if(states != null){
                flag = true;
                for(i=0;i<(states.length)-1;i++)
                    qstates.append("r.state='").append(states[i]).append("' or ");
                qstates.append("r.state='").append(states[i]).append("') ");
                qstates = new StringBuilder("(" + qstates);
            }

            if(seasons != null){
                if(flag) qseasons = new StringBuilder("and (");
                else qseasons = new StringBuilder("(");
                flag = true;
                for(i=0;i<(seasons.length)-1;i++){
                    qseasons.append(monthFromSeason(seasons[i]));
                }
                qseasons.append(monthFromSeason(seasons[i]));
                qseasons = new StringBuilder(qseasons.substring(0, qseasons.length() - 4));
                qseasons.append(") ");
            }

            if(years != null){
                if(flag) qyears = new StringBuilder("and (");
                else qyears = new StringBuilder("(");
                flag = true;
                for(i=0;i<(years.length)-1;i++)
                    qyears.append("left(r.created_date,4)='").append(years[i]).append("' or ");
                qyears.append("left(r.created_date,4)='").append(years[i]).append("') ");
            }
            String wquery = qrole;
            if(flag) wquery += "WHERE " + qstates + qseasons + qyears;
            wquery += "WITH pdt,collect(";
            if(filter.equals("r.created_date")) wquery += "left(r.created_date,4)) ";
            else wquery += (filter+") ");
            wquery += "AS outputList UNWIND outputList AS filter2  WITH filter2,collect(";
            String query = wquery + productSpec + ") AS filterList UNWIND filterList AS filter1 RETURN filter1,filter2,count(filter1) ORDER by filter1, filter2";
            System.out.println(query);


            String finalProductSpec = productSpec;
            String finalFilter = filter;
            tableData1 = session.writeTransaction(tx -> {
                Result result = tx.run(query);
                List<Record> list = new ArrayList<Record>(result.list());
                int rows,columns,f=1;
                rows = finalProductSpec.equals("pdt.type") ? 12 : (finalProductSpec.equals("pdt.category") ? 16 : 5);
                switch (finalFilter){
                    case "r.state": columns = (states != null) ? states.length : WebController.states.length;
                                    f = 0;
                                    break;
                    case "r.month": columns = (seasons != null) ? columnsFromSeasons(seasons) : WebController.months.length;
                                    f = 1;
                                    break;
                    default: columns = (years != null) ? years.length : 5; 
                             f= 2;
                             break;
                    }
                String[][] tableData = new String [rows+1][columns+1];
                String months[] = {};
                if(seasons != null) months = seasonToMonth(seasons);
                switch (f){
                    case 0: for(int k=1;k<=columns;k++) tableData[0][k] = (states != null) ? states[k-1] : WebController.states[k-1];
                        break;
                    case 1: for(int k=1;k<=columns;k++) tableData[0][k] = (seasons != null) ? months[k-1] : WebController.months[k-1];
                        break;
                    default: for(int k=1;k<=columns;k++) tableData[0][k] = (years != null) ? years[k-1] : WebController.years[k-1];
                }

                tableData[0][0] = finalProductSpec.substring(4);
                int rowIndex = 1;
                for(int j=0; j<list.size(); j++,rowIndex++){
                    // System.out.println("lol");
                    tableData[rowIndex][0] = list.get(j).get("filter1").asString();
                    while(j<list.size()-1 && list.get(j).get("filter1").asString().equals(list.get(j+1).get("filter1").asString())){
                        tableData[rowIndex][findIndex(tableData[0], list.get(j).get("filter2").asString())] = String.valueOf(list.get(j).get("count(filter1)"));
                        j++;
                    }
                    tableData[rowIndex][findIndex(tableData[0], list.get(j).get("filter2").asString())] = String.valueOf(list.get(j).get("count(filter1)"));
                    for(int l=1;l<=columns;l++){
                        if(tableData[rowIndex][l] == null)tableData[rowIndex][l] = "0";
                    }
                }

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



    ////////////////////////// PATCHING /////////////////////////////////////////////

    public void patchWeaverRegistration(WeaverReg w){
        String weaverNode = "MERGE (w:Weaver{phone:'" + w.getphone() + "',name:'" + w.getname() + "',yarn_cocoon_type:'" + w.getyarn_cocoon_type() + "',state:'" + w.getstate() + "',id:'" + w.getid() + "',yarn_capacity:'" + w.getyarn_capacity() + "',type:'" + w.gettype() + "',twisted_type:'" + w.gettwisted_type() + "',denier:'" + w.getdenier() + "'})";
        System.out.println(weaverNode);
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
            tx.run(weaverNode);
            System.out.println("Hit");
            return 0;
        });
    }
        catch(Exception e){
            System.out.println("Error");
        }
    }

    public void patchRetailerRegistration(RetailerReg r){
        String retailerNode = "MERGE (r:Retailer{phone:'" + r.getPhone() + "',name:'" + r.getName() + "',majority_sourced_textile:'" + r.getMajority_sourced_textile() + "',state:'" + r.getState() + "',id:'" + r.getId() + "',retailer_of:'" + r.getRetailer_of() + "'})";
        System.out.println(retailerNode);
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
            tx.run(retailerNode);
            System.out.println("New retailer registered successfully");
            return 0;
        });
    }
        catch(Exception e){
            System.out.println("Error");
        }
    }

    public void patchWeaverTransaction(Weaver w){

        // creating new nodes
        String nodes = "MERGE (p1:Product{pdtid:'" + d.pdtid(w.gettype(), w.getcategory(), w.getweave()) + "',type:'" + w.gettype() + "' , weave: '" + w.getweave() + "' , category:'" + w.getcategory() + "'})\n" +
                       "MERGE (t:Type{type:'" + w.gettype() + "'})\n" +
                       "MERGE (t1:Category{category:'" + w.getcategory() + "'})\n" +
                       "MERGE (t2:Weave{weave:'" + w.getweave() + "'})\n" +
                       "MERGE (m:Month{month : '" + d.monthName(w.getcreated_date()) + "'})\n" +
                       "MERGE (s:State{state : '" + w.getstate() + "'})";
        System.out.println(nodes);


        //soldbyRelation
        String soldByRelation = "Match (w:Weaver) WHERE w.id = '" + w.getweaver_id() + "'\n" +
                                "MATCH (p:Product) WHERE p.pdtid = '" + d.pdtid(w.gettype(), w.getcategory(), w.getweave()) + "'\n" +
                                "MERGE (w)-[rl:soldBy{selling_price:'" + w.getselling_price() + "',return_quantity:'" + w.getreturn_quantity() + "',cst:'" + w.getcst() + "',discount_amount:'" + w.getdiscount_amount() + "',discount:'" + w.getdiscount() + "',weaver_id:'" + w.getweaver_id() + "',gross_amount:'" + w.getgross_amount() + "',type:'" + w.gettype() + "',sold_quantity:'" + w.getsold_quantity() + "',igst:'" + w.getigst() + "',uom:'" + w.getuom() + "',gst_amount:'" + w.getgst_amount() + "',sku_listing_status:'" + w.getsku_listing_status() + "',warehouseid:'" + w.getwarehouseid() + "',sku_total_quantity:'" + w.getsku_total_quantity() + "',business_type:'" + w.getbusiness_type() + "',returned_defective_quantity:'" + w.getreturned_defective_quantity() + "',state:'" + w.getstate() + "',id:'" + w.getid() + "',landing_price:'" + w.getlanding_price() + "',cost_price:'" + w.getcost_price() + "',transaction_id:'" + w.gettransaction_id() + "',quantity:'" + w.getquantity() + "',total_pre_tax_price:'" + w.gettotal_pre_tax_price() + "',pdt_id:'" + d.pdtid(w.gettype(), w.getcategory(), w.getweave()) + "',created_by:'" + w.getcreated_by() + "',gst_percentage:'" + w.getgst_percentage() + "',month:'" + d.monthName(w.getcreated_date()) + "',total_amount:'" + w.gettotal_amount() + "',sku_count:'" + w.getsku_count() + "',logistics_amount:'" + w.getlogistics_amount() + "',defective_count:'" + w.getdefective_count() + "',created_date:'" + w.getcreated_date() + "',category:'" + w.getcategory() + "',weave:'" + w.getweave() + "',status:'" + w.getstatus() + "'}]->(p)";
        System.out.println(soldByRelation);

     
        //linking type to product
        String linkTypeToProduct = "MATCH (t:Type) WHERE t.type ='" + w.gettype() + "'\n" +
                                   "MATCH (p:Product) WHERE p.pdtid = '" + d.pdtid(w.gettype(), w.getcategory(), w.getweave()) + "'\n" +
                                   "MERGE (t)-[r:typeName{type : '" + w.gettype() + "'}]->(p)";
        System.out.println(linkTypeToProduct);


        //linking category to product
        String linkCategoryToProduct = "MATCH (t:Category) WHERE t.category ='" + w.getcategory() + "'\n" +
                                   "MATCH (p:Product) WHERE p.pdtid = '" + d.pdtid(w.gettype(), w.getcategory(), w.getweave()) + "'\n" +
                                   "MERGE (t)-[r:categoryName{category : '" + w.getcategory() + "'}]->(p)";
        System.out.println(linkCategoryToProduct);


        //linking weave to product
        String linkWeaveToProduct = "MATCH (t:Weave) WHERE t.weave ='" + w.getweave() + "'\n" +
                                   "MATCH (p:Product) WHERE p.pdtid = '" + d.pdtid(w.gettype(), w.getcategory(), w.getweave()) + "'\n" +
                                   "MERGE (t)-[r:weaveName{weave : '" + w.getweave() + "'}]->(p)";
        System.out.println(linkWeaveToProduct);

        //linking - soldMonth
        String soldMonth = "MATCH (m:Month) WHERE m.month = '" + d.monthName(w.getcreated_date()) + "'\n" +
                           "MATCH (p:Product) WHERE p.pdtid = '" + d.pdtid(w.gettype(), w.getcategory(), w.getweave()) + "'\n" +
                           "MERGE (m)-[r:soldMonth{transaction_id:'" + w.gettransaction_id() + "',month:'" + d.monthName(w.getcreated_date()) + "',weaver_id:'" + w.getweaver_id() + "',state:'" + w.getstate() + "',created_date:'" + w.getcreated_date() + "',pdt_id:'" + d.pdtid(w.gettype(), w.getcategory(), w.getweave()) + "'}]->(p)";
        System.out.println(soldMonth);


        //linking - soldState
        String soldState = "MATCH (m:State) WHERE m.state = '" + w.getstate() + "'\n" +
                           "MATCH (p:Product) WHERE p.pdtid = '" + d.pdtid(w.gettype(), w.getcategory(), w.getweave()) + "'\n" +
                           "MERGE (m)-[r:soldState{transaction_id:'" + w.gettransaction_id() + "',month:'" + d.monthName(w.getcreated_date()) + "',weaver_id:'" + w.getweaver_id() + "',state:'" + w.getstate() + "',created_date:'" + w.getcreated_date() + "',pdt_id:'" + d.pdtid(w.gettype(), w.getcategory(), w.getweave()) + "'}]->(p)";
        System.out.println(soldState);

        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
            tx.run(nodes);
            tx.run(soldByRelation);
            tx.run(linkTypeToProduct);
            tx.run(linkCategoryToProduct);
            tx.run(linkWeaveToProduct);
            tx.run(soldMonth);
            tx.run(soldState);
            System.out.println("Hit");
            return 0;
        });
    }
    catch(Exception e){
        System.out.println("Error");
    }
}


public void patchRetailerTransaction(Retailer r){

    // creating new nodes
    String nodes = "MERGE (p1:Product{pdtid:'" + d.pdtid(r.gettype(), r.getcategory(), r.getweave()) + "',type:'" + r.gettype() + "' , weave: '" + r.getweave() + "' , category:'" + r.getcategory() + "'})\n" +
                   "MERGE (t:Type{type:'" + r.gettype() + "'})\n" +
                   "MERGE (t1:Category{category:'" + r.getcategory() + "'})\n" +
                   "MERGE (t2:Weave{weave:'" + r.getweave() + "'})\n" +
                   "MERGE (m:Month{month : '" + d.monthName(r.getcreated_date()) + "'})\n" +
                   "MERGE (s:State{state : '" + r.getstate() + "'})";
    System.out.println(nodes);


    //BoughtbyRelation
    String BoughtbyRelation = "Match (r:Retailer) WHERE r.id = '" + r.getretailer_id() + "'\n" +
                            "MATCH (p:Product) WHERE p.pdtid = '" + d.pdtid(r.gettype(), r.getcategory(), r.getweave()) + "'\n" +
                            "MERGE (p)-[rl:boughtBy{selling_price:'" + r.getselling_price() + "',cst:'" + r.getcst() + "',discount_amount:'" + r.getdiscount_amount() + "',discount:'" + r.getdiscount() + "',gross_amount:'" + r.getgross_amount() + "',type:'" + r.gettype() + "',sold_quantity:'" + r.getsold_quantity() + "',igst:'" + r.getigst() + "',gst_amount:'" + r.getgst_amount() + "',uom:'" + r.getuom() + "',sku_listing_status:'" + r.getsku_listing_status() + "',warehouseid:'" + r.getwarehouseid() + "',sku_total_quantity:'" + r.getsku_total_quantity() + "',business_type:'" + r.getbusiness_type() + "',state:'" + r.getstate() + "',id:'" + r.getid() + "',landing_price:'" + r.getlanding_price() + "',cost_price:'" + r.getcost_price() + "',transaction_id:'" + r.gettransaction_id() + "',quantity:'" + r.getquantity() + "',total_pre_tax_price:'" + r.gettotal_pre_tax_price() + "',retailer_id:'" + r.getretailer_id() + "',pdt_id:'" + d.pdtid(r.gettype(), r.getcategory(), r.getweave()) + "',created_by:'" + r.getcreated_by() + "',gst_percentage:'" + r.getgst_percentage() + "',month:'" + d.monthName(r.getcreated_date()) + "',total_amount:'" + r.gettotal_amount() + "',sku_count:'" + r.getsku_count() + "',logistics_amount:'" + r.getlogistics_amount() + "',created_date:'" + r.getcreated_date() + "',category:'" + r.getcategory() + "',weave:'" + r.getweave() + "',status:'" + r.getstatus() + "'}]->(r)";
    System.out.println(BoughtbyRelation);

 
    //linking type to product
    String linkTypeToProduct = "MATCH (t:Type) WHERE t.type ='" + r.gettype() + "'\n" +
                               "MATCH (p:Product) WHERE p.pdtid = '" + d.pdtid(r.gettype(), r.getcategory(), r.getweave()) + "'\n" +
                               "MERGE (t)-[r:typeName{type : '" + r.gettype() + "'}]->(p)";
    System.out.println(linkTypeToProduct);


    //linking category to product
    String linkCategoryToProduct = "MATCH (t:Category) WHERE t.category ='" + r.getcategory() + "'\n" +
                               "MATCH (p:Product) WHERE p.pdtid = '" + d.pdtid(r.gettype(), r.getcategory(), r.getweave()) + "'\n" +
                               "MERGE (t)-[r:categoryName{category : '" + r.getcategory() + "'}]->(p)";
    System.out.println(linkCategoryToProduct);


    //linking weave to product
    String linkWeaveToProduct = "MATCH (t:Weave) WHERE t.weave ='" + r.getweave() + "'\n" +
                               "MATCH (p:Product) WHERE p.pdtid = '" + d.pdtid(r.gettype(), r.getcategory(), r.getweave()) + "'\n" +
                               "MERGE (t)-[r:weaveName{weave : '" + r.getweave() + "'}]->(p)";
    System.out.println(linkWeaveToProduct);

    //linking - boughtMonth
    String boughtMonth = "MATCH (m:Month) WHERE m.month = '" + d.monthName(r.getcreated_date()) + "'\n" +
                       "MATCH (p:Product) WHERE p.pdtid = '" + d.pdtid(r.gettype(), r.getcategory(), r.getweave()) + "'\n" +
                       "MERGE (m)-[r:boughtMonth{transaction_id:'" + r.gettransaction_id() + "',month:'" + d.monthName(r.getcreated_date()) + "',retailer_id:'" + r.getretailer_id() + "',state:'" + r.getstate() + "',created_date:'" + r.getcreated_date() + "',pdt_id:'" + d.pdtid(r.gettype(), r.getcategory(), r.getweave()) + "'}]->(p)";
    System.out.println(boughtMonth);


    //linking - boughtState
    String boughtState = "MATCH (m:State) WHERE m.state = '" + r.getstate() + "'\n" +
                       "MATCH (p:Product) WHERE p.pdtid = '" + d.pdtid(r.gettype(), r.getcategory(), r.getweave()) + "'\n" +
                       "MERGE (m)-[r:boughtState{transaction_id:'" + r.gettransaction_id() + "',month:'" + d.monthName(r.getcreated_date()) + "',retailer_id:'" + r.getretailer_id() + "',state:'" + r.getstate() + "',created_date:'" + r.getcreated_date() + "',pdt_id:'" + d.pdtid(r.gettype(), r.getcategory(), r.getweave()) + "'}]->(p)";
    System.out.println(boughtState);

    try (Session session = driver.session()) {
        session.writeTransaction(tx -> {
        tx.run(nodes);
        tx.run(BoughtbyRelation);
        tx.run(linkTypeToProduct);
        tx.run(linkCategoryToProduct);
        tx.run(linkWeaveToProduct);
        tx.run(boughtMonth);
        tx.run(boughtState);
        System.out.println("Updated database with new retailer transaction");
        return 0;
    });
}
    catch(Exception e){
        System.out.println("Error");
    }
}

    ////////////////////////// PATCHING /////////////////////////////////////////////

}
