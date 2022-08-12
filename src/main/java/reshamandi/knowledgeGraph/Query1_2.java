package reshamandi.knowledgeGraph;

public class Query1_2 {
    private String role; //boughtMonth/soldMonth
    private String productSpec; //pdt.type/pdt.category/pdt.weave
    private String filter; //r.state/r.month/left(r.created_date,4)
    private String[] states; //Andaman and Nicobar Islands, Andhra Pradesh, Arunachal Pradesh , Assam, Bihar, Chandigarh, Chhattisgarh,
                          // Dadra and Nagar Haveli, Daman and Diu, Goa, Gujarat, Haryana, Himachal Pradesh, Jammu and Kashmir,
    //                      Jharkhand, Karnataka, Kerala, Lakshadweep, Madhya Pradesh, Maharashtra, Manipur, Meghalaya, Mizoram,
    //                      Nagaland, National Capital Territory of Delhi, Odisha, Puducherry, Punjab, Rajasthan, Sikkim, Tamil Nadu,
    //                      Telangana, Tripura, Uttar Pradesh, Uttarakhand, West Bengal,

    private String[] seasons; //Summer,Spring,Monsoon,Winter
    private String[] years; //2016,2017,2018,2019,2020

    public String getRole() {
        return role;
    }
    public void setRole(String role){
        this.role = role;
    }

    public String[] getSeasons() {
        return seasons;
    }
    public void setSeasons(String[] seasons){
        this.seasons = seasons;
    }

    public String getProductSpec(){ return productSpec; }
    public void setProductSpec(String productSpec){
        this.productSpec = productSpec;
    }

    public String getFilter(){ return filter; }
    public void setFilter(String filter){
        this.filter = filter;
    }

    public String[] getStates(){ return states; }
    public void setStates(String[] states){
        this.states = states;
    }
    public String[] getYears(){ return years; }
    public void setYears(String[] years){
        this.years = years;
    }

}

