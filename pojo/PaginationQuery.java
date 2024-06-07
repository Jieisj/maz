package pojo;
public class RootQuery {
    private Integer page;
    private Integer numPerPage;
    private long offSet = 0;
    private String orderBy;
    private boolean asc;
    private boolean desc;
    private String order = "asc";

    public void setPage(Integer page) {
        Integer numPerPage = getNumPerPage();
        setOffSet((long) numPerPage * page - numPerPage);
        this.page = page;
    }

    public Integer getNumPerPage() {
        return numPerPage;
    }

    public void setNumPerPage(Integer numPerPage) {
        this.numPerPage = numPerPage;
    }

    private void setOffSet(long offSet) {
        this.offSet = offSet;
    }

    public long getOffSet(){
        return this.offset;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
         this.orderBy = orderBy;
    }

    public String getOrder(){
        return this.order;
    }

    private void setOrder(String order) {
        this.order = order;
    }

    public void setAsc(boolean asc) {
        if (asc){
            setOrder("asc");
        }
        this.asc = asc;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setDesc(boolean desc){
        if(desc){
            setOrder("desc");
        }
        this.desc = desc;
    }

    public boolean isDesc() {
        return asc;
    }
}
