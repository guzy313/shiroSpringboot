let my = {};
if (typeof my == "undefined") {
    my = {};
}
(function ($) {
    my.showTab = function (tabName,url) {
        alert(url);
        $("#pageIframe").attr("src",url);
    }
})(jQuery);
