import React, { useEffect, useRef, useState } from 'react';
import '../css/ProductDetail.css';
import Navbar from './menu';
import Footer from './footer';
import axios from "axios";
import { useNavigate, useParams, useLocation, NavLink } from 'react-router-dom';
import { format } from 'date-fns';
import { toast } from 'react-toastify';
import formatVND from '../Service/FormatVND';

const urlApi = process.env.REACT_APP_API_URL;
const url = process.env.REACT_APP_URL;
const createGioHang = (data, token) => {
    return axios.post(`${urlApi}/api/chi-tiet-gio-hang/create`, data, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
};


export const ProductDetail = () => {
    const navMenuRef = useRef();
    const [quantity, setQuantity] = useState(1);
    const [evaluates, setEvaluate] = useState([], null);
    const [product, setProduct] = useState([], null);
    const [products, setProducts] = useState([]);
    const [currentPage, setCurrentPage] = useState(1); // Trạng thái trang hiện tại
    const [currentReviewPage, setCurrentReviewPage] = useState(1);
    const [hasMoreProducts, setHasMoreProducts] = useState(true);
    const [hasMoreReviews, setHasMoreReviews] = useState(true);
    const [productImages, setProductImages] = useState([]);
    const [averageRating, setAverageRating] = useState(0);
    const [imageUrls, setImageUrls] = useState({}); // Store image URLs
    const [loading, setLoading] = useState(true);
    const [count1Stars, setCount1Stars] = useState(0);
    const [count2Stars, setCount2Stars] = useState(0);
    const [count3Stars, setCount3Stars] = useState(0);
    const [count4Stars, setCount4Stars] = useState(0);
    const [count5Stars, setCount5Stars] = useState(0);
    const [totalAllStars, setTotalAllStars] = useState(0);
    const [reviewImages, setReviewImages] = useState([]);
    const [loggedInUserId, setLoggedInUserId] = useState('');
    const [adminResponse,setAdminResponse] = useState([]);
    const [initialRatingCounts, setInitialRatingCounts] = useState(null); // Lưu chỉ số ban đầu
    const [checkSoSaoDanhGia, setCheckSoSaoDanhGia] = useState(true);
    let navigate = useNavigate();
    const location = useLocation();
    const currentUrl = window.location.href; // Current product page URL
    const { maSanPham } = useParams(); // Lấy tham số id từ URL
    let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));
    let sessionTaiKhoan = JSON.parse(sessionStorage.getItem("sessionTaiKhoan"));
    const currentProductId = product.maSanPham;

    useEffect(() => {
        const storedProduct = sessionStorage.getItem('currentProduct');
        if (storedProduct) {
            const productData = JSON.parse(storedProduct);
            setProduct(productData);
            setAverageRating(productData.averageRating || 0);
            setTotalAllStars(productData.totalAllStars || 0);
        }

        const sessionData = JSON.parse(sessionStorage.getItem('sessionTaiKhoan'));
        if (sessionData) {
            setLoggedInUserId(sessionData.maTaiKhoan);
        }
        fetchProductDetails(maSanPham);
        fetchReviews(currentReviewPage);
        fetchAdminResponse();
        console.log(adminResponse);

    }, [maSanPham, currentReviewPage]);

    const fetchProductDetails = async (productId) => {
        try {
            const response = await axios.get(`${urlApi}/api/san-pham/get-one?maSanPham=${productId}`);
            const data = response.data;
            if (data && data.data) {
                const productData = data.data.sanPham;
                setEvaluate([]);
                setProduct(productData);
                setCurrentReviewPage(1);
                setAverageRating(data.data.averageRating || 0);
                setTotalAllStars(data.data.totalAllStars || 0);
                sessionStorage.setItem('currentProduct', JSON.stringify(data.data.sanPham));

                const imagesResponse = await axios.get(`${urlApi}/api/hinh-anh-san-pham?maSanPham=${productId}`);
                const imagesData = imagesResponse.data;
                setHasMoreProducts(productData.length > 0);

                if (productData.loaiSP && productData.loaiSP.maLoai) {
                    fetchSimilarProducts(productData.loaiSP.maLoai);
                    console.log('productData.loaiSP.maLoai: ', productData.loaiSP.maLoai);
                }

            } else {
                console.error('Data or sanPham is not an array:', data);
                setEvaluate([]);
                setProductImages([]);
            }

        } catch (error) {
            console.error('Error fetching product details:', error);
            setProductImages([]);
        }
    };

    const clickMuaNgay = async (item) => {

        if (sessionTaiKhoan && sessionToken) {
            let maTaiKhoan = sessionTaiKhoan.maTaiKhoan;
            let res = await createGioHang({
                "soLuong": 1,
                "maTaiKhoan": maTaiKhoan,
                "maSanPham": item.maSanPham
            }, sessionToken);
            if (res) {
                //  toast.success("Thêm vào giỏ hàng thành công");
                sessionStorage.setItem('checkBuyNow', JSON.stringify(true));
                sessionStorage.setItem("listCTGH", JSON.stringify([res.data.data.chiTietGioHang]));
                navigate("/home/pay-details", { state: [res.data.data.chiTietGioHang] });
            }

        } else {
            navigate("/login", { state: { form: location } })
        }

    };

    const fetchSimilarProducts = async (maLoai) => {
        try {
            const response = await axios.get(`${urlApi}/api/san-pham/get-list-by-bo-loc?maLoai=${maLoai}&page=1&pageSize=100`);
            const data = response.data;

            if (data && data.data && Array.isArray(data.data.sanPham)) {
                setProducts(data.data.sanPham);
                console.log('data.data.sanPham: ', data.data.sanPham);
            } else {
                console.error('Data or sanPham is not an array:', data);
                setProducts([]);
            }
        } catch (error) {
            console.error('Error fetching similar products:', error);
            setProducts([]);
        }
    };

    const fetchReviewImages = async (reviewId) => {
        try {
            const response = await axios.get(`${urlApi}/api/hinh-anh-danh-gia/page/1?maDanhGia=${reviewId}`);
            const data = response.data;


            // If the data is valid, proceed to fetch images from Firebase
            if (data && data.data && Array.isArray(data.data.hinhAnhDG)) {
                const imagePromises = data.data.hinhAnhDG.map(async (image) => {

                    return {
                        maDanhGia: image.danhGia.maDanhGia, // Keep track of the review ID
                        tenHinhAnh: image.tenHinhAnh, // The image file name
                        src: image.tenHinhAnh, // The Firebase image URL
                    };
                });

                // Wait for all image URLs to be fetched
                const newImages = await Promise.all(imagePromises);

                // Update the state with the new images
                setReviewImages((prev) => {
                    const allImages = [...prev, ...newImages]; // Combine new images with existing ones
                    // Ensure no duplicate images by filtering unique URLs
                    const uniqueImages = Array.from(new Set(allImages.map(img => img.src)))
                        .map(src => allImages.find(img => img.src === src));

                    return uniqueImages;
                });
            }
        } catch (error) {
            console.error("Failed to fetch review images from Firebase:", error);
        }
    };

    const fetchAdminResponse = async()=>{
        try {
            const response = await axios.get(`${urlApi}/api/danh-gia/list`,{
                headers:{
                    Authorization: `Bearer ${sessionToken}`
                }
            });
            setAdminResponse(response.data.adminResponse);
            console.log(adminResponse)
        } catch (error) {
            console.log(error)
        }
    }

    const fetchReviews = async (page, rating = '') => {
        try {
            const urlGetApi = rating
                ? `${urlApi}/api/danh-gia/page?page=${page}&pageSize=5&soSao=${rating}&maSanPham=${maSanPham}`
                : `${urlApi}/api/danh-gia/page/${page}?maSanPham=${maSanPham}`;

            const response = await axios.get(urlGetApi);
            console.log(response);

            const data = response.data;
            if (data && data.data && Array.isArray(data.data.danhGia)) {
                setEvaluate(prev => {
                    const newReviews = data.data.danhGia.filter(r => !prev.some(existingReview => existingReview.maDanhGia === r.maDanhGia));
                    newReviews.forEach(review => fetchReviewImages(review.maDanhGia));
                    return [...prev, ...newReviews];
                });
                console.log("Đây là reviews: ", response.data.data)
                console.log("Đây là reviews: ", response)
                setHasMoreReviews(data.data.danhGia.length > 0);
                if (initialRatingCounts === null) {
                    // updateRatingCounts(data.data.danhGia);
                    setCheckSoSaoDanhGia(true);
                    setInitialRatingCounts(data.data.danhGia);
                }

                if (checkSoSaoDanhGia || initialRatingCounts === null) {
                    const danhGias = data.data.danhGia;
                    const avgRating = calculateAverageRating(danhGias);
                    setInitialRatingCounts(data.data.danhGia);
                    const formattedRating = avgRating.toFixed(1);
                    setAverageRating(parseFloat(formattedRating));
                    const counts = data.data.danhGia.reduce((acc, review) => {
                        if (review.soSao === 1) acc[1]++;
                        if (review.soSao === 2) acc[2]++;
                        if (review.soSao === 3) acc[3]++;
                        if (review.soSao === 4) acc[4]++;
                        if (review.soSao === 5) acc[5]++;
                        return acc;
                    }, { 1: 0, 2: 0, 3: 0, 4: 0, 5: 0 });

                    setCount1Stars(counts[1]);
                    setCount2Stars(counts[2]);
                    setCount3Stars(counts[3]);
                    setCount4Stars(counts[4]);
                    setCount5Stars(counts[5]);
                    const totalReviews = counts[1] + counts[2] + counts[3] + counts[4] + counts[5];
                    setTotalAllStars(totalReviews);
                }
            } else {
                setEvaluate([]);
                setHasMoreReviews(false);
            }
        } catch (error) {
            setEvaluate([]);
            setHasMoreReviews(1);
        }
    };

    const handleRatingFilter = (rating) => {
        setCheckSoSaoDanhGia(false);
        setEvaluate([]); // Clear current reviews
        setCurrentReviewPage(1); // Reset to first page
        fetchReviews(currentReviewPage, rating); // Fetch reviews based on selected rating
    };

    const calculateAverageRating = (danhGias) => {
        if (danhGias.length === 0) return 0;
        // Tính tổng số sao
        const totalStars = danhGias.reduce((acc, review) => acc + review.soSao, 0);
        // Tính trung bình số sao
        return totalStars / danhGias.length;
    };


    const loadMoreReviews = (e) => {
        setCheckSoSaoDanhGia(true);
        e.preventDefault();
        if (hasMoreReviews) {
            setCurrentReviewPage(prevPage => prevPage + 1);
        }
    };
    const changeQuantity = (amount, action) => {
        if (product.soLuong === quantity && action === "cong") {
            amount = 0;
        }
        if (product.soLuong === quantity && action === "tru") {
            amount = -1;
        }
        setQuantity((prevQuantity) => {
            const newQuantity = prevQuantity + amount;
            return Math.max(1, Math.min(10, newQuantity)); // Giới hạn giá trị từ 1 đến 10
        });
    };

    const changeImage = (imageSrc) => {
        document.getElementById('largeImage').src = `image/AnhSanPham/${imageSrc}`;
    };


    const renderStars = (rating) => {
        const stars = [];
        for (let i = 1; i <= 5; i++) {
            if (rating >= i) {
                stars.push(<i key={i} className="fa fa-star text-warning"></i>);
            } else if (rating >= (i - 0.5)) {
                stars.push(<i key={i} className="fa fa-star-half-alt text-warning"></i>);
            } else {
                stars.push(<i key={i} className="fa fa-star text-secondary"></i>);
            }
        }
        return stars;
    };
    document.addEventListener('DOMContentLoaded', () => {
        document.getElementById('congSL').addEventListener('click', () => {
            var value = parseInt(document.getElementById('soLuong').value);
            value++;
            document.getElementById('soLuong').value = value;
        });

        document.getElementById('truSL').addEventListener('click', () => {
            var value = parseInt(document.getElementById('soLuong').value);
            value = isNaN(value) ? 1 : value;
            if (value > 1) {
                value--;
                document.getElementById('soLuong').value = value;
            }
        });
    });
    const clickProduct = (productId) => {
        setEvaluate([]);
        fetchProductDetails(productId);
        setCheckSoSaoDanhGia(true);
        window.scrollTo({ top: 0, behavior: 'smooth' });
        navigate(`/home/product-details/${productId}`);

        // window.location.reload();
        window.scrollTo(0, 0);

    };


    const clickEdit = (evaluateId) => {
        fetchReviews(1,evaluateId);
        navigate(`/home/edit-evaluate/${evaluateId}`);
    };

    const clickAddProductCart = async (item) => {
        if (sessionTaiKhoan && sessionToken) {
            let maTaiKhoan = sessionTaiKhoan.maTaiKhoan;
            let res = await createGioHang({
                "soLuong": quantity,
                "maTaiKhoan": maTaiKhoan,
                "maSanPham": item.maSanPham
            }, sessionToken);
            if (res) {
                if(navMenuRef.current){
                    navMenuRef.current.loadListCartMenu(sessionTaiKhoan, sessionToken);
                }
                toast.success("Thêm vào giỏ hàng thành công");
            }
        } else {
            navigate("/login", { state: { form: location } })
        }

    }
    const clickPay = async (item) => {
        if (sessionTaiKhoan && sessionToken) {
            const data = {
                maChiTietGioHang: Math.floor(new Date().getTime() / 1000),
                sanPhamGH: item,
                soLuong: quantity,
                taiKhoanGH: sessionTaiKhoan
            }
            sessionStorage.setItem('checkBuyNow', JSON.stringify(true));
            navigate("/home/pay-details", { state: [data]});
        } else {
            navigate("/login", { state: { form: location } })
        }
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return format(date, 'dd/MM/yyyy'); // Format date as dd-MM-yyyy
    };
    const [currentStarFilter, setCurrentStarFilter] = useState('');
    const filteredEvaluates = currentStarFilter
        ? evaluates.filter(eva => eva.soSao === currentStarFilter)
        : evaluates;


    if (!product) {
        return <div>Loading...</div>;
    }


    return (
        <> <Navbar ref={navMenuRef}/>
            <div className="bg-content product-details-content pb-5" style={{ paddingTop: 50 }}>
                <main className="container">
                    <div className="detail row">
                        <div className="image-product col-md-5">
                            {/* {productImages.length > 0 && ( */}
                            <img
                                className="large-image" id="largeImage"
                                src={product.hinhAnh}
                                alt={product.tenSanPham}
                            />
                        </div>
                        <div className="detail-product col-md-6">
                            <div className="name-product">{product.tenSanPham}</div>
                            <div className="star">
                                <b className="number-star" style={{ fontSize: '25px' }}>{averageRating} </b>
                                <span style={{ fontSize: '20px' }}>
                                    {renderStars(parseFloat(averageRating))}
                                </span>
                                <span style={{ margin: '0 10px', color: '#979797' }}> |</span>
                                <span className="number-evaluate">{totalAllStars}</span>
                                <span className="text-number-evaluate"> Đánh giá</span>
                            </div>
                            <div className="detail-product2">
                                <div className="price">
                                    <span> {formatVND(product.gia)}</span>
                                </div>
                                <div className="description">
                                    <span>{product.moTa}</span>
                                </div>
                                <div>
                                    <div className="salary" >
                                        <div>Số lượng</div>
                                        <div className="box-add-salary">
                                            <button type="button" className="btn btn-sm" onClick={() => { changeQuantity(-1, "tru") }} >-</button>
                                            <input type="text" className="form-control" name="soLuong" value={quantity} min="1" max="5" style={{ width: '50px' }} id="soLuong" />
                                            <button type="button" className="btn btn-sm" onClick={() => { changeQuantity(1, "cong") }} >+</button>
                                        </div>
                                    </div>
                                    <div className="button-btn">
                                        <button className="btn btn-add-cart btn-outline-success" onClick={() => { clickAddProductCart(product) }}>
                                            <i className="fa-solid fa-cart-plus"></i> Thêm Vào giỏ hàng
                                        </button>
                                        <button className="btn btn-buy btn-success" onClick={() => { clickPay(product) }}>Mua ngay</button>
                                    </div>
                                </div>
                                <div style={{ margin: '20px 0 0 0' }}>
                                    <span style={{ margin: '0 40px 0 0' }}>
                                        <i style={{ color: 'red' }} className="fa-solid fa-medal"></i> Hàng chất lượng 100%
                                    </span>
                                    <span style={{ margin: '0 40px 0 0' }}>
                                        <i style={{ color: 'red' }} className="fa-solid fa-clock"></i> Giao hạng nhanh trong 30 phút
                                    </span>
                                </div>

                            </div>
                        </div>
                    </div>

                    <div className="evaluate mt-3">
                        <div className="mb-1" style={{ fontSize: '30px', paddingLeft:28 }}>ĐÁNH GIÁ SẢN PHẨM</div>
                        <div className="detail-evaluate-star">
                            <span className="number-star">{averageRating}</span>
                            <span style={{ color: '#FAA41A', fontSize: '24px', margin: '0 200px 0 0' }}> trên 5</span>
                            <form method="get">
                                <button type="button" name="soSao" value="" className="btn btn-outline-success me-2" onClick={() => handleRatingFilter('')}>Tất Cả</button>
                                <button type="button" name="soSao" value="5" className="btn btn-outline-success me-2" onClick={() => handleRatingFilter('5')}>5 sao ({count5Stars})</button>
                                <button type="button" name="soSao" value="4" className="btn btn-outline-success me-2" onClick={() => handleRatingFilter('4')}>4 sao ({count4Stars})</button>
                                <button type="button" name="soSao" value="3" className="btn btn-outline-success me-2" onClick={() => handleRatingFilter('3')}>3 sao ({count3Stars})</button>
                                <button type="button" name="soSao" value="2" className="btn btn-outline-success me-2" onClick={() => handleRatingFilter('2')}>2 sao ({count2Stars})</button>
                                <button type="button" name="soSao" value="1" className="btn btn-outline-success" onClick={() => handleRatingFilter('1')}>1 sao ({count1Stars})</button>
                            </form>
                            <div style={{ fontSize: '30px', margin: '-20px 0 0 40px' }}>
                                {renderStars(parseFloat(averageRating))}
                            </div>
                        </div>
                        <div className="detail-evaluate">
                            <div className="detail-evaluate">
                                <div className="card card-evaluate">
                                    {filteredEvaluates.length > 0 ? (
                                        Array.isArray(evaluates) && evaluates.length > 0 && evaluates.map(eva => (
                                            <div className="card-body content row" key={eva.maDanhGia}>
                                                <div className="col-md-1 mt-2 ms-3 me-3">
                                                    <img 
                                                        alt="" 
                                                        src={eva.taiKhoanDG?.hinhAnh || 'https://cellphones.com.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg'} 
                                                        width="50px" 
                                                        height="50px" 
                                                        style={{ borderRadius: '50%' }} 
                                                    />
                                                </div>
                                                <div className="col-md-9 mt-2">
                                                    <div className="name">{eva.taiKhoanDG?.hoVaTen}</div>
                                                    <div className="review-rating">
                                                        {renderStars(eva.soSao)}
                                                    </div>
                                                    <div className="time-date">{formatDate(eva.ngayDanhGia)}</div>
                                                </div>
                                                <div className="body-content col-md-12" style={{ fontSize: '20px' }}>
                                                    <div className="mb-3">{eva.binhLuan}</div>

                                                    {reviewImages.filter(image => image.maDanhGia === eva.maDanhGia).map(image => (
                                                        <span key={image.tenHinhAnh}>
                                                            <img 
                                                                src={image.tenHinhAnh} 
                                                                alt="" 
                                                                width="75px" 
                                                                height="75px" 
                                                                style={{ marginRight: '10px' }} 
                                                            />
                                                        </span>
                                                    ))}

                                                    {/* Phản hồi của admin */}
                                                    <div className="admin-replies mt-3">
                                                        <h5 className="text-success">Phản hồi từ Admin:</h5>
                                                        {adminResponse && adminResponse.length > 0 ? (
                                                            adminResponse
                                                                .filter(reply => reply.maTaiKhoan?.maTaiKhoan === eva.taiKhoanDG?.maTaiKhoan) // Lọc phản hồi phù hợp
                                                                .map((reply, index) => (
                                                                    <div className="admin-reply" key={index}>
                                                                        <strong>Admin:</strong> <span>{reply.noiDungPhanHoi}</span>
                                                                    </div>
                                                                ))
                                                        ) : (
                                                            <div className="no-replies text-muted">Chưa có phản hồi nào từ Admin</div>
                                                        )}
                                                    </div>

                                                    {/* Nút chỉnh sửa dành cho người dùng đang đăng nhập */}
                                                    {eva.taiKhoanDG?.maTaiKhoan === loggedInUserId && (
                                                        <div className='danhGiaBtn'>
                                                            <a href="#" onClick={() => clickEdit(eva.maDanhGia)}>
                                                                <i className="fa-regular fa-pen-to-square"></i>
                                                            </a>
                                                        </div>
                                                    )}
                                                </div>
                                            </div>
                                        ))
                                    ) : (
                                        <div className="no-reviews-message text-center">Không có đánh giá</div>
                                    )}
                                </div>
                            </div>
                        </div>

                        {filteredEvaluates.length > 0 && (
                            <div className="see-more">
                                <form action="" method="post">
                                    <input type="hidden" name="sanPhamId" value={product.maSanPham} />
                                    <button onClick={loadMoreReviews} className="btn btn-success xemthem">Xem Thêm</button>
                                </form>
                            </div>
                        )}
                    </div>

                    <div className="similar-product-details row">
                        <div className='mb-2' style={{ fontSize: '30px' }}>SẢN PHẨM TƯƠNG TỰ</div>
                        {products.length > 0 ? (
                            products.filter(product => product.maSanPham !== currentProductId)
                            .map(product => (
                                <div className="col-md-3 card-product" key={product.maSanPham}>
                                    <div className="card card-sp">
                                        <img src={product.hinhAnh} className="card-img-top" alt={product.tenSanPham} onClick={() => clickProduct(product.maSanPham)}/>
                                        {/* </a> */}
                                        <div className="card-body">
                                            <h3 className="product-name">{product.tenSanPham}</h3>
                                            <div className="action row me-1">
                                                <div className="product-price col-md-7 m-0 d-flex justify-content-center align-items-center">{formatVND(product.gia)}</div>
                                                <div className="btn btn-success col-md-5 m-0 d-flex justify-content-center text-center" style={{ cursor: 'pointer' }} onClick={() => clickMuaNgay(product)}>Mua ngay</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            ))
                        ) : (
                            <div className="text-center">Không có sản phẩm để hiển thị.</div>
                        )}

                        {/* <div className="see-more">
                            <button className="btn btn-success xemthem" onClick={handleSeeMore}>Xem Thêm</button>
                        </div> */}
                    </div>
                </main>
            </div>
            <Footer />
        </>
    );
}



