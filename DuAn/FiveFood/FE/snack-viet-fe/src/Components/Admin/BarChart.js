import React, { useEffect } from 'react';
import Chart from 'chart.js/auto';

const BarChart = ({data}) => {

    useEffect(()=>{
        if(data && data.monthlyProductSoldRevenues && window.theme && window.theme.primary){
            const ctx = document.getElementById("chartjs-dashboard-bar").getContext("2d");

            // Kiểm tra nếu chart đã tồn tại trước đó thì xóa đi
            if (ctx.chart) {
                ctx.chart.destroy();
            }

            const monthlyProductSoldRevenues = data.monthlyProductSoldRevenues;

            ctx.chart = new Chart(ctx,{
                type: "bar",
                    data: {
                        labels: ["Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"],
                        datasets: [{
                            label: "Trong năm",
                            backgroundColor: "#067a38",
                            borderColor: "#067a38",
                            hoverBackgroundColor: "#067a38",
                            hoverBorderColor: "#067a38",
                            data: monthlyProductSoldRevenues,
                            barPercentage: .75,
                            categoryPercentage: .5
                        }]
                    },
                    options: {
                        maintainAspectRatio: false,
                        legend: {
                            display: false
                        },
                        scales: {
                            yAxes: [{
                                gridLines: {
                                    display: false
                                },
                                stacked: false,
                                ticks: {
                                    stepSize: 20
                                }
                            }],
                            xAxes: [{
                                stacked: false,
                                gridLines: {
                                    color: "transparent"
                                }
                            }]
                        }
                    }
            })
        }
    },[])

    return (
        <div className="align-self-center chart chart-lg">
            <canvas id="chartjs-dashboard-bar"></canvas>
        </div>
    );
};

export default BarChart;