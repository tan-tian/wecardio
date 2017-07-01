/**
 * Created by Sebarswee on 2015/8/12.
 */
seajs.use(['msgBox'], function (msgBox) {
    $(document).ready(function() {
        // checkbox事件
        $('#checkAll').off('click').on('click', function () {
            $('[name=checkbox]:checkbox').prop('checked', this.checked);
        });
        $("[name=checkbox]:checkbox").on("click", function () {
            var $chk = $("[name=checkbox]:checkbox");
            $("#checkAll").prop("checked", $chk.length == $chk.filter(":checked").length);
        });

        $('#submitBtn').on('click', function () {
            $('#submitType').val('submit');
        });
        $('#cancelBtn').on('click', function () {
            $('#submitType').val('cancel');
        });

        var $form = $('#form');
        $form.submit(function() {
            if ($('#submitType').val() === 'submit') {
                var $chk = $("[name=checkbox]:checkbox");
                if ($chk.filter(":checked").length === 0) {
                    msgBox.warn(requiredMsg, null, null);
                    return false;
                }
                $chk.filter(":checked").each(function(i) {
                    var $this = $(this);
                    var year = $this.siblings('[name=year]:input').val();
                    var month = $this.siblings('[name=month]:input').val();
                    var money = $this.siblings('[name=money]:input').val();
                    $form.append(
                            '<input type="hidden" name="monthClears[' + i + '].year" value="' + year + '"/>' +
                            '<input type="hidden" name="monthClears[' + i + '].month" value="' + month + '"/>' +
                            '<input type="hidden" name="monthClears[' + i + '].applyMoney" value="' + money + '"/>'
                    );
                });
            }
        });
    });
});