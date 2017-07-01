/**
 * Created by Sebarswee on 2015/9/2.
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
                    	msgBox.tips(data['content'], 1, function() {
		                    {
		                   		msgBox.exWindow.close(true);
		                    }
		                });
		             }
                });
            }
        });
       //下载绑定文件按钮
       $('#downloadBtn').on('click', function() {
        	window.location.href=path +'/'+userTypePath+'/patient/bind620/download?pId='+$('#pId').val();
	   });
         
	   $('#refreshBtn').on('click', function () {
            window.location.href=path +'/'+userTypePath+'/patient/bind620?pId='+$('#pId').val();
       });
       
       $('#cancelBtn').on('click', function () {
            msgBox.exWindow.close(null);
       });
    })
});