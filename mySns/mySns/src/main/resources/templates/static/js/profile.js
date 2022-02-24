$(document).ready(() => {
	console.log("sss")

     $('#profileTable').DataTable().destroy();
	 $('#profileTable').DataTable({
//	                        lengthChange:true,
//                            paging:true,
//                            pagingType: "full_numbers", // 페이징 버튼 타입 설정
                          pageLength: 10, // 한페이지에 보여주는 데이터 개수
//                            searching: true,
//                            serverSide: true,
//                            scrollY: 200,
//                            scrollCollapse: true,
//                            autoWidth:true,



                            ajax : {
                                url:"profile",
                                type:"POST",
                                dataType: "json",
                                contentType: "application/json",
                                dataSrc : "data"
//                                dataSrc : (x) => {
//                                    console.log(x)
//                                    return x
//                                },
//                                dataSrc:function(a, x) {
//                                    console.log(a, x)
//                                    return a, x;
//                                },
                              /*  data: function(d) {
                                  console.log(d)
                                  return JSON.stringify(d);
                                }*/
                            },
                            columns : [
                                {data: "idx"},
                                {data: "url"},
                                {data: "typeBox"}
                            ]
                        });

	$("#btn_connect").click(() => {
		console.log("sss")
		location.href = '/connect'
	})

	var ctx = document.getElementById('myChart');
	var myChart = new Chart(ctx, {
		type: 'doughnut',
		data: {
			labels: ['Naver', 'Insta', 'facebook'],
			datasets: [
					{
						label: 'Dataset 1',
						data: [10,20,70],
						backgroundColor: ['#4dbd74', '#ce2f82', '#007bff'],
					}
				]
			},
			options: {
				responsive: true,
				plugins: {
					legend: {
						display: false,
						position: 'top',
					},
					title: {
						display: true,
						text: ''
					}
				}
			}
		});
		console.log(myChart)




	})