package user.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class PaginatedResult<T> {
    private List<T> data;
    private int pageIndex;
    private int pageSize;
    private int totalItems;
    private int totalPage;

    public PaginatedResult() {}

    public PaginatedResult(List<T> data, int pageIndex, int pageSize, int totalItems) {
        this.data = data;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPage = (int) Math.ceil((double) totalItems / pageSize);
    }
}
