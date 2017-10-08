package ru.leasoft.challenge.aggregator.persistence.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "news")
public class News {

    public static final int MIN_TITLE_LENGTH = 1;
    public static final int MAX_TITLE_LENGTH = 32000; //less than TEXT / 2
    public static final int MIN_LOCATION_LENGTH = 1;
    public static final int MAX_LOCATION_LENGTH = 8000000; //less than MEDIUMTEXT / 2

    private long id;
    @Size(min = MIN_TITLE_LENGTH, max = MAX_TITLE_LENGTH)
    private String title;
    @Size(min = MIN_LOCATION_LENGTH, max = MAX_LOCATION_LENGTH)
    private String content;
    private Date appendTimestamp;

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

    @Column(name = "APPEND_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    public Date getAppendTimestamp() {
        return appendTimestamp;
    }

    public void setAppendTimestamp(Date appendTimestamp) {
        this.appendTimestamp = appendTimestamp;
    }

    @ManyToOne(targetEntity = NewsSource.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "SOURCE_ID")
    public NewsSource getNewsSource() {
        return newsSource;
    }

    public void setNewsSource(NewsSource newsSource) {
        this.newsSource = newsSource;
    }

    @Transient
    public boolean isSameAs(News news) {
        boolean srcIsSame = this.newsSource == null ?
                news.newsSource == null :
                news.newsSource != null && this.newsSource.isSameAs(news.newsSource);
        return this.title.equals(news.title) && this.content.equals(news.content) && srcIsSame;
    }
}
