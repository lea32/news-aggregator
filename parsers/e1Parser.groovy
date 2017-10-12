
loadResource(baseUrl).findAll('a.m-article-box__content').each {

    def subLink = it.attr('href')
    def link = subLink.startsWith('http') ? subLink : "$baseUrl$subLink"

    def page = loadResource(link)

    appendNews page.findFirst('h1.m-box-news__content-title').plain, page.findFirst('div.m-box-news__content-text').text
}