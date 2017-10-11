package ru.leasoft.challenge.aggregator.persistence.entities;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilterFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Parameter;
import ru.leasoft.challenge.aggregator.web.dto.NewsDto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@Table(name = "news")
@Indexed
@AnalyzerDefs({
        @AnalyzerDef(name = "ruAnalyzer",
                tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
                filters = {
                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                        @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
                                @Parameter(name = "language", value = "Russian")
                        })
                }),

        @AnalyzerDef(name = "suggestionNGram",
                tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
                filters = {
                        @TokenFilterDef(factory = WordDelimiterFilterFactory.class),
                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                        @TokenFilterDef(factory = NGramFilterFactory.class, params = {
                                @Parameter(name = "minGramSize", value = "3"),
                                @Parameter(name = "maxGramSize", value = "5") })
                }

        )
})
public class News {

    public static final int MIN_TITLE_LENGTH = 1;
    public static final int MAX_TITLE_LENGTH = 32000; //less than TEXT / 2
    public static final int MIN_CONTENT_LENGTH = 1;
    public static final int MAX_CONTENT_LENGTH = 8000000; //less than MEDIUMTEXT / 2

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

    private long id;
    @Size(min = MIN_TITLE_LENGTH, max = MAX_TITLE_LENGTH)
    private String title;
    @Size(min = MIN_CONTENT_LENGTH, max = MAX_CONTENT_LENGTH)
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
    @Fields({
            @Field(name = "search", index = Index.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "ruAnalyzer")),
            @Field(name = "suggestion", index = Index.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "suggestionNGram"))
    })
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

    @Transient
    public static NewsDto packToDto(News news) {
        NewsDto dto = new NewsDto();
        dto.setTitle(news.title);
        dto.setContent(news.content);
        dto.setSource(news.newsSource.toString());
        dto.setReceivingDate(LocalDateTime.ofInstant(news.appendTimestamp.toInstant(), ZoneId.systemDefault()).format(formatter));
        return dto;
    }
}
