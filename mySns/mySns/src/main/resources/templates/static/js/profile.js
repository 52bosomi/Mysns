$(document).ready(() => {
	console.log("sss")
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