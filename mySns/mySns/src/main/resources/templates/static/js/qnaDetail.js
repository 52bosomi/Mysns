$(document).ready(() => {

    $('#delete').click(function (){

            let deleteFlag = confirm("삭제하시겠습니까?");
            if(deleteFlag){
            console.log("삭제");
                //글 삭제하는 api 타고 성공하면 리스트로
                 let jsonData = JSON.stringify({
                            idx: $('#board_idx').val()

                        });
                $.ajax({
                            url:"/qna/detailDelete",
                            type:"POST",
                            data:jsonData,
                            contentType:"application/json",
                            dataType:"json",
                            success:function(message){
                            alert(message.data);
                            location.href='/qna';
                            },
                            error:function(message){
                            alert(message.data);
                            }
                        });
            }
            console.log("삭제");
    });

    $('#update').click(function (){
            console.log("수정버튼");
            let board_idx = parseInt($('#board_idx').val());

//            let board_idx = $('#board_idx').val();
            console.log("수정글 idx"+typeof(board_idx));
            location.href="/qna/detailUpdateForm?idx="+board_idx;

        });


    $('#updateComplete').click(function (){
         console.log("수정 완료");
         let jsonData = JSON.stringify({
                            idx: $('#board_idx').val(),
                            title: $('#board_title').val(),
                            content: $('#board_content').val()
                        });
         console.log("수정게시물 idx:"+jsonData);
                $.ajax({
                         url:"/qna/detailUpdate",
                         type:"POST",
                         data:jsonData,
                         contentType:"application/json",
                         dataType:"json",
                         success:function(message){
                         alert(message.data);
                         location.href='/qna';//get,post 이슈때문에 그냥 리스트로 보냄
                         },
                         error:function(message){
                         alert(message.data);
                         }
                     });


    });

})

