function send_email() {

  let username = $('#username').val()
  if(!username) { return alert('required username via email'); }

  $('#spinner').show();
  $('#checker').hide();
  $('#btn_send').prop('disabled', true)
  $('#username').prop('disabled', true)

  console.log('request to email')
  $.ajax({
    async : true,
    url:'/auth/email/signup', // 요청 할 주소
    type:'POST', // GET, PUT
    data: JSON.stringify({ username : username }),
    contentType : 'application/json',
    dataType:'json',// xml, json, script, html
    beforeSend:function(x) {
      console.log(x)
    },// 서버 요청 전 호출 되는 함수 return false; 일 경우 요청 중단
    success:function(x) {
      $('#checker').show();
      $('#btn_send_text').text('Sent!')
      alert(x.data);
    },// 요청 완료 시
    error:function(x) {
      console.log(x)
      $('#username').prop('disabled', false)
      $('#btn_send_text').text('Resend Email')
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


function joinSubmit(){

  let pw1 = $("#pw").val();
  let pw2 = $("#pw2").val();
  let username = $("#username").val();
  let userEmail = $("#userEmail").val();
  console.log(userEmail);

  if(username.length == 0){
    alert("이름을 입력해주세요.");
    return false;
  }
  if(pw1.length == 0 || pw2.length == 0 ){
    alert("비밀번호를 입력해주세요.");
    return false;
  }

  if(pw1 != pw2){
    alert("비밀번호가 일치하지 않습니다.");
    return false;
  }
  if(username.length > 0 && pw1 == pw2 && pw1.length > 0 && pw2.length > 0 ){
    alert("회원가입이 완료되었습니다! 반갑습니다 "+username+"님 ~")
    return true;
  }

}

function chkPw(){
  let pw1 = $("#pw").val();
  let pw2 = $("#pw2").val();

  if(pw1 != pw2){
    $("#checkMessage").html("비밀번호가 일치하지 않습니다.");
  }else{
    $("#checkMessage").html("");
  }

}

