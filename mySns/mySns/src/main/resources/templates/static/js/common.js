function openNav() {
    document.getElementById("mySidenav").style.width = "250px";
      document.getElementById("btn_toggle").style.display = "none";
  }
  
  function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
      document.getElementById("btn_toggle").style.display = "block";
  }
  
  // TODO : PC 버전에서는 보이고 모바일에서는 사이드 바가 안보여야 함
  openNav()