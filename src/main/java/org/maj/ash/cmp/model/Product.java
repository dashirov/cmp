package org.maj.ash.cmp.model;

import com.googlecode.objectify.annotation.Container;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import org.maj.ash.cmp.model.enums.ProductStatus;

import java.util.*;


@Entity
public class Product implements Comparable<Product>{

	@Id private String code;
	private String name;
	private String description;
	private Long   parentAccount;

    @Container
	private ChangeLog<ProductStatus> statusChangeLog = new ChangeLog<ProductStatus>();

    /**
     * List of campaigns assosiated with this product. Campaigns always move together with the product within
     * account hierarchy.
     */
    @Container
    private SortedSet<String> campaigns = new TreeSet<>();

    /**
     * Campaigns are added by campaignCode
     * @param campaignCode
     */
    public void addCampaign(String campaignCode){
        campaigns.add(campaignCode);
    }

    /**
     * Campaigns are removed by campaignCode
     * @param campaignCode
     */
    public void removeCampaign(String campaignCode){
        campaigns.remove(campaignCode);
    }

    public SortedSet<String> getCampaigns(){
        return campaigns;
    }

    public void setCampaigns(SortedSet<String> campaigns) {
        this.campaigns = campaigns;
    }

    public Long getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(Long parentAccount) {
        this.parentAccount = parentAccount;
    }

    public Product() {
		super();
	}

	public Product(String code, String name, String description) {
		super();
		this.code = code;
        this.name = name;
		this.description = description;
	}

	public void setStatus(final Date effectiveDate, ProductStatus status){
        ChangeLogEntry<ProductStatus>  entry= new ChangeLogEntry<ProductStatus>(effectiveDate,status);
        this.statusChangeLog.addLogEntry(entry);
    }

    public ProductStatus getStatus(final Date date){
        return this.statusChangeLog.getValue(date,ProductStatus.NONEXISTING);
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code.toUpperCase();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ChangeLog<ProductStatus> getStatusChangeLog() {
		return statusChangeLog;
	}

	public void setStatusChangeLog(ChangeLog<ProductStatus> statusChangeLog) {
		this.statusChangeLog = statusChangeLog;
	}

	@Override
	public String toString() {
		return "Product [code=" + code + ", name=" + name+ ", description=" + description + ", status=" + this.getStatus(new Date()).toString() + "]";
	}

    @Override
    public int compareTo(Product p) {
        return this.getCode().compareTo(p.getCode());
    }
}
