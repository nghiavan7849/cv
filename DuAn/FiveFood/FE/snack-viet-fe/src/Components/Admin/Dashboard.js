import React, { useEffect, useState } from 'react';
import HorizontalNav from './HorizontalNav';
import SideNav from './SideNav';
import '../../css/Admin/app.css.map';
import '../../css/Admin/app.css'
import '../../css/Admin/pagination.css';
import '../../css/Admin/test.css';
import Footer from './Footer';
import axios from 'axios';
import Chart from 'chart.js/auto';
import LineChart from './LineChart';
import PieChart from './PieChart';
import BarChart from './BarChart';
import Calendar from './Calendar';
import { FourSquare } from 'react-loading-indicators';


const Dashboard = () => {

    const base_url = process.env.REACT_APP_API_URL;

    const [data,setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));

    const formattedMoney = (amount) => (
        amount.toLocaleString('vi-VN',{
            style: 'currency',
            currency: 'VND'
        })
    )

    const fetchData = async() => {
        try {
            const response = await axios.get(base_url+"/api/admin/data",{
                headers: {
                    Authorization: `Bearer ${sessionToken}`
                }
            })
            setData(response.data.data);
            
        } catch (error) {
            setError(error.message);
        }finally{
            setLoading(false);
        }
    }

    useEffect(() => {

        // Tạo một thẻ script và gắn vào head
        const script = document.createElement('script');
        script.src = `${process.env.PUBLIC_URL}/static/js/app.js`;
        script.async = true;
        document.body.appendChild(script);

        fetchData();

        // Cleanup function để xóa thẻ script khi component unmount
        return () => {
        document.body.removeChild(script);
        };
      }, []);


    if(loading) return <div className='d-flex justify-content-center align-items-center' style={{minHeight:700}}>
            <FourSquare color="#067A38" size="large" text="" textColor="" />
        </div>;
    if (error) return <p>Error: {error}</p>;

    return (
        <div className="wrapper">

            <SideNav></SideNav>

            <div className="main main-admin p-0">

                <HorizontalNav></HorizontalNav>

                <main className="content">
                    <div className="container-fluid p-0">
                        <h1 className="h3 mb-3">
                            <strong>Số liệu tổng quát</strong>
                        </h1>

                        <div className="row">
                            <div className="col-xl-6 col-xxl-5 d-flex">
                                <div className="w-100">
                                    <div className="row">
                                        <div className="col-sm-6">
                                            <div className="card">
                                                <div className="card-body"  style={{borderRadius:8}}>
                                                    <div className="row">
                                                        <div className="col mt-0">
                                                            <h5 className="card-title">Số lượng sản phẩm bán ra</h5>
                                                        </div>

                                                        <div className="col-auto">
                                                            <div className="stat text-primary">
                                                                <i className="fa-solid fa-truck" style={{color: '#067a38'}} />
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <h1 className="mt-1 mb-3 fs-2"> {data.totalProduct}</h1>
                                                    <div className="mb-0">

                                                    </div>
                                                </div>
                                            </div>
                                            <div className="card">
                                                <div className="card-body"  style={{borderRadius:8}}>
                                                    <div className="row">
                                                        <div className="col mt-0">
                                                            <h5 className="card-title">Lượng khách đăng ký</h5>
                                                        </div>

                                                        <div className="col-auto">
                                                            <div className="stat text-primary">
                                                                <i className="fa-solid fa-users" style={{color: '#067a38'}}/>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <h1 className="mt-1 mb-3 fs-2"> {data.totalRegister} </h1>
                                                    <div className="mb-0">

                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div className="col-sm-6">
                                            <div className="card">
                                                <div className="card-body"  style={{borderRadius:8}}>
                                                    <div className="row">
                                                        <div className="col mt-0">
                                                            <h5 className="card-title">Doanh thu</h5>
                                                        </div>

                                                        <div className="col-auto">
                                                            <div className="stat text-primary">
                                                                <i className="fa-solid fa-dollar-sign" style={{color: '#067a38'}}/>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <h1 className="mt-1 mb-3 fs-2">
                                                        {formattedMoney(data.totalRevenue)}
                                                    </h1>
                                                    <div className="mb-0">

                                                    </div>
                                                </div>
                                            </div>
                                            <div className="card">
                                                <div className="card-body" style={{borderRadius:8}}>
                                                    <div className="row">
                                                        <div className="col mt-0">
                                                            <h5 className="card-title">Số đơn hàng đã bán</h5>
                                                        </div>

                                                        <div className="col-auto">
                                                            <div className="stat text-primary">
                                                                <i className="fa-solid fa-shopping-cart" style={{color: '#067a38'}}/>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <h1 className="mt-1 mb-3 fs-2">{data.totalOrder}</h1>
                                                    <div className="mb-0">

                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </div>

                            <div className="col-xl-6 col-xxl-7 col-sm-6">
                                <div className="card flex-fill w-100">
                                    <div className="card-header">

                                        <h5 className="card-title mb-0">Biến động doanh thu</h5>
                                    </div>
                                    <div className="card-body py-3" style={{borderRadius:8}}>
                                        <div className="chart chart-sm">
                                            <LineChart  data={data} />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="row d-flex justify-content-center">
                            <div className="col-12 col-md-6 col-xxl-3 d-flex order-2 order-xxl-3">
                                <div className="card flex-fill w-100">
                                    <div className="card-header">
                                        <h5 className="card-title mb-0">Phân bố lượt mua loại sản phẩm</h5>
                                    </div>
                                    <div className="card-body d-flex" style={{borderRadius:8}}>
                                        <div className="align-self-center w-100">
                                            <div className="py-3">
                                                <div className="chart chart-xs">
                                                    <PieChart data={data}></PieChart>
                                                </div>
                                            </div>
                                            <table className="table mb-0">
                                                <tbody>
                                                    {data.pieChart.labels.map((label, index) => (
                                                        <tr key={index}>
                                                            <td>{label}</td>
                                                            <td className="text-end">{data.pieChart.dulieu[index]}</td>
                                                        </tr>
                                                    ))}
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="col-12 col-lg-4 col-xxl-3 d-flex">
                                <div className="card flex-fill w-100">
                                    <div className="card-header">

                                        <h5 className="card-title mb-0">Số lượng sản phẩm bán ra hằng tháng</h5>
                                    </div>
                                    <div className="card-body d-flex w-100" style={{borderRadius:8}}>
                                        <div className="align-self-center chart chart-lg">
                                            <BarChart data = {data}></BarChart>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="col-12 col-md-6 col-xxl-3 d-flex order-1 order-xxl-1">
                                <div className="card flex-fill">
                                    <div className="card-header">

                                        <h5 className="card-title mb-0">Lịch</h5>
                                    </div>
                                    <div className="card-body d-flex" style={{borderRadius:8}}>
                                        <div className="align-self-center w-100">
                                            <div className="chart">
                                                <Calendar></Calendar>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                </main>
                <Footer></Footer>

            </div>
        </div>
    );
};

export default Dashboard;