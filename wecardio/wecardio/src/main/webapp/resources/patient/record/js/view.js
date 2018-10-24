/**
 * Created by tantian on 2015/7/25.
 */
seajs.use(['msgBox'], function (msgBox) {
    $(document).ready(function() {
        $.post(path + '/patient/record/pdf', { rid : $('#rid').val() }, function(data) {
            var pdf = data + '#view=Fit';
            var $container = $('#pdfContent');
            $container.append('<object width="100%" height="600" data="' + pdf + '" type="application/pdf">'
                + '<param name="src" value="' + pdf + '">'
                + '</object>');
        });

        $('#flagBtn').on('click', function() {
            msgBox.exWindow.open({
                title : '&nbsp;',
                width : '350px',
                height : '245px',
                url : path + '/patient/record/flag?ids=' + $('#rid').val(),
                close : function(flag) {
                    var $img = $('#flag').find('img');
                    switch (flag) {
                        case 0:
                            $img.hide();
                            break;
                        case 1:
                            $img.attr('src', path + '/resources/images/lan.png').show();
                            break;
                        case 2:
                            $img.attr('src', path + '/resources/images/huang.png').show();
                            break;
                        case 3:
                            $img.attr('src', path + '/resources/images/hong.png').show();
                            break;
                        default :
                            $img.hide();
                            break;
                    }
                }
            });
        });
        $('#downloadBtn').on('click', function () {
            window.location.href = path + '/patient/record/pdf/download?rid=' + $('#rid').val();
        });
        $('#createBtn').on('click', function () {
            window.location.href = path + '/patient/record/' + $('#rid').val() + '/choose';
        });
    });
});