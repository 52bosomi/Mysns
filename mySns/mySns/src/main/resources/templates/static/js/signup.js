function send_email() {

  let username = $('#username').val()
  if(!username) { return alert('required username via email'); }

  $('#spinner').show();
  $('#checker').hide();
  $('#btn_send').prop('disabled', true)
  

  console.log('request to email')
  $.ajax({
    async : true,
    url:'/auth/email/send', // 요청 할 주소
    type:'POST', // GET, PUT
    data: username,
    contentType : 'application/json',
    dataType:'json',// xml, json, script, html
    beforeSend:function(x) {
      console.log(x)
    },// 서버 요청 전 호출 되는 함수 return false; 일 경우 요청 중단
    success:function(x) {
      $('#checker').show();
      console.log()
      alert(x.data);
    },// 요청 완료 시
    error:function(x) {
      console.log(x)
      alert('마..ㅅX.... 발송 실패함');
    },// 요청 실패.
    complete:function(x) {
      $('#btn_send').prop('disabled', false)
      $('#spinner').hide();
    }// 요청의 실패, 성공과 상관 없이 완료 될 경우 호출
  });
}

$('#spinner').hide();
$('#checker').hide();