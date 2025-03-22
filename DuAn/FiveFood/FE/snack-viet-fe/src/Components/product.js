import '../css/product.css';
import Navbar from './menu';
import Footer from './footer';
import axios from 'axios';
import { toast } from 'react-toastify';
import { useEffect, useRef, useState } from 'react';
import ReactPaginate from 'react-paginate';
import { useNavigate, useLocation } from 'react-router-dom';
import * as tf from "@tensorflow/tfjs";
import formatVND from '../Service/FormatVND';
import { FourSquare } from 'react-loading-indicators';

/* global tflite */
const urlApi = process.env.REACT_APP_API_URL;

export const Product = () => {
    const [products, setProducts] = useState([]);
    const [loaiSanPhams, setLoaiSanPhams] = useState([]);
    const [loading, setLoading] = useState(true);
    const [totalPage, setTotalPages] = useState(0);
    const [valueSapXep, setValueSapXep] = useState("");
    const [pageSapXep, setPageSapXep] = useState(0);
    const [boLoc, setBoLoc] = useState('');
    const [loai, setLoai] = useState({});
    const [timKiem, setTimKiem] = useState("");
    const [imageUrls, setImageUrls] = useState({}); // Store image URLs
    //state text để lấy ra text từ speech
    const [text, setText] = useState('');
    //state isListening để xác định xem micro còn đang lắng nghe không
    const [isListening, setIsListening] = useState(false);
    const transcriptRef = useRef(''); // Sử dụng useRef để giữ giá trị transcript

    // Kiểm tra xem trình duyệt có hỗ trợ Web Speech API không
    const recognition = new (window.SpeechRecognition || window.webkitSpeechRecognition)();
    recognition.lang = 'vi-VN'; // Đặt ngôn ngữ thành tiếng Việt
    recognition.interimResults = true; // Cho phép nhận diện kết quả tức thì

    let sessionTaiKhoan = JSON.parse(sessionStorage.getItem("sessionTaiKhoan"));
    let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));

    //tìm kiếm bằng hình ảnh

    const [imageURL, setImageURL] = useState(null);
    const [results, setResults] = useState([]);
    const classNames = [
        "Banh flan", "Banh mi ngot", "Banh mochi", "Banh tiramisu",
        "Che thai", "Com bo luc lac", "Com ca chien", "Com chien duong chau", "Com ga", "Com tam",
        "Com thit kho", "Com xa xiu", "Kem dua", "Kem socola", "Nuoc ngot 7up", "Nuoc ngot coca-cola",
        "Nuoc ngot pepsi", "Nuoc ngot sprite", "Nuoc tang luc red bull", "Nuoc tang luc sting", "Thit bo ham tieu xanh",
        "Thit heo quay"
    ]

    const classNamesMapping = {
        "Banh flan": "Bánh Flan",
        "Banh mi ngot": "Bánh Mì Ngọt",
        "Banh mochi": "Bánh Mochi",
        "Banh tiramisu": "Tiramisu",
        "Che thai": "Chè Thái",
        "Com bo luc lac": "Cơm Bò Lúc Lắc",
        "Com ca chien": "Cơm Cá Chiên",
        "Com chien duong chau": "Cơm Chiên Dương Châu",
        "Com ga": "Cơm Gà",
        "Com tam": "Cơm Tấm",
        "Com thit kho": "Cơm Thịt Kho",
        "Com xa xiu": "Cơm Xá Xíu",
        "Kem dua": "Kem Dừa",
        "Kem socola": "Kem Socola",
        "Nuoc ngot 7up": "Nước Ngọt 7UP",
        "Nuoc ngot coca-cola": "Nước Ngọt Coca-Cola",
        "Nuoc ngot pepsi": "Nước Ngọt Pepsi",
        "Nuoc ngot sprite": "Nước Ngọt Sprite",
        "Nuoc tang luc red bull": "Nước Tăng Lực Red Bull",
        "Nuoc tang luc sting": "Nước Tăng Lực Sting",
        "Thit bo ham tieu xanh": "Thịt Bò Hầm Tiêu Xanh",
        "Thit heo quay": "Thịt Heo Quay"
    };

    const [isModelLoading, setIsModelLoading] = useState(false);
    const [model, setModel] = useState(null);

    const loadModel = async () => {
        setIsModelLoading(true);
        try {
            const loadedModel = await tflite.loadTFLiteModel(`${process.env.PUBLIC_URL}/assets/model/model_unquant.tflite`);
            console.log("Model loaded:", loadedModel);
            setModel(loadedModel);
        } catch (error) {
            console.error("Model load error:", error);
        } finally {
            setIsModelLoading(false);
        }
    };

    useEffect(() => {
        loadModel();
    }, []);

    const imageRef = useRef();
    const textInputRef = useRef();
    const fileInputRef = useRef();


    const uploadImage = (e) => {
        const { files } = e.target;
        if (files.length > 0) {
            const url = URL.createObjectURL(files[0]);
            setImageURL(url);
        } else {
            setImageURL(null);
        }
    };

    const identify = async () => {
        if (!model) {
            console.error("Model is not loaded yet");
            return;
        }

        console.log(imageRef.current);

        const imgTensor = tf.browser.fromPixels(imageRef.current);
        const resizedImg = tf.image.resizeBilinear(imgTensor, [224, 224]);
        const inputTensor = resizedImg.expandDims(0).toFloat().div(tf.scalar(255));

        try {
            // Run the model and get logits
            const logits = await model.predict(inputTensor);

            // Apply softmax to get probabilities
            const probabilities = tf.softmax(logits.squeeze()).arraySync();

            // Prepare results
            const resultWithConfidence = probabilities.map((prob, index) => ({
                className: classNamesMapping[classNames[index]],
                confidence: prob
            }));

            // Sort results by confidence
            resultWithConfidence.sort((a, b) => b.confidence - a.confidence);

            // Set results in state
            setResults(resultWithConfidence);
            console.log("Prediction results:", resultWithConfidence);

            // Tìm kiếm ngay với tên sản phẩm cần tìm
            transcriptRef.current = resultWithConfidence[0].className; // Lấy tên sản phẩm có độ tin cậy cao nhất
            setTimKiem(transcriptRef.current); // Đặt text từ mic vào thanh tìm kiếm
            clickTimkiem(); // Gọi hàm tìm kiếm với giá trị text
            setTimKiem("");
            setText("");
            transcriptRef.current = "";


        } catch (error) {
            console.error("Prediction error:", error);
        }
    };

    let location = useLocation();
    let navigate = useNavigate();
    const clickMuaNgay = async (item) => {
        if (sessionTaiKhoan && sessionToken) {
            const data = {
                maChiTietGioHang: Math.floor(new Date().getTime() / 1000),
                sanPhamGH: item,
                soLuong: 1,
                taiKhoanGH: sessionTaiKhoan
            }
            sessionStorage.setItem('checkBuyNow', JSON.stringify(true));
            navigate("/home/pay-details", { state: [data] });
        } else {
            navigate("/login", { state: { form: location } })
        }
    };

    const getProductsByPage = (page, pageSize = 6) => {
        return axios.get(`${urlApi}/api/san-pham/page?page=${page}&pageSize=${pageSize}`);
    };
    const getListProductBySapXep = (sapXep, page, pageSize) => {
        return axios.get(`${urlApi}/api/san-pham/get-list-sap-xep?sapXep=${sapXep}&page=${page}&pageSize=${pageSize}`);
    }
    const getListProductByBoLoc = (maLoai, timKiem, minGia, maxGia, page, pageSize) => {
        if (maLoai === undefined) {
            maLoai = "";
        }
        if (timKiem === undefined) {
            timKiem = "";
        }
        if (minGia === undefined) {
            minGia = "";
        }
        if (maxGia === undefined) {
            maxGia = "";
        }
        return axios.get(`${urlApi}/api/san-pham/get-list-by-bo-loc?maLoai=${maLoai}&timKiem=${timKiem}&minGia=${minGia}&maxGia=${maxGia}&page=${page}&pageSize=${pageSize}`)
    }

    const getAllLoaiSanPham = () => {
        return axios.get(`${urlApi}/api/loai-san-pham/list`)
            .then(response => response.data)
            .catch(error => {
                console.error("Lỗi khi lấy dữ liệu loại sản phẩm:", error);
                throw error;
            });
    };

    const loadPage = async (page) => {
        if (boLoc === 'loai') {
            // Phân trang product theo loai sản phẩm khi click vào handlePageClick
            let res = await getListProductByBoLoc(loai.maLoai, '', '', '', page + 1, 6);
            if (res) {
                setProducts(res.data.data.sanPham);
                setTotalPages(res.data.totalPage);
                console.log(res.data.data.sanPham);
            }
        } else if (boLoc === "timkiem") {
            // Phân trang product theo loai sản phẩm khi click vào handlePageClick
            let res = await getListProductByBoLoc('', timKiem, '', '', page + 1, 6);
            if (res) {
                setProducts(res.data.data.sanPham);
                setTotalPages(res.data.totalPage);
            }

        } else if (valueSapXep === "" || valueSapXep === 'banchaynhat') {
            // Phân trang product theo sắp xếp bình thường khi click vào handlePageClick
            setLoading(true);
            try {
                let res = await getProductsByPage(page + 1, 6); // Assuming page size is 6
                if (res.data) {
                    setProducts(res.data.data.sanPham);
                    setTotalPages(res.data.totalPage);
                }
            } catch (error) {
                toast.error("Có lỗi xảy ra khi lấy dữ liệu sản phẩm.");
            } finally {
                setLoading(false);
            }
        } else {
            // Phân trang product bình thường khi click vào handlePageClick
            let res = await getListProductBySapXep(valueSapXep, page + 1, 6);
            if (res) {
                setProducts(res.data.data.sanPham);
                setTotalPages(res.data.totalPage);
            }
        }
    };

    const fetchImages = async () => {
        // try {
        //   const urls = {}; // Create an empty object to store image URLs
        //   for (const product of products) {
        //     var imageRef;
        //     if(product.hinhAnh===null){
        //         imageRef = ref(storage, `AnhSanPham/Image_not_available.png`);
        //     }
        //     else{
        //         imageRef = ref(storage, `AnhSanPham/${product.hinhAnh}`); // Use product.hinhAnh
        //     }
        //     const url = await getDownloadURL(imageRef);
        //     urls[product.hinhAnh] = url; // Store URL by product.hinhAnh
        //   }
        //   setImageUrls(urls); // Set the fetched image URLs
        // } catch (error) {
        //   console.error("Error fetching image URLs:", error);
        // }
    };

    useEffect(() => {
        const fetchData = async () => {
            await loadPage(0); // Load page 1 initially

            await fetchImages();

            try {
                let res = await getAllLoaiSanPham();
                if (res.data && Array.isArray(res.data.loaiSP)) {
                    setLoaiSanPhams(res.data.loaiSP);
                } else {
                    toast.error("Dữ liệu loại sản phẩm không hợp lệ.");
                }
            } catch (error) {
                toast.error("Có lỗi xảy ra khi lấy dữ liệu loại sản phẩm.");
            }
        };

        fetchData();
    }, []);

    useEffect(() => {
        if (products.length > 0) {
            fetchImages(); // Fetch lại URL ảnh từ Firebase mỗi khi trang reload
        }
    }, [products]);

    // sự kiện clich thanh pagination phân trang
    const handlePageClick = (event) => {
        setPageSapXep(event.selected);
        loadPage(event.selected);

    };

    // Sự kiẹn on change để fill product theo sắp xếp
    const clickSapXep = async (event) => {
        let value = event.target.value;
        // Value dong option được chọn
        setValueSapXep(value);
        // Chưa code api để lấy bán chạy nhất
        if (value !== 'banchaynhat') {
            let res = await getListProductBySapXep(value, pageSapXep + 1, 6);
            if (res) {
                setProducts(res.data.data.sanPham);
                setTotalPages(res.data.totalPage);
            }
        }
    }

    // CLick chọn loại để fill sản phẩm theo loại
    const clickChonLoai = async (loai) => {
        // Set để phẩn biệt khi nào là tìm kiếm và loại
        setBoLoc('loai');
        // Set đối tượng loai
        setLoai(loai);
        let res = await getListProductByBoLoc(loai.maLoai, '', '', '', 1, 6);
        if (res) {
            setProducts(res.data.data.sanPham);
            setTotalPages(res.data.totalPage);
        }
    }

    // Click nút tìm kiếm
    const clickTimkiem = async () => {
        const searchText = timKiem || transcriptRef.current
        if (searchText) {
            // Set để phẩn biệt khi nào là tìm kiếm và loại
            setBoLoc('timkiem');
            let res = await getListProductByBoLoc('', searchText, '', '', 1, 6);
            if (res) {
                setProducts(res.data.data.sanPham);
                setTotalPages(res.data.totalPage);
            }
        }
    }

    const timKiemBangGiongNoi = () => {
        if (isListening) {
            recognition.stop(); // Nếu đang nghe, dừng micro
        } else {
            recognition.start(); // Nếu không, bắt đầu nghe
        }
    };

    // Xử lý khi nhận được kết quả từ mic
    recognition.onresult = (event) => {
        let transcript = '';
        for (let i = event.resultIndex; i < event.results.length; ++i) {
            transcript += event.results[i][0].transcript;
        }
        transcriptRef.current = transcript; // Cập nhật giá trị transcriptRef
        setText(transcript);
    };

    // Xử lý khi bắt đầu và kết thúc lắng nghe
    recognition.onstart = () => setIsListening(true);
    recognition.onend = () => {
        setIsListening(false);
        setTimKiem(transcriptRef.current); // Đặt text từ mic vào thanh tìm kiếm
        clickTimkiem(); // Gọi hàm tìm kiếm với giá trị text
        recognition.stop(); // Đảm bảo rằng micro dừng lại sau khi người dùng ngừng nói
        setTimKiem("");
        setText("");
        transcriptRef.current = "";
    };

    const handleFileUpload = () => {

    }


    return (
        <div className='body-product'>
            <Navbar />
            <div className="container product-container mb-4" style={{ paddingTop: 70 }}>
                <div className="row">
                    <div className="col-md-2 mt-3">
                        <div className="filters">
                            <div>
                                <div className="input-group mb-3">
                                    <input
                                        type="search"
                                        className="form-control"
                                        placeholder="Tìm kiếm..."
                                        onChange={(event) => { setTimKiem(event.target.value) }}
                                        value={text ? text : timKiem}
                                    />
                                    <button
                                        className="btn btn-success"
                                        type="button"
                                        onClick={() => { clickTimkiem() }}
                                    >
                                        <i className="fa-solid fa-magnifying-glass"></i>
                                    </button>
                                    <button
                                        className="btn btn-success"
                                        type="button"
                                        onClick={timKiemBangGiongNoi}
                                    >
                                        <i className="fa-solid fa-microphone"></i>
                                    </button>
                                    {/* Nút upload hình ảnh */}
                                    <label className="btn btn-success mb-0">
                                        <i className="fa-solid fa-image"></i>
                                        <input
                                            type="file"
                                            accept="image/*"
                                            capture="camera"
                                            style={{ display: "none" }}
                                            onChange={uploadImage}
                                        />
                                    </label>
                                </div>

                                <h3>Danh mục</h3>
                                <ul className="categories">
                                    {loaiSanPhams.filter(item => item.trangThai === true)
                                        .map(loai => (
                                            <li key={loai.maLoai}>
                                                <a onClick={() => { clickChonLoai(loai) }}>{loai.tenLoai}</a></li>
                                        ))}
                                </ul>

                                {/* <h3>Giá</h3>
                                <div className="form-group">
                                    <input type="number" className="form-control" name="minPrice" id="minPrice" placeholder="Min Price" />
                                    <input type="number" className="form-control" name="maxPrice" id="maxPrice" placeholder="Max Price" />
                                    <input type="range" className="form-control-range" id="priceRange" min="0" max="250000" step="1000" />
                                    <span id="priceRangeLabel">0 VND - 250000 VND</span>
                                </div>
                                <button type="submit" className="btn btn-primary">Lọc</button> */}
                            </div>
                        </div>
                    </div>
                    <div className="col-md-10">
                        <div className="sort-options">
                            <label htmlFor="sort">Sắp xếp theo: </label>
                            <div>
                                <select className="form-control d-inline w-auto" id="sort" name="sort" value={valueSapXep} onChange={(event) => clickSapXep(event)} >
                                    <option value="banchaynhat" >Vui lòng chọn</option>
                                    <option value="giatangdan">Giá tăng dần</option>
                                    <option value="giagiamdan">Giá giảm dần</option>
                                    {/* <option value="moinhat">Mới nhất</option> */}
                                </select>
                            </div>
                        </div>
                       
                        {loading ? 
                            // <div>Loading...</div>
                            <div className='d-flex justify-content-center align-items-center' style={{ minHeight: 310 }}>
                                <FourSquare color="#067A38" size="large" text="" textColor="" />
                            </div>
                            :
                            products.length === 0 ? <div>Không có sản phẩm để hiển thị.</div> :
                                <div className="similar-product row container-center">
                                    {products.filter(item => item.trangThai === true)
                                        .map(product => (
                                            <div key={product.maSanPham} className="col-md-4">
                                                <div className="card">
                                                    <img src={product.hinhAnh} alt={product.tenSanPham} onClick={() => { navigate(`/home/product-details/${product.maSanPham}`) }} />
                                                    <div className="card-body">
                                                        <h3 className="product-name">{product.tenSanPham}</h3>
                                                        <div className="action row">
                                                            <div className="product-price col-md-6">{formatVND(product.gia)}</div>
                                                            <div className="btn btn-success col-md-5" style={{ cursor: 'pointer' }} onClick={() => clickMuaNgay(product)}>Mua ngay</div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        ))}
                                </div>
                        }
                        <div className="pagination justify-content-center">
                            <div className='d-flex justify-content-center' style={{ marginRight: '72px' }}>
                                <ReactPaginate
                                    breakLabel="..."
                                    nextLabel={<i className="fa-solid fa-forward-step"></i>}
                                    onPageChange={handlePageClick}
                                    pageRangeDisplayed={3}
                                    pageCount={totalPage}
                                    previousLabel={<i className="fa-solid fa-backward-step"></i>}
                                    renderOnZeroPageCount={null}
                                    pageClassName='page-item  page-address'
                                    pageLinkClassName='page-link'
                                    previousClassName='page-item page-address'
                                    previousLinkClassName='page-link'
                                    nextClassName='page-item page-address'
                                    nextLinkClassName='page-link'
                                    breakClassName='page-item'
                                    breakLinkClassName='page-link'
                                    containerClassName='pagination'
                                    activeClassName='active'
                                />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            {imageURL && (
                <img
                    src={imageURL}
                    alt="Upload Preview"
                    crossOrigin="anonymous"
                    ref={imageRef}
                    onLoad={identify} // Gọi identify sau khi ảnh đã load xong
                    style={{ display: "none" }}
                />
            )}

            <Footer />
        </div>
    );
};

const createGioHang = (data, token) => {
    return axios.post(`${urlApi}/api/chi-tiet-gio-hang/create`, data, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
};

export default Product;
