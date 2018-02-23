(function ($) {
    $.ajaxSetup({
        complete: function (xhr, ts) {
            // console.log(xhr, ts);
            if (xhr.status == 401) {
                top.location = "login.html";
            }
        }
    });
})(jQuery);