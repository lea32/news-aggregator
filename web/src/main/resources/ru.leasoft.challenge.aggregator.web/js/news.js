
$(function () {
    var suggestionsBox = $('#autocomplete');

    suggestionsBox.autocomplete({
        serviceUrl: '/api/suggest',
        paramName: 'propose',
        minChars: 3,
        onSelect: function (suggestion) {
            query.queryString = suggestion.value;
            query.exactly = true;
            update();
        }
    });

    suggestionsBox.change(function() {
        query.queryString = suggestionsBox.val();
        query.exactly = false;
        update();
    });
});

var currentPage = 0;
var query = {
    queryString : '',
    exactly : false
};
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
    var request = (query.queryString.length > 0) ?
        "/api/search?query=" + query.queryStringПутин
        + "&page=" + currentPage :
        "/api/news/?page=" + currentPage;

    $.get(
        request,
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

    var to = (query.exactly) ? 1 : data.length;
    for (i = 0; i < to; i++) {

        var newsItem = $('<div/>');
        $('<h3/>').addClass('news-header').text(data[i].title).appendTo(newsItem);
        $('<div/>').html(data[i].content).appendTo(newsItem);
        var footer = $('<div/>').appendTo(newsItem);
        footer.addClass('news-footer');
        $('<div/>').addClass('from').html(data[i].source).appendTo(footer);
        $('<div/>').addClass('when').text(data[i].receivingDate).appendTo(footer);

        newsItem.appendTo(container);
    }

    $('#pageNumber').text(currentPage);
}

