$(document).ready(() => {
	console.log("sss")

  $.noConflict();
     var _$ = jQuery //식별자 변경

     _$('#qnaTable').DataTable().destroy();
	 _$('#qnaTable').DataTable({

                          pageLength: 10, // 한페이지에 보여주는 데이터 개수

                            ajax : {
                                url:"/qna/qnalist",
                                type:"POST",
                                dataType: "json",
                                contentType: "application/json",
                                dataSrc : "data"

                            },
                            columns : [
                                {data: "idx"},
                                {data: "title",
                                render:function(title,data,idx){
                                console.log(title,data,idx.idx,idx.userId);

                                /*String 인데 ''으로 안감싸져있었음, 가능한한 들어오는 값 확인*/
                                let userid = "'"+idx.userId+"'";
                                data='<a href="#" onclick="trans('+idx.idx+','+userid+');">'+title+'</a>';
                                return data;
                                }
                                },
                                {data: "username"},
                                {data: "created_at"},
                                {data: "disclosure"}
                            ]
                        });

	})
    function trans(idx,userId){
                console.log("이동"+idx);
                console.log("이동"+userId);
                //location.href="/qna/qnaDetail?idx="+idx;
                let form = document.createElement("form");
               /* form.setAttribute("method","POST");
                form.setAttribute("action","/qna/qnaDetail");*/

                form.action = "./qna/detail";
                form.method = "POST";

                let Field = document.createElement("input");
                Field.setAttribute("type","hidden");
                Field.setAttribute("name","data");
                Field.setAttribute("value",idx);

                let user = document.createElement("input");
                user.setAttribute("type","hidden");
                user.setAttribute("name","userId");
                user.setAttribute("value",userId);


                form.appendChild(Field);
                form.appendChild(user);
                document.body.appendChild(form);
                form.submit();

                }