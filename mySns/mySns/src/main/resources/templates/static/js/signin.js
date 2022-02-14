function login() {

  let username = $('#username').val()
  let password = $('#password').val()
  if(!username) { alert('required username via email'); return false }
  if(!password) { alert('required password'); return false }

  // $('#spinner').show();
  // $('#checker').hide();
  // $('#btn_send').prop('disabled', true)
  // $('#username').prop('disabled', true)

  console.log('request to email')
  $.ajax({
    async : true,
    url:'/auth/login', // 요청 할 주소
    type:'POST', // GET, PUT
    data: JSON.stringify({ username : username, password : password }),
    contentType : 'application/json',
    dataType:'json',// xml, json, script, html
    beforeSend:function(x) {
      console.log(x)
    },// 서버 요청 전 호출 되는 함수 return false; 일 경우 요청 중단
    success:function(x) {
      $('#checker').show();

      if(x.isError) {
        alert(x.reason);
        return
      }

      alert(x.data)
      location.href = '/welcome?user_id='+username
    },// 요청 완료 시
    error:function(x) {
      console.log('failed', x)
      // $('#username').prop('disabled', false)
      alert(x.responseJSON.isError ? x.responseJSON.reason : x.responseJSON.data);
      // alert('마..ㅅX.... 발송 실패함');
    },// 요청 실패.
    complete:function(x) {
      console.log(x)
      // $('#btn_send').prop('disabled', false)
      // $('#spinner').hide();
    }// 요청의 실패, 성공과 상관 없이 완료 될 경우 호출
  });
}

$('#spinner').hide();
$('#checker').hide();


// for dev
if(location.hostname == 'localhost') {
  $('#username').val('hdh0926@naver.com')
  $('#password').val('hdh')
}
