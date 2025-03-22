import React, { useEffect } from 'react';
import Chart from 'chart.js/auto';

const PieChart = ({data}) => {

    useEffect(()=>{
        if(data && data.pieChart && window.theme && window.theme.primary){
            const ctx = document.getElementById("chartjs-dashboard-pie").getContext("2d");

            // Kiểm tra nếu chart đã tồn tại trước đó thì xóa đi
            if (ctx.chart) {
                ctx.chart.destroy();
            }

            const labels = data.pieChart.labels;
            const dulieu = data.pieChart.dulieu;

            ctx.chart = new Chart(ctx,{
                type: "pie",
                data: {
                    labels: labels,
                    datasets: [{
                        data: dulieu,
                        backgroundColor: [
                            window.theme.primary,
                            window.theme.info,
                            window.theme.warning,
                            window.theme.danger,
                            window.theme.info
                        ],
                        borderWidth: 5
                    }]
                },
                options: {
                    responsive: !window.MSInputMethodContext,
                    maintainAspectRatio: false,
                    legend: {
                        display: false
                    },
                    cutoutPercentage: 75
                }
            })
        }
    },[])

    return (
        <div className="chart chart-xs">
            <canvas id="chartjs-dashboard-pie"></canvas>
        </div>
    );
};

export default PieChart;