google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);


function drawChart() {
    var favor = document.getElementById("favor");
    var contra = document.getElementById("contra");
    
    var favorValue = favor.innerText;
    var contraValue = contra.innerText;

  var data = google.visualization.arrayToDataTable([
	  ['Titulo', 'Cantidad'],
	  ['Positivo',    parseInt(favorValue)],
    ['Negativo',    parseInt(contraValue)]
  ]);

  var options = {
    pieStartAngle: 100,
    title: 'PUNTAJE',
    is3D: true,
    slices: {
      0: { color: '#1ef5ad' },
      1: { color: '#ff5364' }
    },
    backgroundColor: "transparent",
    titleTextStyle: { 
    		color: "#fff",
            bold: true
          }
  };

  var chart = new google.visualization.PieChart(document.getElementById('piechart'));
        chart.draw(data, options);

  chart.draw(data, options);
}