def newsPaths = ['news', 'feature', 'shapito']

def processNewsPage = { subUrl ->
    def page = loadResource(baseUrl + subUrl)
    def content = page.findFirst('div.Lead').text + "<br />" + page.findFirst('div.Body').text
    appendNews page.findFirst('h1').plain, content
}

loadResource(baseUrl).findAll('a.NewsTitle').each {
    def path = it.attr('href').split('/')[1]
    if (path in newsPaths) {
        processNewsPage it.attr('href')
    }
}

