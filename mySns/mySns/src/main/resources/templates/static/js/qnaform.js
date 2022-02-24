$(document).ready(() => {

$('#insert').click(function (){
       /* let titles =  $('#board_title').val();
        console.log(titles+"제목");
        let contents =  $('#board_content').val();
        console.log("////"+contents);
        if(titles){
         alert("제목을 입력해주세요.");
         return false;
        }
        if(contents==""){
         alert("내용을 입력해주세요.");
         return false;
        }*/
        alert("저장");
        let jsonData = JSON.stringify({
            username: $('#board_username').val(),
            title: $('#board_title').val(),
            content:$('#board_content').val()
        });



        $.ajax({
            url:"/qna/writeForm",
            type:"POST",
            data:jsonData,
            contentType:"application/json",
            dataType:"json",
            success:function(){
            //alert("저장성공");
            location.href='/qna';
            },
            error:function(){
            alert("저장실패");
            }
        });


    });

})


