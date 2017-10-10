loadResource(baseUrl).findAll('.//item').each {
    appendNews it.findFirst('.//title').text, it.findFirst('.//description').text
}