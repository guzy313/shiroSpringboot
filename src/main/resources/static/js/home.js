let my = {};
if (typeof my == "undefined") {
    my = {};
}
(function ($) {
    my.showTab = function (tabName,url) {
        $("#pageIframe").attr("src",url);
    }
})(jQuery);
