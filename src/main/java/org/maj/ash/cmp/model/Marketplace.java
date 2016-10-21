package org.maj.ash.cmp.model;

/**
 * Created by dashirov on 10/16/16.
 *
 * AppNexus, Google, Yahoo! Gemini, Facebook, Instagram are marketplaces where you place a purchase order,
 *  insertion order and spend money on user acquisition
 */
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


@Entity
public class Marketplace {
    @Id private Long id;
    private String name;

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
}
