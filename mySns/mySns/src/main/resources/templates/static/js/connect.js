this.step = 0

$(document).ready(function() {

  function next() {
    console.log(this.step, "thislaskjdlkajsl")
    // this.step = step
    

    switch (this.step) {
      case 0:
        // facebook
        $('.step_0').hide()
        $('.step_1').show()

        $('#username').val('hdh0926@naver.com')
        $('#password').val('hdh')
        this.step = 1
        break;
      case 1:
        $('.step_1').hide()
        this.step = 2

        $(".loading").show()
      default:
        break;
    }

    // console.log('request to email')
    // $.ajax({
    //   async : true,
    //   url:'/auth/login', // 요청 할 주소
    //   type:'POST', // GET, PUT
    //   data: JSON.stringify({ username : username, password : password }),
    //   contentType : 'application/json',
    //   dataType:'json',// xml, json, script, html
    //   success:function(x) {
    //     $('#checker').show();
    //     alert(x.isError ? x.reason : x.data);
    //     location.href = '/welcome'
    //   },// 요청 완료 시
    //   error:function(x) {
    //     console.log('failed', x)
    //     alert(x.responseJSON.isError ? x.responseJSON.reason : x.responseJSON.data);
    //   },// 요청 실패.
    //   complete:function(x) {
    //     console.log(x)
    //   }// 요청의 실패, 성공과 상관 없이 완료 될 경우 호출
    // });
  }


  this.step = 0
  $('.loading').hide()
  $(".step_1").hide()
  $("#btn_next").click((x) => {
    console.log("x", x)
    next()
  })
  
  console.log("already loaded")
})