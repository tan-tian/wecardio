/**
 * Created by tantian on 2016/1/21.
 */
seajs.use(['BaiduUploader', 'cookie' ,'msgBox'], function (BaiduUploader,msgBox) {
    $(document).ready(function() {
    	var $list = $('#thelist'),
        $btn = $('#ctlBtn'),
        state = 'pending',
        headUploader;
    	//文件上传控件
        headUploader = BaiduUploader.create({
            server : path + '/file/upload',
            formData : {
                fileType : 3,
                token : $.cookie("token")
            },
            pick : {
                id : '#pick',
                multiple:true
            },
            resize : false,
            fileNumLimit:4,
        });
        
		headUploader.on( 'fileQueued', function( file ) {
			$list.append( '<div id="' + file.id + '" class="item">' +
	       	 '<h4 class="info">' + file.name + '</h4>' +
	       	 '<p class="state">等待上传...</p>' +
	    	'</div>' );
		});
        headUploader.on( 'uploadProgress', function( file, percentage ) {
   			 var $li = $( '#'+file.id ),
     		   $percent = $li.find('.progress .progress-bar');

			    // 避免重复创建
			    if ( !$percent.length ) {
			        $percent = $('<div class="progress progress-striped active">' +
			          '<div class="progress-bar" role="progressbar" style="width: 0% ">' +
			          '</div>' +
			        '</div>').appendTo( $li ).find('.progress-bar');
			    }

			    $li.find('p.state').text('正在上传');
			
			    $percent.css( 'width', percentage * 100 + '%' );
		});
        
        headUploader.on('uploadSuccess', function (file, response) {
        	if (response.message.type=='warn'){
        		$( '#'+file.id ).find('p.state').css('color','red');
        	
        		$( '#'+file.id ).find('p.state').text
            	('上传文件格式错误');
            }
            else
            	$( '#'+file.id ).find('p.state').text
            	(JSON.stringify(response.message.content).replace('"','').replace('"',''));
        });
        
        headUploader.on( 'uploadError', function( file ) {
        $( '#'+file.id ).find('p.state').text('上传错误');
        });
        
        headUploader.on( 'all', function( type ) {
        if ( type === 'startUpload' ) {
         	   state = 'uploading';
        	} else if ( type === 'stopUpload' ) {
	        	    state = 'paused';
	        } else if ( type === 'uploadFinished' ) {
	            state = 'done';
	        }

        	 if ( state === 'uploading' ) {
         	  	 $btn.text('暂停上传');
       			 } else {
          	 	 $btn.text('开始上传');
       		 }
    	});
	
	    $btn.on( 'click', function() {
	        if ( state === 'uploading' ) {
	            headUploader.stop();
	        } else {
	            headUploader.upload();
	        }
	    });
	});
});