// LineChart.js
import React, { useEffect } from 'react';
import Chart from 'chart.js/auto';

const LineChart = ({ data }) => {

    useEffect(() => {
        if (data && data.monthlyRevenues) {
            const ctx = document.getElementById("chartjs-dashboard-line").getContext("2d");

            // Kiểm tra nếu chart đã tồn tại trước đó thì xóa đi
            if (ctx.chart) {
                ctx.chart.destroy();
            }

            var gradient = ctx.createLinearGradient(0, 0, 0, 225);
            gradient.addColorStop(0, "rgba(215, 227, 244, 1)");
            gradient.addColorStop(1, "rgba(215, 227, 244, 0)");

            ctx.chart = new Chart(ctx, {
                type: "line",
                data: {
                    labels: ["Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"],
                    datasets: [{
                        label: "Doanh thu (VNĐ)",
                        fill: true,
                        backgroundColor: gradient,
                        borderColor: "#067a38",
                        data: data.monthlyRevenues
                    }]
                },
                options: {
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: false
                        },
                        filler: {
                            propagate: false
                        },
                    },
                    scales: {
                        x: {
                            reverse: true,
                            grid: {
                                color: "rgba(0,0,0,0.0)"
                            }
                        },
                        y: {
                            ticks: {
                                stepSize: 1000
                            },
                            grid: {
                                color: "rgba(0,0,0,0.0)"
                            }
                        }
                    },
                    interaction: {
                        intersect: false,
                        mode: 'index'
                    }
                }
            });
        }
    }, [data]);

    return (
        <div className="chart chart-sm">
            <canvas id="chartjs-dashboard-line"></canvas>
        </div>
    );
};

export default LineChart;
