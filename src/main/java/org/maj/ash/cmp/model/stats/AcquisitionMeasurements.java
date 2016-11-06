package org.maj.ash.cmp.model.stats;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dashirov on 10/18/16.
 */
@Entity
public class AcquisitionMeasurements {
    @Id private String id;
    private String campaign;
    private Date acquisitionDate;
    private Long totalDownloads;
    private Long paidDownloads;
    private Double spend;

    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCampaign() {
        return campaign;
    }

    public void generateId(){
        if (this.campaign != null && this.acquisitionDate != null){
            this.id=campaign+"@"+df.format(this.acquisitionDate);
        }
    }
    public void setCampaign(String campaign) {
        this.campaign = campaign;
        if (this.id == null)
            generateId();
    }

    public Date getAcquisitionDate() {
        return acquisitionDate;

    }

    public void setAcquisitionDate(Date acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
        if (this.id == null)
            generateId();
    }

    public Long getTotalDownloads() {
        return totalDownloads;
    }

    public void setTotalDownloads(Long totalDownloads) {
        this.totalDownloads = totalDownloads;
    }

    public Long getPaidDownloads() {
        return paidDownloads;
    }

    public void setPaidDownloads(Long paidDownloads) {
        this.paidDownloads = paidDownloads;
    }

    public Double getSpend() {
        return spend;
    }

    public void setSpend(Double spend) {
        this.spend = spend;
    }
}
