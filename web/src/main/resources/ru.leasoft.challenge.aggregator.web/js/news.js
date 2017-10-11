
var currentPage = 0;

go_next();

function go_next() {
    clearContainer();
    currentPage++;
    request();
}

function go_back() {
    clearContainer();
    currentPage--;
    if (currentPage <= 0) currentPage = 1;
    request();
}

function update() {
    clearContainer();
    request();
}

function request() {
    console.log('request');
    $.get(
        "/api/news/?page=" + currentPage,
        function (data) {
            onLoad(data);
        }
    )
}

function clearContainer() {
    $('#news').empty();
}

function onLoad(data) {
    var container = $('#news');

    if (data.length === 0) {
        $('<h3/>').text('Кажется, мы достигли дна ;)').appendTo(container);
        currentPage--;
        return;
    }

    for (i = 0; i < data.length; i++) {

        var newsItem = $('<div/>');
        $('<h3/>').addClass('news-header').text(data[i].title).appendTo(newsItem);
        $('<div/>').html(data[i].content).appendTo(newsItem);
        var footer = $('<div/>').appendTo(newsItem);
        $('<span/>').html(data[i].source).appendTo(footer);
        $('<span/>').text(data[i].receivingDate).appendTo(footer);

        newsItem.appendTo(container);
    }

    $('#pageNumber').text(currentPage);
}

