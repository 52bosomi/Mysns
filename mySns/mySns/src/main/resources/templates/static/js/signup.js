function send_email() {

  let username = $('#username').val()
  if(!username) { alert('required username via email'); return false }

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
    success:function(x) {
      $('#checker').show();
      $('#btn_send_text').text('Sent!')
      alert(x.isError ? x.reason : x.data);

      // location.href = '/login'
    },// 요청 완료 시
    error:function(x) {
      console.log('failed', x)
      $('#username').prop('disabled', false)
      $('#btn_send_text').text('Resend Email')
      alert(x.responseJSON.isError ? x.responseJSON.reason : x.responseJSON.data);
      // alert('마..ㅅX.... 발송 실패함');
    },// 요청 실패.
    complete:function(x) {
      $('#btn_send').prop('disabled', false)
      $('#spinner').hide();
    }// 요청의 실패, 성공과 상관 없이 완료 될 경우 호출
  });
}

$('#spinner').hide();
$('#checker').hide();


function send_join(){

  let name = $("#name").val();
  let phone = $("#phone").val();
  let username = $("#username").val();
  let password = $("#password").val();
  let password_repeat = $("#password_repeat").val();

  if(!name) { return alert('이름을 입력해주세요') }
  if(!phone) { return alert('전화번호를 입력해주세요') }
  if(!password) { return alert('비밀번호를 입력해주세요') }
  if(!password_repeat) { return alert('비밀번호 확인을 입력해주세요') }
  if(password != password_repeat) { return alert('비밀번호가 서로 일치하지 않습니다') }
  $('#btn_send').prop('disabled', true)

  console.log('request to signup')

  let body = { username : username, password : password, phone : phone, name : name }

  try { body.token = getUrlVars()['token'] } catch(e) { console.log(e) }

  // 보내기 전 패스워드 암호화 필요
  // body.password = body.password

  $.ajax({
    async : true,
    url:'/auth/email/join', // 요청 할 주소
    type:'POST', // GET, PUT
    data: JSON.stringify(body),
    contentType : 'application/json',
    dataType:'json',// xml, json, script, html
    success : function(x) {
      $('#checker').show();
      $('#btn_send_text').text('Signup Successful')

      alert(x.isError ? x.reason : x.data);

      location.href = '/login'
    },// 요청 완료 시
    error:function(x) {
      console.log('failed', x)
      $('#username').prop('disabled', false)
      $('#btn_send_text').text('Signup Failed')

      alert(x.responseJSON.isError ? x.responseJSON.reason : x.responseJSON.data);
    },// 요청 실패.
    complete:function(x) {
      $('#btn_send').prop('disabled', false)
      $('#spinner').hide();
    }// 요청의 실패, 성공과 상관 없이 완료 될 경우 호출
  });

}

function getUrlVars()
{
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++)
    {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}