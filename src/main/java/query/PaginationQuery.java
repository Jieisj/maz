package query;
import lombok.Getter;
import lombok.Setter;

public class PaginationQuery {
    @Getter
    private Integer page;
    @Getter
    @Setter
    private Integer numPerPage;
    @Getter
    private long offset = 0;
    @Getter
    @Setter
    private String orderBy;
    @Getter
    private boolean asc;
    @Getter
    private boolean desc;
    @Getter
    private String order = "asc";

    public void setPage(Integer page) {
        Integer numPerPage = getNumPerPage();
        setOffSet((long) numPerPage * page - numPerPage);
        this.page = page;
    }

    public void setAsc(boolean asc) {
        if (asc){
            setOrder("asc");
        }
        this.asc = asc;
    }

    private void setOrder(String order) {
        this.order = order;
    }

    public void setDesc(boolean desc){
        if(desc){
            setOrder("desc");
        }
        this.desc = desc;
    }

    private void setOffSet(long offSet) {
        this.offset = offset;
    }
}