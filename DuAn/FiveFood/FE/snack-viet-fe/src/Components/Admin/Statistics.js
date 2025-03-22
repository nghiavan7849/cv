import React, { useEffect, useState } from 'react';
import SideNav from './SideNav';
import HorizontalNav from './HorizontalNav';
import axios from 'axios';
import { toast } from 'react-toastify';
import Footer from './Footer';
const urlApi = process.env.REACT_APP_API_URL;

const Statistics = () => {

    const baseUrl = `${urlApi}/api/admin/thong-ke`;
    let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));


    const [displaySelected,setDisplaySelected] = useState("");
    const [ngayFrom, setNgayFrom] = useState();
    const [ngayTo, setNgayTo] = useState();
    const [revenueData,setRevenueData] = useState([]);
    const [totalRevenue,setTotalRevenue] = useState("");
    const [err, setErr] = useState("");

    const handleDateChange = (event) => {
        const { name, value } = event.target;
        if (name === 'ngayFrom') {
            setNgayFrom(value);  // Lưu trực tiếp giá trị chuỗi "yyyy-MM-dd"
        } else if (name === 'ngayTo') {
            setNgayTo(value);    // Lưu trực tiếp giá trị chuỗi "yyyy-MM-dd"
        }
    };

    const handleSelectChange = (e) => {
        setDisplaySelected(e.target.value);
    };

    const handleSearch = async()=>{

        // const formattedNgayFrom = new Date(ngayFrom).toISOString().split("T")[0];
        // const formattedNgayTo = new Date(ngayTo).toISOString().split("T")[0];

        if(displaySelected===""){
            toast.info("Vui lòng chọn bộ lọc");
            return;
        }

        if (!ngayFrom || !ngayTo) {
            toast.info("Vui lòng chọn ngày hợp lệ.");
            return;
        }

        const payload = {
            select: displaySelected,
            ngayFrom,
            ngayTo,
        }


        try {
            const response = await axios.post(baseUrl,payload,{
                headers:{
                    Authorization: `Bearer ${sessionToken}`
                }
            })
            console.log(response);
            setRevenueData(response.data.data.revenueData);
            setTotalRevenue(response.data.data.totalRevenue);
        } catch (error) {
            // Kiểm tra xem có response hay không để truy cập vào data
            if (error.response) {
                // Nếu có response từ server
                toast.error(error.response.data.data.err || "Có lỗi xảy ra"); // Hiển thị thông báo lỗi
            } else {
                // Nếu không có response từ server (ví dụ: lỗi mạng)
                console.log("Error:", error.message);
                toast.error("Có lỗi xảy ra!");
            }
        }finally{

        }
    }

    const formattedMoney = (amount) => (
        amount.toLocaleString('vi-VN',{
            style: 'currency',
            currency: 'VND'
        })
    )

    return (
        <div className="wrapper">
            <SideNav></SideNav>
            <div className="main main-admin p-0">
                <HorizontalNav></HorizontalNav>
                <main className="content">
                    <div className="container-fluid p-0">
                        <h1 className="h3 mb-3">Thống kê dữ liệu</h1>
                        <div className="analyticsList" style={{marginTop:"20px"}}>
                            <form>
                                <div className='row'>
                                    <div className="col-md-3">
                                        <label className="form-label">Ngày bắt đầu:</label>
                                        <input onChange={handleDateChange} 
                                            name="ngayFrom" className="form-control"
                                            type="date" placeholder="Nhập vào ngày bắt đầu"
                                            value={ngayFrom}
                                            />
                                    </div>
                                    <div className="col-md-3">
                                        <label className="form-label">Ngày kết thúc:</label>
                                        <input onChange={handleDateChange} 
                                               name="ngayTo" className="form-control"
                                             type="date" placeholder="Nhập vào ngày kết thúc"
                                                value={ngayTo}
                                             />
                                    </div>
                                    <div className="col-md-6">
                                        <label className="form-label">Bộ lọc:</label>
                                        <div className="input-group mb-3">
                                            <select value={displaySelected} onChange={handleSelectChange} name="select" className="form-select" aria-label="Default select example">
                                                {displaySelected ? (
                                                    <option value={displaySelected} hidden>{displaySelected}</option>
                                                ) : (
                                                    <option disabled value="" hidden>Chọn bộ lọc</option>
                                                )}
                                                <option value="Lọc theo doanh thu">Lọc theo doanh thu</option>
                                            </select>
                                            <button type='button' onClick={handleSearch} id="searchButton" className="btn btn-success">Tìm kiếm</button>
                                        </div>
                                    </div>
                                </div>

                            </form>

                            {revenueData && revenueData!==null ? (
                                <div>
                                    <table id="dataTable" className="table table-striped table-hover table-light">
                                        <thead className="text-center" style={{ backgroundColor: '#067a38', color: '#fff',fontSize:'0.8rem' }}>
                                            <th>Loại sản phẩm</th>
                                            <th>Số lượng bán</th>
                                            <th>Doanh thu</th>
                                        </thead>
                                        <tbody className="text-center">
                                            {revenueData.map((row,index)=>(
                                                <tr key={index}>
                                                    <td>{row[0]}</td>
                                                    <td>{row[1]}</td>
                                                    <td>{formattedMoney(row[2])}</td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                    <h2 className="text-success">Tổng doanh thu:<span> {formattedMoney(totalRevenue)}</span>
                                    </h2>
                                </div>
                            ):(
                                <div>
                                    <span>hello</span>
                                </div>
                            )}

                        </div>
                    </div>
                </main>
                <Footer></Footer>
            </div>
        </div>
    );
};

export default Statistics;