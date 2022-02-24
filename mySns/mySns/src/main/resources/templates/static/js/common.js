function openNav() {
    //document.getElementById("mySidenav").style.width = "250px";
      document.getElementById("btn_toggle").style.display = "none";
  }
  
  function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
      document.getElementById("btn_toggle").style.display = "block";
  }
  
  // TODO : PC 버전에서는 보이고 모바일에서는 사이드 바가 안보여야 함
  //openNav()
  
    $.fn.enterKey = function (fnc) {
    return this.each(function () {
        $(this).keypress(function (ev) {
            var keycode = (ev.keyCode ? ev.keyCode : ev.which);
            if (keycode == '13') {
                fnc.call(this, ev);
            }
        })
    })
}