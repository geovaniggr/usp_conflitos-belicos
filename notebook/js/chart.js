const chartCanvas = document.getElementById('notebook__section--graphic').getContext('2d');

fetch(`${baseUrl}/report/histogram`, { method: 'GET' })
.then( data => data.json())
.then( ([conflicts]) => {
    new Chart(chartCanvas, {
        type: 'bar',
        data: {
            labels: [ 'Econômicos', 'Territorial', 'Religioso', 'Etnicos'],
            datasets: [{
                label: 'Dataset',
                data: Object.values(conflicts),
                backgroundColor: [
                    'rgba(255,0,52)',
                    'rgb(45,250,89)',
                    'rgba(255, 206, 86, 0.6)',
                    'rgba(75, 192, 192, 0.6)',
                ],
                borderWidth: 1
            }],
        },
        options: {
            title: {
                display: true,
                text: "Tipo Conflito"
            },
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            },
        }
    });
})