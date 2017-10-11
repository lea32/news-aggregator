loadResource(baseUrl).findAll('item').each {
    appendNews it.findFirst('title').plain, it.findFirst('description').text
}