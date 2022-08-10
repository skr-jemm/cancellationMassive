package mx.sekura.cancelationMassive.Entity;

import java.util.List;

/**
 * @author: Jorge Martinez Mohedano
 * @since: 2022-08-09
 * <p>
 */
public class WorkOrderResult {
    private Integer totalRecords;
    private List<WorkOrderResponse> workOrderResponseList;

    public WorkOrderResult() {
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<WorkOrderResponse> getWorkOrderList() {
        return workOrderResponseList;
    }

    public void setWorkOrderList(List<WorkOrderResponse> workOrderResponseList) {
        this.workOrderResponseList = workOrderResponseList;
    }

    public static class WorkOrderResponse {
        private String workOrderId;
        private String externalNumber;
        private Integer workOrderType;
        private String  executiveWo;
        private String  lineBusiness;
        private String  subLineBusiness;
        private String  customer;
        private String  insurer;
        private String  project;
        private String  areaId;
        private boolean haveOtEndorsementsActives;
        private String  styleToFavorite;
        private String  status;
        private boolean renewable;
        private String  startDate;
        private String  endDate;
        private Integer workOrderIdParent;
        private String customerBucket;

        public WorkOrderResponse() {
        }

        public String getWorkOrderId() {
            return workOrderId;
        }

        public void setWorkOrderId(String workOrderId) {
            this.workOrderId = workOrderId;
        }

        public String getExternalNumber() {
            return externalNumber;
        }

        public void setExternalNumber(String externalNumber) {
            this.externalNumber = externalNumber;
        }

        public Integer getWorkOrderType() {
            return workOrderType;
        }

        public void setWorkOrderType(Integer workOrderType) {
            this.workOrderType = workOrderType;
        }

        public String getExecutiveWo() {
            return executiveWo;
        }

        public void setExecutiveWo(String executiveWo) {
            this.executiveWo = executiveWo;
        }

        public String getLineBusiness() {
            return lineBusiness;
        }

        public void setLineBusiness(String lineBusiness) {
            this.lineBusiness = lineBusiness;
        }

        public String getSubLineBusiness() {
            return subLineBusiness;
        }

        public void setSubLineBusiness(String subLineBusiness) {
            this.subLineBusiness = subLineBusiness;
        }

        public String getCustomer() {
            return customer;
        }

        public void setCustomer(String customer) {
            this.customer = customer;
        }

        public String getInsurer() {
            return insurer;
        }

        public void setInsurer(String insurer) {
            this.insurer = insurer;
        }

        public String getProject() {
            return project;
        }

        public void setProject(String project) {
            this.project = project;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public boolean isHaveOtEndorsementsActives() {
            return haveOtEndorsementsActives;
        }

        public void setHaveOtEndorsementsActives(boolean haveOtEndorsementsActives) {
            this.haveOtEndorsementsActives = haveOtEndorsementsActives;
        }

        public String getStyleToFavorite() {
            return styleToFavorite;
        }

        public void setStyleToFavorite(String styleToFavorite) {
            this.styleToFavorite = styleToFavorite;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isRenewable() {
            return renewable;
        }

        public void setRenewable(boolean renewable) {
            this.renewable = renewable;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public Integer getWorkOrderIdParent() {
            return workOrderIdParent;
        }

        public void setWorkOrderIdParent(Integer workOrderIdParent) {
            this.workOrderIdParent = workOrderIdParent;
        }

        public String getCustomerBucket() {
            return customerBucket;
        }

        public void setCustomerBucket(String customerBucket) {
            this.customerBucket = customerBucket;
        }
    }
}
