package org.maj.ash.cmp.model;

/**
 * Created by dashirov on 10/16/16.
 *
 * AppNexus, Google, Yahoo! Gemini, Facebook, Instagram are marketplaces where you place a purchase order,
 *  insertion order and spend money on user acquisition
 */
import com.googlecode.objectify.annotation.Container;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import org.maj.ash.cmp.model.enums.MarketplaceStatus;

import java.util.Date;


@Entity
public class Marketplace implements Comparable<Marketplace> {
    @Id private Long id;
    private String name;
    @Container
    private ChangeLog<MarketplaceStatus> statusChangeLog=new ChangeLog<>();

    public void setStatus(Date effectiveDate, MarketplaceStatus marketplaceStatus){
        this.statusChangeLog.addLogEntry(new ChangeLogEntry<>(effectiveDate,marketplaceStatus));
    }

    public void setStatus(MarketplaceStatus marketplaceStatus){
        // default effective date is NOW()
        setStatus(new Date(), marketplaceStatus);
    }

    public MarketplaceStatus getStatus(){
        return getStatus(new Date());
    }

    public MarketplaceStatus getStatus(Date date) {
        return this.statusChangeLog.getValue(date,MarketplaceStatus.TERMINATED);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Marketplace{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(Marketplace o) {
        return this.getName().compareTo(o.getName());
    }
}
