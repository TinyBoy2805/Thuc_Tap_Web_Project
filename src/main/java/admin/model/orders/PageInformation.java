package admin.model.orders;

import java.util.List;

public class PageInformation<T>{
    private List<T> data; //danh sách sau khi query
    private int pageIndex; //số trang đang được hiển thị
    private int pageSize; //kích thước hiển thị 1 trang
    private int totalItems; //tổng số lợng của 1 query
    private int totalPage; //tổng số trang

    public PageInformation() {}

    public PageInformation(List<T> data, int pageIndex, int pageSize, int totalItems, int totalPage) {
        this.data = data;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPage = totalPage;
    }

    public List<T> getData() { return data; }
    public void setData(List<T> data) { this.data = data; }

    public int getPageIndex() { return pageIndex; }
    public void setPageIndex(int pageIndex) { this.pageIndex = pageIndex; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }

    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }

    public int getTotalPage() { return totalPage; }
    public void setTotalPage(int totalPage) { this.totalPage = totalPage; }

    @Override
    public String toString() {
        return "PageInformation{" +
                "data=" + data +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                ", totalItems=" + totalItems +
                ", totalPage=" + totalPage +
                '}';
    }
}
