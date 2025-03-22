import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams, useNavigate, NavLink } from 'react-router-dom';
import '../css/Evaluate.css';
import Navbar from './menu';
import Footer from './footer';
import { toast } from 'react-toastify';
import Numeral from 'react-numeral';
import { getDownloadURL, ref, uploadBytes } from 'firebase/storage';
import { storage } from './Firebase';
import { v4 as uuidv4 } from 'uuid';

const urlApi = process.env.REACT_APP_API_URL;
const urlApiPython = process.env.REACT_APP_API_URL_PYTHON;
const createHinhAnh = (maDanhGia, formData, token) => {
    return axios.post(`${urlApi}/api/hinh-anh-danh-gia/create?maDanhGia=${maDanhGia}`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
            'Authorization': `Bearer ${token}`
        }
    });
}

export const Evaluate = () => {

    const [product, setProduct] = useState([]);
    const [images, setImages] = useState([null, null, null, null]);
    const [content, setContent] = useState('');
    const [rating, setRating] = useState(null);
    const [message, setMessage] = useState("");
    const [imageInputVisibility, setImageInputVisibility] = useState([true, false, false, false]);
    const navigate = useNavigate();
    const { maSanPham, soLuong, maHoaDon} = useParams();

    const sessionToken = JSON.parse(sessionStorage.getItem('sessionTokenTK'));
    useEffect(() => {
        fetchProductDetails(maSanPham);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }, [maSanPham]);

    const handleFileChange = async(event, index) => {
        let file = event.target.files[0];
        if (!file) return;

        //đánh giá nsfw và violence
        const formData = new FormData();
        formData.append("file",file)
        try {
            const response = await axios.post(`${urlApiPython}/analyze_image`, formData, {
              headers: {
                "Content-Type": "multipart/form-data",
              },
            });
            console.log(response)
            const nsfw = response.data.data.nsfw_detection
            const violence = response.data.data.violence_detection
            if(nsfw === 'nsfw'){
                toast.error("Hình ảnh chứa nội dung khiêu dâm nên đã bị xóa!");
                file=null;
                return;
            }
            else if(violence === 'violent'){
                toast.error("Hình ảnh chứa nội dung bạo lực nên đã bị xóa!");
                file=null;
                return;
            }
          } catch (error) {
            console.error("Error sending image:", error.message);
        }

        const newImages = [...images];
        newImages[index] = file;

        const reader = new FileReader();
        reader.onload = () => {
            if (reader.readyState === 2) {
                const previewImageId = `previewImage${index + 1}`;
                document.getElementById(previewImageId).src = reader.result;
            }
        };
        reader.readAsDataURL(file);
        console.log(newImages);
        setImages(newImages);
        if (index < 3) {
            const newVisibility = [...imageInputVisibility];
            newVisibility[index + 1] = true;
            setImageInputVisibility(newVisibility);
        }
    };

    const fetchProductDetails = async (productId) => {
        try {
            const response = await axios.get(`${urlApi}/api/san-pham/get-one?maSanPham=${productId}`);
            const data = response.data;
            if (data && data.data) {
                setProduct(data.data.sanPham);
                sessionStorage.setItem('currentProduct', JSON.stringify(data.data.sanPham));
            }
        } catch (error) {
            console.error('Error fetching product details:', error);
        }
    };

    const getAccountId = () => {
        const sessionData = sessionStorage.getItem('sessionTaiKhoan');
        if (sessionData) {
            const parsedData = JSON.parse(sessionData);
            return parsedData.maTaiKhoan; // Trả về maTaiKhoan từ đối tượng
        }
        return null; // Xử lý trường hợp không có dữ liệu phiên
    };

    const handleFormSubmit = async (event) => {
        event.preventDefault();
        
        if (!rating) {
            toast.error("Vui lòng chọn sao đánh giá");
            return;
        }
        if (!content) {
            toast.error("Vui lòng nhập nội dung");
            return;
        }
        const maTaiKhoan = getAccountId();
        if (!maTaiKhoan) {
            toast.error("Không tìm thấy thông tin tài khoản");
            return;
        }
    
        // Tạo đối tượng chứa dữ liệu đánh giá
        const reviewData = {
            soSao: rating,
            binhLuan: content,
            maSanPham: parseInt(maSanPham),
            maTaiKhoan: maTaiKhoan,
            maHoaDon: maHoaDon,
          
        };
        console.log('maHoaDon', maHoaDon)  
        
        try {
            console.log(reviewData.binhLuan);
            const reviewLabel = await axios.post(`${urlApiPython}/reviewLabel`,{review: reviewData.binhLuan},{
                headers: {
                  'Content-Type': 'application/json',
                },
            });

            console.log(reviewLabel.data.label);

            const label = reviewLabel.data.label;

            if(label === 'toxic'){
                toast.error("Bình luận của bạn đã bị xóa vì chứa 'Ngôn từ gây thù ghét'");
                setContent("");
                reviewData.binhLuan = "";
                return;
            }
            else if(label === 'Off-topic'){
                toast.error("Bình luận của bạn được gắn mắc là 'Không liên quan đến ngữ cảnh'")
                return;
            }

            // Tạo đánh giá trước
            const reviewResponse = await axios.post(`${urlApi}/api/danh-gia/create`, reviewData, {
                headers: {
                    'Authorization': `Bearer ${sessionToken}`
                }
            });
            console.log('reviewData: ', reviewData);
            // const data = reviewResponse.data.data;
            const maDanhGia = reviewResponse.data.data.danhGia.maDanhGia;
    
            // Tạo danh sách promise để upload ảnh lên Firebase và lấy URL
            const imageUploadPromises = images.map(async (image, index) => {
                if (image) {
                    // Tạo tên file unique với uuid
                    const fileNameSplit = image.name.split('.');
                    const fileExtension = fileNameSplit.pop(); // Lấy phần mở rộng
                    const fileNameWithoutExtension = fileNameSplit.join('.'); // Lấy tên file không có extension
                    const newUniqueFileName = `${fileNameWithoutExtension}_${uuidv4()}.${fileExtension}`;
    
                    // Tạo reference trên Firebase Storage
                    const storageRef = ref(storage, `AnhDanhGia/${newUniqueFileName}`);
    
                    // Upload ảnh lên Firebase
                    await uploadBytes(storageRef, image);
    
                    // Lấy URL đầy đủ của ảnh đã tải lên
                    const imageURL = await getDownloadURL(storageRef);
    
                    // Lưu URL ảnh cùng với maDanhGia vào backend
                    const formData = new FormData();
                    formData.append('file', image);  // Nếu cần giữ lại file, nhưng bạn đã có imageURL
                    formData.append('url', imageURL); // Thêm URL Firebase thay vì file
                    formData.append('maDanhGia', maDanhGia); // Thêm maDanhGia
    
                    return axios.post(`${urlApi}/api/hinh-anh-danh-gia/create`, formData, {
                        headers: {
                            'Content-Type': 'multipart/form-data',
                            'Authorization': `Bearer ${sessionToken}`
                        }
                    });
                }
            });
    
            // Chờ tất cả ảnh được upload và lưu URL
            await Promise.all(imageUploadPromises);
    
            toast.success("Đánh giá thành công");
            setTimeout(async () => {
                navigate(`/home/product-details/${maSanPham}`);
            }, 2500);
        } catch (error) {
            console.error("Error submitting review or uploading images:", error); 
            toast.error("Đánh giá không thành công");
        }
    };

    const handleImageError = (event) => {
        event.target.src = `${product.tenHinhAnh || 'default-image.png'}`;
    };

    return (
        <><Navbar />

            <div className="main" style={{marginTop:50}}>
                <b className='text-center mt-3 mb-3'>ĐÁNH GIÁ SẢN PHẨM</b>
                <form onSubmit={handleFormSubmit}>
                    <table className="table">
                        <thead>
                            <tr>
                                <th>Ảnh</th>
                                <th>Tên Sản Phẩm</th>
                                <th>Số Lượng</th>
                                <th>Số Tiền</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr className="cart-item">
                                <td className="align-content-center">
                                    <div className="product-info">
                                        <span>
                                            <img alt="" src={product.hinhAnh} />
                                        </span>
                                    </div>
                                </td>
                                <td className="align-content-center">{product.tenSanPham}</td>
                                <td className="align-content-center">{soLuong}</td>
                                <td className="align-content-center" style={{ color: 'red' }}>
                                    <span className="product-price"><Numeral value={soLuong * product.gia} format={"0,0"} /> VNĐ</span>
                                </td>
                            </tr>
                        </tbody>
                    </table>

                    <div className="stars">
                        {[5, 4, 3, 2, 1].map((star) => (
                            <React.Fragment key={star}>
                                <input
                                    className="star"
                                    value={star}
                                    id={`star-${star}`}
                                    type="radio"
                                    name="star"
                                    required
                                    checked={rating === star}
                                    onChange={() => setRating(star)}
                                />
                                <label className="star" htmlFor={`star-${star}`}></label>
                            </React.Fragment>
                        ))}
                    </div>

                    <div className="content">
                        <input name="masanpham" className="mb-3" type="number" hidden value={product.maSanPham} />
                        <label style={{ marginTop: '50px' }}>Hình ảnh</label>
                        <div className="image-input">
                            {[0, 1, 2, 3].map((i) => (
                                imageInputVisibility[i] && (
                                    <div className="image-container" key={i}>
                                        <img
                                            id={`previewImage${i + 1}`}

                                             src={`https://t4.ftcdn.net/jpg/04/81/13/43/360_F_481134373_0W4kg2yKeBRHNEklk4F9UXtGHdub3tYk.jpg`}
                                            alt="Chọn ảnh"
                                        />

                                        <input
                                            name={`image${i + 1}`}
                                            id={`image${i + 1}`}
                                            className="mb-3"
                                            type="file"
                                            onError={handleImageError}
                                            onChange={(e) => handleFileChange(e, i)}
                                            style={{ width: '150px' }}
                                        />
                                    </div>
                                )
                            ))}
                        </div>
                        <textarea
                            name="content"
                            rows="5"
                            cols="80"
                            placeholder="Nội dung"
                            value={content}
                            onChange={(e) => setContent(e.target.value)}
                        />
                        <br />
                        <div className="content-btn mt-3" style={{ textAlign: 'right' }}>
                            <button className="btn btn-out btn-secondary me-3" type="button" onClick={() => window.history.back()}>
                                Thoát
                            </button>
                            {/* <NavLink className="btn btn-sm btn-review" to={`/home/evaluate/${maDanhGia}`} >  <button className="btn btn-agree btn-warning" type="submit">
                                Đánh giá
                            </button></NavLink> */}
                            <button className="btn btn-agree btn-warning" type="submit">
                                Đánh giá
                            </button>
                        </div>
                    </div>
                </form>
            </div>
            <Footer />
        </>
    );
};

