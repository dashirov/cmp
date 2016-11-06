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
public class AcquisitionGuidance {
    @Id private String id;
    private String campaign;
    private Date acquisitionDate;
    private Long ltvOutlook;

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
            this.setId(campaign+"@"+df.format(this.acquisitionDate));
        }
    }
    public void setCampaign(String campaign) {
        this.campaign = campaign;
        if (this.getId() == null)
            generateId();
    }

    public Date getAcquisitionDate() {
        return acquisitionDate;

    }

    public void setAcquisitionDate(Date acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
        if (this.getId() == null)
            generateId();
    }

    public Long getLtvOutlook() {
        return ltvOutlook;
    }

    public void setLtvOutlook(Long ltvOutlook) {
        this.ltvOutlook = ltvOutlook;
    }
    
}
