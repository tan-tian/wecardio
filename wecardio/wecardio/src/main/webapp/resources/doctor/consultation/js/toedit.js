/**
 * Created by tantian on 2015/9/2.
 */
seajs.use(['msgBox', 'validate'], function (msgBox) {
    $(document).ready(function () {
        var $form = $('#form');
        $form.validate({
            rules: {
                
            },
            submitHandler: function (form) {
                var $form = $(form);
                $.ajax({
                    url: $form.attr("action"),
                    type: "POST",
                    data: $form.serialize(),
                    dataType: "json",
                    cache: false,
                    success: function(data) {
                    	msgBox.tips(data['msg']['content'], 1, function() {
		                    {
		                   		msgBox.exWindow.close(true);
		                    }
		                });
		             }
                });
            }
        });
        
        $('#submitBtn').on('click', function() {
        	$('#form').submit();
		});
         
		
       $('#cancelBtn').on('click', function () {
            msgBox.exWindow.close(null);
       });
    })
});