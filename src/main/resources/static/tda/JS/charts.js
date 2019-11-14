function drawPieChart(elementID, dataArray, labelArray, colorBG, colorBorder) {
    var ctx = document.getElementById(elementID).getContext('2d');
    var myDoughnutChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: labelArray,
            datasets: [{
            data: dataArray,
            backgroundColor: colorBG,
            borderColor: colorBorder,
            borderColor: colorBorder,
            borderWidth: 1
        }]
            },
        options: {
            responsive: true,
            legend: {
                position: 'left',
            },
            
            animation: {
                animateScale: true,
                animateRotate: true
            }
        }
    });
}

function drawBarChart(elementID, dataArray, labelArray, colorBG, colorBorder) {
    var ctx = document.getElementById(elementID).getContext('2d');
    var myDoughnutChart = new Chart(ctx, {
        type: 'bar',
        data: {
        labels: labelArray,
        datasets: [
            {
                backgroundColor: colorBG,
                data: dataArray
            }
        ]
        },
        options: {
            legend: { display: false },
            title: {display: false},
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        }
    });
}