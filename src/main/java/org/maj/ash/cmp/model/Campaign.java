package org.maj.ash.cmp.model;

import com.googlecode.objectify.annotation.Container;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import org.maj.ash.cmp.model.enums.CampaignStatus;
import org.maj.ash.cmp.model.enums.CampaignType;

import java.util.Date;

@Entity
public class Campaign implements Comparable<Campaign>{

	@Id private String code;
	private String description;
    private Long marketplace;  // where do we buy the traffc from
    private String product; // what product are we advertising
    private CampaignType type;


    @Container
    private ChangeLog<Double> priceChangeLog = new ChangeLog<>();

    @Container
    private ChangeLog<CampaignStatus> statusChangeLog=new ChangeLog<>();
	
	public Campaign() {
		super();
	}

    public CampaignType getType() {
        return type;
    }

    public void setType(CampaignType type) {
        this.type = type;
    }

    public Campaign(String code, String description) {
		super();
		this.code = code;
		this.description = description;
	}
	public void setPrice(Date effectiveDate, Double campaignPrice){
		this.priceChangeLog.addLogEntry(new ChangeLogEntry<>(effectiveDate,campaignPrice));
	}

	public void setStatus(Date effectiveDate, CampaignStatus campaignStatus){
        this.statusChangeLog.addLogEntry(new ChangeLogEntry<>(effectiveDate,campaignStatus));
    }

    public CampaignStatus getStatus(){
        return getStatus(new Date());
    }

    public CampaignStatus getStatus(Date date) {
        return this.statusChangeLog.getValue(date,CampaignStatus.NOT_FOUND);
    }

    public Double getPrice(){
        return getPrice(new Date());
    }

    public Double getPrice(Date date){
        /**
         * Price is more than zero if campaign is active on the same day
         */
        if (!getStatus(date).equals(CampaignStatus.ACTIVE)){
            return 0.0;
        }
        /**
         * Now that we know it was active, find the price or default to $0.00
         */
        return priceChangeLog.getValue(date,0.00);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getMarketplace() {
        return marketplace;
    }

    public void setMarketplace(Long marketplace) {
        this.marketplace = marketplace;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public ChangeLog<Double> getPriceChangeLog() {
        return priceChangeLog;
    }

    public void setPriceChangeLog(ChangeLog<Double> priceChangeLog) {
        this.priceChangeLog = priceChangeLog;
    }

    public ChangeLog<CampaignStatus> getStatusChangeLog() {
        return statusChangeLog;
    }

    public void setStatusChangeLog(ChangeLog<CampaignStatus> statusChangeLog) {
        this.statusChangeLog = statusChangeLog;
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", marketplace=" + marketplace +
                ", product='" + product + '\'' +

                ", price=" + this.getPrice(new Date()).toString() +
                ", priceChangeLog=" + this.priceChangeLog.toString()+

                ", status=" + this.getStatus(new Date()).toString() +
                ", statusChangeLog=" + this.statusChangeLog.toString() +
                '}';
    }

    @Override
    public int compareTo(Campaign o) {
        return this.getCode().compareTo(o.getCode());
    }
}
