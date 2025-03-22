import React, { useEffect, useState } from "react";
import SideNav from "./SideNav";
import HorizontalNav from "./HorizontalNav";
import axios from "axios";
import { toast } from "react-toastify";
import { getDownloadURL, ref } from "firebase/storage";
import { storage } from "../Firebase";
import { NavLink } from "react-router-dom";
import ReactPaginate from "react-paginate";
import { div } from "@tensorflow/tfjs";
import Footer from "./Footer";
const urlApi = process.env.REACT_APP_API_URL;
const urlApiPython = process.env.REACT_APP_API_URL_PYTHON;

const Reviews = () => {
  const [review, setReview] = useState("");
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const [editting, setEditting] = useState(false);
  const [displayR, setDisplayR] = useState([]);
  const [phanHoi, setPhanHoi] = useState({});
  const [search, setSearch] = useState("");
  const [sort, setSort] = useState("Tên sản phẩm");
  const [sortAnalyze, setSortAnalyze] = useState("");
  const [pagination, setPagination] = useState([]);
  const [reviews, setReviews] = useState([]);
  const [newReviews, setNewReviews] = useState([]);
  const [hide, setHide] = useState(false);
  const [analytics, setAnalytics] = useState([]);
  const [displayReview, setDisplayReview] = useState([]);
  const [err, setErr] = useState({
    errReply: "",
  });
  const [cards, setCards] = useState([]);
  const [displaySelected, setDisplaySelected] = useState("");

  const [listHinhDG, setListHinhDG] = useState([]);

  const [ngayFrom, setNgayFrom] = useState(new Date());
  const [ngayTo, setNgayTo] = useState(new Date());

  const baseUrl = `${urlApi}/api/admin/quan-ly-danh-gia`;
  let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));
  const pages = Array.from(
    { length: pagination.totalPages },
    (_, index) => index + 1
  );

  const handleSearchChange = (event) => {
    setSearch(event.target.value);
  };

  const formatDate = (date) => {
    //Chuyển đổi đối tượng Date thành chuỗi ISO với toISOString()
    //Sử dụng split('T')[0] để lấy phần ngày từ chuỗi
    return new Date(date).toISOString().split("T")[0];
  };

  const resetForm = () => {
    setErr({
      errReply: "",
    });
  };

  const fetchData = async (pageNo=1) => {
    // const pageNo = 1;
    resetForm();
    const formattedNgayFrom = formatDate(ngayFrom);
    const formattedNgayTo = formatDate(ngayTo);
    try {
      const response = await axios.get(
        `${baseUrl}?pageNo=${pageNo}&sort=${sort}&search=${search}&ngayFrom=${formattedNgayFrom}&ngayTo=${formattedNgayTo}`,
        {
          headers: {
            Authorization: `Bearer ${sessionToken}`,
          },
        }
      );
      setCards(response.data.data.cards);
      setEditting(response.data.data.editting);
      setReviews(response.data.data.reviews);
      setPagination(response.data.data.pagination);

      const binhLuanList = response.data.data.reviews.map((i) => i.binhLuan);

      const sentimentResult = await fetchSentimentAnalysis(binhLuanList);

      // Kết hợp reviews với sentimentResult vào analytics
      const analyticsData = response.data.data.reviews.map((review, index) => {
        return {
          ...review, // Sao chép toàn bộ thông tin từ review
          phanTich: sentimentResult[index], // Gắn phân tích cảm xúc tương ứng
        };
      });

      setAnalytics({ reviews: analyticsData }); // Lưu kết quả vào state analytics
    } catch (error) {
    } finally {
    }
  };

  const handleSearchSubmit = (event) => {
    event.preventDefault();
  
    // Lấy giá trị của các trường ngày
    const ngayFrom = document.querySelector("input[name='ngayFrom']").value;
    const ngayTo = document.querySelector("input[name='ngayTo']").value;
  
    // Kiểm tra ngày
    if (ngayFrom && ngayTo && new Date(ngayFrom) > new Date(ngayTo)) {
      toast.error("Bộ lọc ngày không hợp lệ! Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc.");
      return; // Ngăn không cho gọi fetchData()
    }
  
    // Nếu hợp lệ, gọi fetchData()
    fetchData();
  };
  

  const handlePageClick = (data) => {
    let selectedPage = data.selected + 1; // Page index is 0-based in react-paginate
    fetchData(selectedPage); // Gọi hàm fetchData với số trang đã chọn
  };

  const fetchSentimentAnalysis = async (reviewText) => {
    try {
      let response;
      if (reviewText.length > 1) {
        response = await axios.post(`${urlApiPython}/analyze`, {
          review: [reviewText],
        });
        return response.data;
      } else {
        response = await axios.post(`${urlApiPython}/analyze`, {
          review: [reviewText],
        });
        return response.data[0];
      }
      // Giả sử API trả về mảng kết quả, lấy phần tử đầu tiên
    } catch (error) {
      console.error("Error fetching sentiment analysis:", error);
      return null;
    }
  };

  const fetchDetail = (maDanhGia) => async () => {
    try {
      const response = await axios.get(baseUrl + "/" + maDanhGia, {
        headers: {
          Authorization: `Bearer ${sessionToken}`,
        },
      });

      const newDisplayR = response.data.data.displayR; // Lưu trữ thông tin displayR mới

      setDisplayR(response.data.data.displayR);
      setDisplayReview(response.data.data.displayReview);
      setEditting(response.data.data.editting);
      setListHinhDG(response.data.data.listHinhDG);
      setPhanHoi(response.data.data.phanHoi);
      console.log(listHinhDG)
      // Sau khi nhận được dữ liệu, phân tích cảm xúc và cập nhật lại displayR
      const sentimentResult = await fetchSentimentAnalysis(
        newDisplayR.binhLuan
      );

      // Cập nhật lại phanTich cho displayR
      setDisplayR((prevState) => ({
        ...prevState,
        phanTich: sentimentResult[0],
      }));
    } catch (error) {
      toast.error("Có lỗi xảy ra khi lấy dữ liệu đánh giá");
    } finally {
    }
  };

  const handlePost = async () => {
    const payload = {
      noiDungPhanHoi: phanHoi?.noiDungPhanHoi || "",
      maDanhGia: displayR?.maDanhGia || -1,
      maTaiKhoan:
        JSON.parse(sessionStorage.getItem("sessionTaiKhoan")).maTaiKhoan || -1,
    };

    if (err.errReply === "") {
      axios
        .post(baseUrl + "/create", payload, {
          headers: {
            Authorization: `Bearer ${sessionToken}`,
          },
        })
        .then((response) => {
          toast.success(response.data.message);
          fetchData();
          setErr({ ...err, errReply: "" });
          setPhanHoi(response.data.data);
        })
        .catch((error) => {
          if (error.response) {
            const errorMessage =
              error.response.data.message || "Đã xảy ra lỗi.";
            setErr({ ...err, errReply: error.response.data.data.errReply });
            console.log(error.response.data.data);
            toast.error(error.response.data.data.errReply);
          } else {
            toast.error("Đã xảy ra lỗi không xác định.");
          }
        })
        .finally(() => {
          resetForm();
        });
    } else {
      toast.error("Vui lòng kiểm tra lỗi hiển thị!");
      console.log(Object.keys(err).length); // In ra số lượng lỗi
    }
  };

  const handlePut = async () => {
    const payload = {
      noiDungPhanHoi: phanHoi.noiDungPhanHoi || "",
      maDanhGia: displayR.maDanhGia,
      maTaiKhoan: JSON.parse(sessionStorage.getItem("sessionTaiKhoan"))
        .maTaiKhoan,
      maPhanHoiDanhGia: phanHoi.maPhanHoiDanhGia || "",
    };

    if (err.errReply === "") {
      axios
        .put(baseUrl + "/update/" + displayR.maDanhGia, payload, {
          headers: {
            Authorization: `Bearer ${sessionToken}`,
          },
        })
        .then((response) => {
          toast.success(response.data.message);
          fetchData();
          setErr({ ...err, errReply: "" });
        })
        .catch((error) => {
          if (error.response) {
            const errorMessage =
              error.response.data.message || "Đã xảy ra lỗi.";
            setErr(error.response.data.data);
            console.log(error);
            toast.error(errorMessage);
          } else {
            toast.error("Đã xảy ra lỗi không xác định.");
          }
        })
        .finally(() => {
          resetForm();
        });
    } else {
      toast.error("Vui lòng kiểm tra lỗi hiển thị!");
      console.log(Object.keys(err).length); // In ra số lượng lỗi
    }
  };

  const handleSortChange = (event) => {
    setSort(event.target.value);

    toggleSearchAndDateFields();
  };

  const handleSortChangeAnalyze = (event) => {
    setSortAnalyze(event.target.value);
  };

  const toggleSearchAndDateFields = () => {
    var sortSelect = document.getElementById("sortSelect");
    var searchField = document.getElementById("searchField");
    var dateFields = document.getElementById("dateFields");

    if (sortSelect.value === "Ngày đánh giá") {
      searchField.style.display = "none";
      dateFields.style.display = "flex"; // Sử dụng flex để giữ các phần tử trong cùng một hàng
    } else {
      searchField.style.display = "flex"; // Sử dụng flex để giữ các phần tử trong cùng một hàng
      dateFields.style.display = "none";
    }
  };

  const handleDateChange = (event) => {
    const { name, value } = event.target;
    if (name === "ngayFrom") {
      setNgayFrom(new Date(value));
    } else if (name === "ngayTo") {
      setNgayTo(new Date(value));
    }
  };

  const basicValidation = (e, fieldName) => {
    if (fieldName === "phanHoi") {
      if (e.target.value.length <= 0) {
        setErr({ ...err, errReply: "Vui lòng nhập phản hồi cho đánh giá!" });
      } else {
        setErr({ ...err, errReply: "" });
      }
      setPhanHoi({ ...phanHoi, noiDungPhanHoi: e.target.value });
    }
  };

  const fetchAllReviews = async () => {
    try {
      let allReviews = [];
      const response = await axios.get(
        `${baseUrl}?sort=${sortAnalyze}&search=${search}`,
        {
          headers: {
            Authorization: `Bearer ${sessionToken}`,
          },
        }
      );
      allReviews = response.data.data.allReviews; // Gom tất cả reviews
      console.log(response)
  
      return allReviews; // Trả về danh sách tất cả các đánh giá
    } catch (error) {
      console.error("Error fetching all reviews:", error);
      return [];
    }
  };

  useEffect(() => {
    const fetchAndFilterReviews = async () => {
      try {
        const allReviews = await fetchAllReviews();
        console.log(allReviews) // Lấy tất cả đánh giá
        const sentimentResult = await fetchSentimentAnalysis(
          allReviews.map((r) => r.binhLuan)
        );
  
        const analyticsData = allReviews.map((review, index) => ({
          ...review,
          phanTich: sentimentResult[index], // Gắn kết quả phân tích cảm xúc
        }));
  
        // Lọc theo điều kiện `sort`
        let filteredReviews = [];
        if (sortAnalyze === "Đánh giá khen") {
          filteredReviews = analyticsData.filter(
            (a) =>
              a.phanTich.summary.includes("Khen") && // Phải có từ "Khen"
              !a.phanTich.summary.match(/Chê|trung lập/) // Không chứa "Chê" hoặc "trung lập"
          );
          console.log(filteredReviews)
        } else if (sortAnalyze === "Đánh giá chê") {
          filteredReviews = analyticsData.filter(
            (a) =>
              a.phanTich.summary.includes("Chê") && // Phải có từ "Chê"
              !a.phanTich.summary.match(/Khen|trung lập/) // Không chứa "Khen" hoặc "trung lập"
          );
        } else if (sortAnalyze === "Đánh giá trung lập") {
          filteredReviews = analyticsData.filter(
            (a) =>
              a.phanTich.summary.includes("trung lập") && // Phải có từ "trung lập"
              !a.phanTich.summary.match(/Khen|Chê/) // Không chứa "Khen" hoặc "Chê"
          );
        } else if (sortAnalyze === "Đánh giá mixed") {
          filteredReviews = analyticsData.filter(
            (a) =>
              ["Khen", "Chê"].every((keyword) =>
                a.phanTich.summary.includes(keyword)
              ) 
          );
        }
  
        setAnalytics({ reviews: analyticsData }); // Lưu tất cả reviews vào state
        setNewReviews(filteredReviews); // Lưu kết quả lọc
        setHide(true);
      } catch (error) {
        console.error("Error fetching or filtering reviews:", error);
      }
    };
  
    fetchAndFilterReviews();
    console.log('Đã in');
  }, [sortAnalyze]);

  useEffect(() => {
    fetchData();
    toggleSearchAndDateFields();
  }, [sort, search, displayR]);


  return (
    <div className="wrapper">
      <SideNav></SideNav>
      <div className="main main-admin p-0">
        <HorizontalNav></HorizontalNav>
        <main className="content">
          <div className="container-fluid p-0">
            <h1 className="h3 mb-3">Danh sách đánh giá sản phẩm</h1>

            <div className="row">
              <div className="col-md-6">
                <div className="card">
                  <div className="card-body" style={{borderRadius:8}}>
                    <div className="row">
                      <div className="col mt-0">
                        <h5 className="card-title">Tổng số bình luận</h5>
                      </div>

                      <div className="col-auto">
                        <div className="stat text-primary">
                          <i className="fa-solid fa-file-invoice" style={{color: "#067a38"}}></i>
                        </div>
                      </div>
                    </div>
                    <h1 className="mt-1 mb-3 fs-2">{cards.total}</h1>
                  </div>
                </div>
              </div>
              <div className="col-md-6">
                <div className="card">
                  <div className="card-body" style={{borderRadius:8}}>
                    <div className="row">
                      <div className="col mt-0">
                        <h5 className="card-title">Số sản phẩm đạt 5 sao</h5>
                      </div>

                      <div className="col-auto">
                        <div className="stat text-primary">
                          <i className="fa-solid fa-file-invoice" style={{color: "#067a38"}}></i>
                        </div>
                      </div>
                    </div>
                    <h1 className="mt-1 mb-3 fs-2">{cards.fivestar}</h1>
                  </div>
                </div>
              </div>
            </div>

            <div className="commentDetail d-flex justify-content-center">
              <div
                className="card"
                style={{ width: "1000px", height: "600px" }}
              >
                <div className="card-body" style={{borderRadius:8}}>
                  <div hidden={displayR.length !== 0}>
                    <h2
                      className="text-center"
                      style={{
                        color: "grey",
                        position: "absolute",
                        top: "50%",
                        left: "25%",
                        right: "25%",
                      }}
                    >
                      Chọn dữ liệu để hiển thị
                    </h2>
                  </div>

                  {/* NẾU NGƯỜI DÙNG CLICK VÀO EDIT THÌ HIỂN THỊ */}
                  <div hidden={displayR.length === 0}>
                    <div className="d-flex">
                      <img
                        className="rounded-circle"
                        alt="ảnh người dùng"
                        src={displayR?.taiKhoanDG?.hinhAnh}
                        style={{ width: "10%" }}
                      />
                      <div
                        className="customerDetail"
                        style={{ marginLeft: "15px" }}
                      >
                        <p className="fw-bold">
                          {displayR?.taiKhoanDG?.hoVaTen}
                        </p>
                        <p style={{ color: "rgb(163,163,163)" }}>
                          {displayR.ngayDanhGia}
                        </p>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-md-4">
                        <p>{displayR.binhLuan}</p>
                        <span>
                          {[...Array(displayR.soSao)].map((_, i) => (
                            <span key={i}>⭐</span>
                          ))}
                        </span>
                        <span>{displayR.soSao}</span>
                        <span>
                          <textarea
                            onChange={(e) => basicValidation(e, "phanHoi")}
                            value={phanHoi?.noiDungPhanHoi || ""}
                            placeholder="Nhập phản hồi"
                            className="form-control m-0"
                            name=""
                            id=""
                            cols={6}
                            rows={4}
                          />
                          <em className="text-danger">{err.errReply}</em>
                        </span>
                        <div className="display-reply mt-3">
                          <button
                            onClick={
                              phanHoi?.maPhanHoiDanhGia ? handlePut : handlePost
                            }
                            className={`btn ${
                              phanHoi?.maPhanHoiDanhGia
                                ? "btn-outline-info"
                                : "btn-outline-success"
                            }`}
                          >
                            {phanHoi?.maPhanHoiDanhGia
                              ? "Cập nhật phản hồi"
                              : "Phản hồi"}
                          </button>
                        </div>
                      </div>
                      <div className="col-md-8 text-center">
                        <div className="row">
                          <div
                            className="image-gallery"
                            style={{ marginTop: "10px" }}
                          >
                            {listHinhDG &&
                              listHinhDG.map((hinh, index) => (
                                <a
                                  key={index}
                                  href={hinh.tenHinhAnh} // Đường dẫn ảnh từ imageUrls
                                  data-lightbox="review-images"
                                  data-title={hinh.tenHinhAnh}
                                >
                                  <img
                                    src={hinh.tenHinhAnh} // Đường dẫn ảnh từ imageUrls
                                    alt={hinh.tenHinhAnh}
                                    style={{
                                      width: "100px",
                                      height: "120px",
                                      objectFit: "cover",
                                      margin: "5px",
                                      border: "1px solid #ddd",
                                      padding: "5px",
                                    }}
                                  />
                                </a>
                              ))}
                          </div>
                        </div>
                      </div>

                      {/* Phân tích cảm xúc */}
                      <div className="sentiment-analysis mt-4">
                        <h5>Phân tích cảm xúc:</h5>
                        {displayR?.phanTich?.analysis?.map((item, index) => (
                          <div key={index} className="sentiment-item">
                            <p>
                              <span
                                className={`sentiment-label ${item.sentiment.toLowerCase()}`}
                              >
                                {item.sentiment === "POS"
                                  ? "Tích cực"
                                  : item.sentiment === "NEG"
                                  ? "Tiêu cực"
                                  : "Trung lập"}
                              </span>
                              : {item.clause}
                            </p>
                          </div>
                        ))}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <ul className="nav nav-tabs" id="myTab" role="tablist">
              <li className="nav-item" role="presentation">
                <button
                  className="nav-link active"
                  id="home-tab"
                  data-bs-toggle="tab"
                  data-bs-target="#danhgia"
                  type="button"
                  role="tab"
                  aria-controls="home"
                  aria-selected="true"
                >
                  Đánh giá sản phẩm
                </button>
              </li>
              <li className="nav-item" role="presentation">
                <button
                  className="nav-link"
                  id="profile-tab"
                  data-bs-toggle="tab"
                  data-bs-target="#phantich"
                  type="button"
                  role="tab"
                  aria-controls="profile"
                  aria-selected="false"
                >
                  Phân tích đánh giá
                </button>
              </li>
            </ul>
            <div className="tab-content" id="myTabContent">
              {/* ĐÁNH GIÁ Ở ĐÂY */}
              <div
                className="tab-pane fade show active"
                id="danhgia"
                role="tabpanel"
                aria-labelledby="home-tab"
              >
                <div className="commentList" style={{ marginTop: "20px" }}>
                  <h4 className="text-center"> Đánh giá sản phẩm </h4>
                  <form onSubmit={handleSearchSubmit}>
                    <div className="row">
                      <div className="col-md-6">
                        <div className="input-group mb-3">
                          <select
                            id="sortSelect"
                            name="sort"
                            className="form-select"
                            aria-label="Default select example"
                            onChange={
                              toggleSearchAndDateFields && handleSortChange
                            }
                          >
                            <option
                              disabled={
                                displaySelected === null ||
                                displaySelected === ""
                                  ? "disabled"
                                  : ""
                              }
                              selected
                              hidden
                            >
                              Chọn bộ lọc
                            </option>
                            <option value="Ngày đánh giá">Ngày đánh giá</option>
                            <option value="Tên sản phẩm">Tên sản phẩm</option>
                            <option value="Số sao">Số sao</option>
                            <option value="Trạng thái hiển thị">
                              Trạng thái hiển thị
                            </option>
                            <option value="Trạng thái ẩn">Trạng thái ẩn</option>
                            <option value="Bình luận">Bình luận</option>
                          </select>
                          <div id="searchField">
                            <input
                              name="search"
                              type="search"
                              value={search}
                              className="form-control"
                              placeholder="Tìm kiếm"
                              onChange={handleSearchChange}
                            />
                            {/* <button className="btn btn-success">
                              Tìm kiếm
                            </button> */}
                          </div>
                        </div>
                      </div>

                      <div
                        id="dateFields"
                        className="col-md-6"
                        style={{ display: "none" }}
                      >
                        <div className="input-group mb-3">
                          <input
                            name="ngayFrom"
                            type="date"
                            className="form-control"
                            placeholder="Từ"
                            onChange={handleDateChange}
                          />
                          <input
                            name="ngayTo"
                            type="date"
                            className="form-control"
                            placeholder="Đến"
                            onChange={handleDateChange}
                          />
                          {/* <button className="btn btn-success">Tìm kiếm</button> */}
                        </div>
                      </div>
                    </div>
                  </form>

                  <table className="table table-striped table-hover table-light">
                    <thead className="text-center" style={{ backgroundColor: '#067a38', color: '#fff',fontSize:'0.8rem' }}>
                        <th>Mã đánh giá</th>
                        <th>Ngày đánh giá</th>
                        <th>Trạng thái</th>
                        <th>Tên khách hàng</th>
                        <th>Tên sản phẩm</th>
                        <th>Số sao</th>
                        <th>Hành động</th>
                    </thead>
                    <tbody className="text-center">
                      {reviews.map((r, index) => (
                        <tr key={index}>
                          <td>{r.maDanhGia}</td>
                          <td>{r.ngayDanhGia}</td>
                          <td>
                            <span
                              className={`badge ${
                                r.trangThai ? "bg-success" : "bg-danger"
                              }`}
                            >
                              {r.trangThai ? "Hiển thị" : "Ẩn"}
                            </span>
                          </td>
                          <td>{r.taiKhoanDG.hoVaTen}</td>
                          <td>{r.sanPhamDG.tenSanPham}</td>
                          <td>
                            {[...Array(r.soSao)].map((_, i) => (
                              <span key={i}>⭐</span>
                            ))}
                          </td>
                          <td>
                            <i
                              className="fa-solid fa-pen-to-square"
                              onClick={fetchDetail(r.maDanhGia)}
                            ></i>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>

                  <div
                    className="text-center"
                    hidden={pagination.totalPages !== 0}
                  >
                    <p className="fw-bold">Không tìm thấy sản phẩm tương ứng</p>
                  </div>

                  <div
                    hidden={pagination.totalPages === 0}
                    className="d-flex justify-content-between"
                    style={{ marginTop: "25px" }}
                  >
                    {/* Vị trí hiển thị số trang */}
                    <p className="fw-bold">
                      Đang xem trang {pagination.currentPage} /{" "}
                      {pagination.totalPages}
                    </p>

                    {/* React Paginate */}
                    <ReactPaginate
                      previousLabel={"«"}
                      nextLabel={"»"}
                      breakLabel={"..."}
                      breakClassName={"page-item"} // Áp dụng style cho phần tử 3 chấm
                      breakLinkClassName={"page-link"} // Áp dụng style cho link của 3 chấm
                      pageCount={pagination.totalPages}
                      marginPagesDisplayed={2}
                      pageRangeDisplayed={5} // Số lượng trang hiển thị
                      onPageChange={handlePageClick}
                      containerClassName={"pagination"}
                      pageClassName={"page-item"}
                      pageLinkClassName={"page-link"}
                      previousClassName={"page-item"}
                      previousLinkClassName={"page-link"}
                      nextClassName={"page-item"}
                      nextLinkClassName={"page-link"}
                      activeClassName={"active"}
                      disabledClassName={"disabled"}
                      forcePage={
                        pagination.currentPage > 0
                          ? pagination.currentPage - 1
                          : 0
                      } // Chuyển currentPage thành 0-based index cho ReactPaginate
                    />
                    <p className="fw-bold">5 sản phẩm / 1 trang</p>
                  </div>
                </div>
              </div>
              {/* PHÂN TÍCH Ở ĐÂY */}
              <div
                className="tab-pane fade"
                id="phantich"
                role="tabpanel"
                aria-labelledby="profile-tab"
              >
                <div className="commentList" style={{ marginTop: "20px" }}>
                  <h4 className="text-center"> Phân tích đánh giá </h4>
                  <form onSubmit={handleSearchSubmit}>
                    <div className="row">
                      <div className="col-md-6">
                        <div className="input-group mb-3">
                          <select
                            id="sortSelect"
                            name="sort"
                            className="form-select"
                            aria-label="Default select example"
                            onChange={
                              toggleSearchAndDateFields && handleSortChangeAnalyze
                            }
                          >
                            <option
                              disabled={
                                displaySelected === null ||
                                displaySelected === ""
                                  ? "disabled"
                                  : ""
                              }
                              selected
                              hidden
                            >
                              Chọn bộ lọc
                            </option>
                            <option value="Đánh giá khen">Đánh giá khen</option>
                            <option value="Đánh giá chê">Đánh giá chê</option>
                            <option value="Đánh giá trung lập">
                              Đánh giá trung lập
                            </option>
                            <option value="Đánh giá mixed">Đánh giá mixed</option>
                          </select>
                          <div id="searchField" className="d-flex">
                            <input
                              name="search"
                              type="text"
                              value={search}
                              className="form-control"
                              placeholder="Tìm kiếm"
                              onChange={handleSearchChange}
                            />
                            {/* <button className="btn btn-success">
                              Tìm kiếm
                            </button> */}
                          </div>
                        </div>
                      </div>

                      <div
                        id="dateFields"
                        className="col-md-6"
                        style={{ display: "none" }}
                      >
                        <div className="input-group mb-3">
                          <input
                            name="ngayFrom"
                            type="date"
                            className="form-control"
                            placeholder="Từ"
                            onChange={handleDateChange}
                          />
                          {/* th:value="${#dates.format(ngayFrom, 'yyyy-MM-dd')}" */}
                          <input
                            name="ngayTo"
                            type="date"
                            className="form-control"
                            placeholder="Đến"
                            onChange={handleDateChange}
                          />
                          {/* th:value="${#dates.format(ngayTo, 'yyyy-MM-dd')}" */}
                          {/* <button className="btn btn-success">Tìm kiếm</button> */}
                        </div>
                      </div>
                    </div>
                  </form>

                  <table className="table table-striped table-hover table-light">
                    <thead className="text-center" style={{ backgroundColor: '#067a38', color: '#fff' }}>
                        <th>Mã đánh giá</th>
                        <th>Tên khách hàng</th>
                        <th>Tên sản phẩm</th>
                        <th>Số sao</th>
                        <th>Bình luận</th>
                        <th>Phân tích cảm xúc</th>
                        <th>Hành động</th>
                    </thead>
                    <tbody className="text-center">
                      {!sortAnalyze.includes("Đánh giá")
                        ? analytics.reviews?.map((r, index) => (
                            <tr key={index}>
                              <td>{r.maDanhGia}</td>
                              <td>{r.taiKhoanDG.hoVaTen}</td>
                              <td>{r.sanPhamDG.tenSanPham}</td>
                              <td>
                                {[...Array(r.soSao || 0)].map((_, i) => (
                                  <span key={i}>⭐</span> // Hiển thị số sao
                                ))}
                              </td>
                              <td>{r.binhLuan}</td>
                              <td>{r.phanTich?.summary}</td>
                              <td>
                                <i
                                  className="fa-solid fa-pen-to-square"
                                  onClick={fetchDetail(r.maDanhGia)}
                                ></i>
                              </td>
                            </tr>
                          ))
                        : newReviews.map((r, index) => (
                            <tr key={index}>
                              <td>{r.maDanhGia}</td>
                              <td>{r.taiKhoanDG.hoVaTen}</td>
                              <td>{r.sanPhamDG.tenSanPham}</td>
                              <td>
                                {[...Array(r.soSao || 0)].map((_, i) => (
                                  <span key={i}>⭐</span> // Hiển thị số sao
                                ))}
                              </td>
                              <td>{r.binhLuan}</td>
                              <td>{r.phanTich.summary}</td>
                              <td>
                                <i
                                  className="fa-solid fa-pen-to-square"
                                  onClick={fetchDetail(r.maDanhGia)}
                                ></i>
                              </td>
                            </tr>
                          ))}
                    </tbody>
                  </table>

                  <div
                    className="text-center"
                    hidden={pagination.totalPages !== 0}
                  >
                    <p className="fw-bold">Không tìm thấy đánh giá tương ứng</p>
                  </div>

                  <div
                    id="pending-display" className={`d-flex justify-content-between ${hide ? "d-none" : ""}`}
                    style={{ marginTop: "25px" }}
                  >
                    <p className="fw-bold">
                      Đang xem trang {pagination.currentPage} /{" "}
                      {pagination.totalPages}
                    </p>

                    <ReactPaginate
                      previousLabel={"«"}
                      nextLabel={"»"}
                      breakLabel={"..."}
                      breakClassName={"page-item"} // Áp dụng style cho phần tử 3 chấm
                      breakLinkClassName={"page-link"} // Áp dụng style cho link của 3 chấm
                      pageCount={pagination.totalPages}
                      marginPagesDisplayed={2}
                      pageRangeDisplayed={5} // Số lượng trang hiển thị
                      onPageChange={handlePageClick}
                      containerClassName={"pagination"}
                      pageClassName={"page-item"}
                      pageLinkClassName={"page-link"}
                      previousClassName={"page-item"}
                      previousLinkClassName={"page-link"}
                      nextClassName={"page-item"}
                      nextLinkClassName={"page-link"}
                      activeClassName={"active"}
                      disabledClassName={"disabled"}
                      forcePage={
                        pagination.currentPage > 0
                          ? pagination.currentPage - 1
                          : 0
                      } // Chuyển currentPage thành 0-based index cho ReactPaginate
                    />
                    <p className="fw-bold"> 5 sản phẩm / 1 trang</p>
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

export default Reviews;
