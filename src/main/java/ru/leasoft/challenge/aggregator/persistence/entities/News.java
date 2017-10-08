package ru.leasoft.challenge.aggregator.persistence.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "news")
public class News {

    private long id;
    private String title;
    private String content;
    private Date appendDate;

    @NotNull
    private NewsSource newsSource;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "TITLE")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "CONTENT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "APPEND_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    public Date getAppendDate() {
        return appendDate;
    }

    public void setAppendDate(Date appendDate) {
        this.appendDate = appendDate;
    }

    @ManyToOne(targetEntity = NewsSource.class, optional = false, fetch = FetchType.LAZY)
    public NewsSource getNewsSource() {
        return newsSource;
    }

    public void setNewsSource(NewsSource newsSource) {
        this.newsSource = newsSource;
    }
}
