package ru.leasoft.challenge.aggregator.persistence.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "newssrc")
public class NewsSource {

    public static final int MIN_NAME_LENGTH = 1;
    public static final int MAX_NAME_LENGTH = 255;
    public static final int MIN_LOCATION_LENGTH = 1;
    public static final int MAX_LOCATION_LENGTH = 2048;


    private long id;

    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    private String name;

    @Size(min = MIN_LOCATION_LENGTH, max = MAX_LOCATION_LENGTH)
    private String location;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "LOCATION")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Transient
    public boolean isSameAs(NewsSource newsSource) {
        return this.name.equals(newsSource.name) && this.location.equals(newsSource.location);
    }

    @Override
    public String toString() {
        return name + " (" + location + ")";
    }
}
