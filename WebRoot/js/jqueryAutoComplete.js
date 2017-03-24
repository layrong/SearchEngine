//定义全局变量
var highlightindex = -1; 	//表示高亮的节点
var timeoutId;				//表示延時向服务器发送请的时间

/*Ajax 自动补全*/
//注册页面装在时执行的方法
$(document).ready(function () {
	//得到文本框对象
	var wordInput = $("#indexKeyWords");

	//得到文本框距离屏幕左边距和上边的距离
	var wordInputOffset = wordInput.offset();
	console.warn( "nothing selected, can't validate, returning nothing" );

	//自动补全框最开始隐藏起来
	//添加样式必须现价 css("position","absolute")属性
	$("#auto").hide().css("border", "1px black solid").css("position", "absolute").css("top", wordInputOffset.top + wordInput.height() + 6 + "px").css("left", wordInputOffset.left + "px").width(wordInput.width() + 2);

	//给文本框添加键盘按下并弹起的事件
	$("#indexKeyWords").keyup(function (event) {
		//alert("keydown");
		//处理文本框中的键盘事件
		//得到弹出框对象
		var autoNode = $("#auto");
		//得到当前按键的code值
		var myEvent = event || window.evnet;
		var keyCode = myEvent.keyCode;
		
		//如果输入的是字母，应该将文本框最新的信息发送给服务器
		//如果输入的是退格键或删除键，也应该将文本框的信息发送给服务器
		if (keyCode >= 65 || (keyCode != 13 && keyCode != 38 && keyCode != 40 && keyCode <= 90) || keyCode == 8 || keyCode == 46) {
			//1、首先获取文本框的内容
			var wordText = $("#indexKeyWords").val();
			//文本内容不为空才将文本框内容发给服务器
			if (wordText != "") {
				//2、将文本框的内容发给服务器
				//对上次未执行的延时做清除操作
				var list = new Array();
				$.getJSON("AutomaticFillServlet", {word:wordText}, function(json){
					//alert("json parse");
			        //循环取json中的数据,并呈现在列表中
			        $.each(json, function(i){
			        	//获取单词
						var wordNode = json[i];
						if(wordNode != ""){
							list[i] = json[i];
						};
			        });
			    });

				clearTimeout(timeoutId);
				timeoutId = setTimeout(function(){
					//新建div节点,将单词内容加入到新建的节点中
					//将新建的节点加入到弹出框的节点中
					autoNode.html("");
					$.each(list,function(n,value) {  
						//alert(value);
						var newDivNode=$('<div></div>'); 
						newDivNode.css({
							"height":"25px",
						});
						newDivNode.css("backgroundColor","white");
						newDivNode.html(value).appendTo(autoNode);
						autoNode.show();
						//添加鼠标进入事件,高亮节点
						newDivNode.mouseover(function(){
							//将原来高亮的节点取消
							if(highlightindex != -1){
								$("#auto").children("div").eq(highlightindex).css("background-color","white");
							}
							//记录新的高亮节点
							highlightindex =  $(this).attr("id");
							$(this).css("background-color","#dddddd");
						});
						
						//鼠标移出，取消高亮
						newDivNode.mouseout(function(){
							//取消节点的高亮
							$(this).css("background-color","white");
						});
						//鼠标补全
						newDivNode.click(function(){											
							//文本框的内容变成高亮显示的内容
							$("#indexKeyWords").val($(this).text());
							//隐藏弹出窗体
							$("#auto").hide();
						});
					}); 
					//如果服务服务器端有数据,则显示弹出框
					if (list.length > 0) {
						//alert("show");
						autoNode.show();
					} else {
						//alert("hidden");
						autoNode.hide();
						//弹出框隐藏时没有高亮显示的节点
						highlightindex = -1;
					}
				}, 500);
			}
			//alert("hello");
		} else if(keyCode == 38) {		//向上键			
			//得到弹出框的所有子节点
			//alert("keyup");
			var autoNodes = $("#auto").children("div");
			if(highlightindex != -1){
				//如果原来存在高亮显示节点，则将背景色改为白色
				autoNodes.eq(highlightindex).css("background-color","white");
				//将highlightindex等于零的情况单独拿出来处理
				if(highlightindex == 0){
					highlightindex = autoNodes.length - 1;
				}else{
					highlightindex--;
				}			
			}else{
				highlightindex = autoNodes.length - 1;
			}
			
			//让现在高亮的内容变成红色
			autoNodes.eq(highlightindex).css("background-color","#dddddd");
		}else if(keyCode == 40){ 	//向下键		
			//得到弹出框的所有子节点
			var autoNodes = $("#auto").children("div");
			if(highlightindex != -1){
				//如果原来存在高亮显示节点，则将背景色改为白色
				autoNodes.eq(highlightindex).css("background-color","white");
			}	
			highlightindex++;	
			
			if(highlightindex == autoNodes.length){
				highlightindex = 0;
			}
			//让现在高亮的内容变成红色
			autoNodes.eq(highlightindex).css("background-color","#dddddd");
		}else if (keyCode == 13) {
			//如果输入的是回车
			if(highlightindex != -1){
				//取出高亮显示下拉框的内容
				var comText = $("#auto").hide().children("div").eq(highlightindex).text();
				//alert(comText);
				//文本框的内容变成高亮显示的内容
				$("#indexKeyWords").val(comText);
				
				highlightindex = -1;
				
				//$("#submit").click();
			}else{
			  	var obj = $("#indexKeyWords");
			  	var count = obj.val();
				obj.val("");
				//alert(count + " has been submitted");
				
				//让文本框失去焦点
				obj.get(0).blur();
			}		
		}
	});
	
	$(".radio").click(function(){ 
        //alert("radio");
		$("#submit").click();
		//alert("radio");
    }); 
	
	document.onkeydown = function(e){
        if(!e) e = window.event;//火狐中是 window.event
        if((e.keyCode || e.which) == 13){
        	$("#submit").click();
        }
    };
	
	//给按钮添加事件，表示文本框中的数据被提交
	$("input[type='button']").click(function () {
		//$.post("./SearchServlet", {action:"initial"}, {pageType:"first"}, {indexKeyWords:wordInput});
		var txt = document.getElementById("indexKeyWords").value;
		if(txt){
			var tmp = document.createElement("form");
			var sortIndex = $('input[name="sortIndex"]:checked').val();
			if(!sortIndex){
				sortIndex = 0;
			}
			var action = "./SearchServlet?action=initial&pageType=first&indexKeyWords="+txt+"&sortIndex="+sortIndex;
			post = encodeURI(action);
			post = encodeURI(action);
			tmp.action = action;
			tmp.method = "post";
			document.body.appendChild(tmp);
			tmp.submit();
			
			//$('input[name="sortIndex"][value=1]').attr("checked", "true");
			
			//更新查询历史记录
			//writeFile(historyRecordPath, txt);
		}else{
			alter("请输入查询关键字");
		}
	});
});