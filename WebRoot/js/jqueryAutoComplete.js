//����ȫ�ֱ���
var highlightindex = -1; 	//��ʾ�����Ľڵ�
var timeoutId;				//��ʾ�ӕr��������������ʱ��

/*Ajax �Զ���ȫ*/
//ע��ҳ��װ��ʱִ�еķ���
$(document).ready(function () {
	//�õ��ı������
	var wordInput = $("#indexKeyWords");

	//�õ��ı��������Ļ��߾���ϱߵľ���
	var wordInputOffset = wordInput.offset();
	console.warn( "nothing selected, can't validate, returning nothing" );

	//�Զ���ȫ���ʼ��������
	//�����ʽ�����ּ� css("position","absolute")����
	$("#auto").hide().css("border", "1px black solid").css("position", "absolute").css("top", wordInputOffset.top + wordInput.height() + 6 + "px").css("left", wordInputOffset.left + "px").width(wordInput.width() + 2);

	//���ı�����Ӽ��̰��²�������¼�
	$("#indexKeyWords").keyup(function (event) {
		//alert("keydown");
		//�����ı����еļ����¼�
		//�õ����������
		var autoNode = $("#auto");
		//�õ���ǰ������codeֵ
		var myEvent = event || window.evnet;
		var keyCode = myEvent.keyCode;
		
		//������������ĸ��Ӧ�ý��ı������µ���Ϣ���͸�������
		//�����������˸����ɾ������ҲӦ�ý��ı������Ϣ���͸�������
		if (keyCode >= 65 || (keyCode != 13 && keyCode != 38 && keyCode != 40 && keyCode <= 90) || keyCode == 8 || keyCode == 46) {
			//1�����Ȼ�ȡ�ı��������
			var wordText = $("#indexKeyWords").val();
			//�ı����ݲ�Ϊ�ղŽ��ı������ݷ���������
			if (wordText != "") {
				//2�����ı�������ݷ���������
				//���ϴ�δִ�е���ʱ���������
				var list = new Array();
				$.getJSON("AutomaticFillServlet", {word:wordText}, function(json){
					//alert("json parse");
			        //ѭ��ȡjson�е�����,���������б���
			        $.each(json, function(i){
			        	//��ȡ����
						var wordNode = json[i];
						if(wordNode != ""){
							list[i] = json[i];
						};
			        });
			    });

				clearTimeout(timeoutId);
				timeoutId = setTimeout(function(){
					//�½�div�ڵ�,���������ݼ��뵽�½��Ľڵ���
					//���½��Ľڵ���뵽������Ľڵ���
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
						//����������¼�,�����ڵ�
						newDivNode.mouseover(function(){
							//��ԭ�������Ľڵ�ȡ��
							if(highlightindex != -1){
								$("#auto").children("div").eq(highlightindex).css("background-color","white");
							}
							//��¼�µĸ����ڵ�
							highlightindex =  $(this).attr("id");
							$(this).css("background-color","#dddddd");
						});
						
						//����Ƴ���ȡ������
						newDivNode.mouseout(function(){
							//ȡ���ڵ�ĸ���
							$(this).css("background-color","white");
						});
						//��겹ȫ
						newDivNode.click(function(){											
							//�ı�������ݱ�ɸ�����ʾ������
							$("#indexKeyWords").val($(this).text());
							//���ص�������
							$("#auto").hide();
						});
					}); 
					//��������������������,����ʾ������
					if (list.length > 0) {
						//alert("show");
						autoNode.show();
					} else {
						//alert("hidden");
						autoNode.hide();
						//����������ʱû�и�����ʾ�Ľڵ�
						highlightindex = -1;
					}
				}, 500);
			}
			//alert("hello");
		} else if(keyCode == 38) {		//���ϼ�			
			//�õ�������������ӽڵ�
			//alert("keyup");
			var autoNodes = $("#auto").children("div");
			if(highlightindex != -1){
				//���ԭ�����ڸ�����ʾ�ڵ㣬�򽫱���ɫ��Ϊ��ɫ
				autoNodes.eq(highlightindex).css("background-color","white");
				//��highlightindex���������������ó�������
				if(highlightindex == 0){
					highlightindex = autoNodes.length - 1;
				}else{
					highlightindex--;
				}			
			}else{
				highlightindex = autoNodes.length - 1;
			}
			
			//�����ڸ��������ݱ�ɺ�ɫ
			autoNodes.eq(highlightindex).css("background-color","#dddddd");
		}else if(keyCode == 40){ 	//���¼�		
			//�õ�������������ӽڵ�
			var autoNodes = $("#auto").children("div");
			if(highlightindex != -1){
				//���ԭ�����ڸ�����ʾ�ڵ㣬�򽫱���ɫ��Ϊ��ɫ
				autoNodes.eq(highlightindex).css("background-color","white");
			}	
			highlightindex++;	
			
			if(highlightindex == autoNodes.length){
				highlightindex = 0;
			}
			//�����ڸ��������ݱ�ɺ�ɫ
			autoNodes.eq(highlightindex).css("background-color","#dddddd");
		}else if (keyCode == 13) {
			//���������ǻس�
			if(highlightindex != -1){
				//ȡ��������ʾ�����������
				var comText = $("#auto").hide().children("div").eq(highlightindex).text();
				//alert(comText);
				//�ı�������ݱ�ɸ�����ʾ������
				$("#indexKeyWords").val(comText);
				
				highlightindex = -1;
				
				//$("#submit").click();
			}else{
			  	var obj = $("#indexKeyWords");
			  	var count = obj.val();
				obj.val("");
				//alert(count + " has been submitted");
				
				//���ı���ʧȥ����
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
        if(!e) e = window.event;//������� window.event
        if((e.keyCode || e.which) == 13){
        	$("#submit").click();
        }
    };
	
	//����ť����¼�����ʾ�ı����е����ݱ��ύ
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
			
			//���²�ѯ��ʷ��¼
			//writeFile(historyRecordPath, txt);
		}else{
			alter("�������ѯ�ؼ���");
		}
	});
});