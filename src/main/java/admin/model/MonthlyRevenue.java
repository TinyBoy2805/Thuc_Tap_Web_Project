package admin.model;

public class MonthlyRevenue {
    private int month;
    private long monthlyRevenue;

    public MonthlyRevenue(int month, long monthlyRevenue) {
        this.month = month;
        this.monthlyRevenue = monthlyRevenue;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MonthlyRevenue{");
        sb.append("monthNumber=").append(month);
        sb.append(", monthlyRevenue=").append(monthlyRevenue);
        sb.append('}');
        return sb.toString();
    }

    public MonthlyRevenue(){}

    public int getMonthNumber() { return month; }
    public long getMonthlyRevenue() { return monthlyRevenue; }

    public void setMonthNumber(int month) {
        this.month = month;
    }

    public void setMonthlyRevenue(long monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }
}
