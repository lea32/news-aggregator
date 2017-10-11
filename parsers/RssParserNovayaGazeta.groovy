loadResource(baseUrl).findAll('item').each {
    def link = it.findFirst('link').text
    def description = it.findFirst('description').text
    def delimiter = !description.isEmpty() ? '<br />' : ''

    appendNews it.findFirst('title').plain, "$description$delimiter$link"
}