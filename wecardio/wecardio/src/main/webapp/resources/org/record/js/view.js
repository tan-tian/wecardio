/**
 * Created by Sebarswee on 2015/7/25.
 */
seajs.use([], function () {
    $(document).ready(function() {
        $.post(path + '/org/record/pdf', { rid : $('#rid').val() }, function(data) {
            var pdf = data + '#view=Fit';
            var $container = $('#pdfContent');
            $container.append('<object width="100%" height="600" data="' + pdf + '" type="application/pdf">'
                + '<param name="src" value="' + pdf + '">'
                + '</object>');
        });
        $('#downloadBtn').on('click', function () {
            window.location.href = path + '/org/record/pdf/download?rid=' + $('#rid').val();
        });
    });
});