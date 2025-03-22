import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../css/Evaluate.css';
import Navbar from './menu';
import Footer from './footer';
import { toast } from 'react-toastify';
import { useNavigate, useParams } from 'react-router-dom';
import { getDownloadURL, ref, uploadBytes, deleteObject } from 'firebase/storage';
import { storage } from './Firebase';
import { v4 as uuidv4 } from 'uuid';

const urlApi = process.env.REACT_APP_API_URL;
const urlApiPython = process.env.REACT_APP_API_URL_PYTHON;

export const EditEvaluate = () => {
    const [evaluate, setEvaluate] = useState({});
    const [product, setProduct] = useState({});
    const [images, setImages] = useState([{ file: null, url: null }, { file: null, url: null }, { file: null, url: null }, { file: null, url: null }]);
    const [soLuong, setSoLuong] = useState(0);
    const [tongTien, setTongTien] = useState(0);
    const [imageInputVisibility, setImageInputVisibility] = useState([true, false, false, false]);

    const navigate = useNavigate();

    const { maDanhGia, productId } = useParams();
    let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));

    useEffect(() => {
        const fetchEvaluateData = async () => {
            try {
                const response = await axios.get(`${urlApi}/api/danh-gia/get-one?maDanhGia=${maDanhGia}`, {
                    headers: {
                        'Authorization': `Bearer ${sessionToken}`,
                    },
                });
                const data = response.data.data.danhGia;
                if (data) {
                    setEvaluate(data);
                    setProduct(data.sanPhamDG);
                    setSoLuong(data.sanPhamDG.soLuong);
                    setTongTien(data.sanPhamDG.gia * 1);

                    // Fetch evaluation images
                    const imageResponse = await axios.get(`${urlApi}/api/hinh-anh-danh-gia/page/1?maDanhGia=${maDanhGia}`, {
                        headers: {
                            'Authorization': `Bearer ${sessionToken}`,
                        },
                    });
                    const imageData = imageResponse.data.data.hinhAnhDG;
                    if (imageData && imageData.length > 0) {
                        const newImages = imageData.map((img) => ({
                            file: null, // Initial state for file upload
                            url: `${img.tenHinhAnh}`,
                            maHinhAnhDG: img.maHinhAnhDG,
                        }));
                        setImages(newImages);
                    }

                    // Set visibility of image inputs
                    const newVisibility = Array(4).fill(false);
                    if (imageData.length < 4) {
                        newVisibility[imageData.length] = true;
                    }
                    setImageInputVisibility(newVisibility);

                } else {
                    console.error('Data or sanPhamDG is not an object:', data);
                }
            } catch (error) {
                console.error('Error fetching evaluate data', error);
            }
        };

        fetchEvaluateData(maDanhGia);
    }, [maDanhGia, productId]);

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
        newImages[index] = {
            file: file, // Store the new file
            maHinhAnhDG: newImages[index] ? newImages[index].maHinhAnhDG : undefined,
        };

        const reader = new FileReader();
        reader.onload = () => {
            if (reader.readyState === 2) {
                const previewImageId = `previewImage${index + 1}`;
                document.getElementById(previewImageId).src = reader.result;
                newImages[index].file = file;// Cập nhật preview của hình ảnh
            }
        };
        reader.readAsDataURL(file);
        setImages(newImages);
        if (index < 3) {
            const newVisibility = [...imageInputVisibility];
            newVisibility[index + 1] = true;
            setImageInputVisibility(newVisibility);
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
        const maTaiKhoan = getAccountId();
        if (!maTaiKhoan) {
            toast.error("Không tìm thấy thông tin tài khoản");
            return;
        }

        const reviewData = {
            maDanhGia: maDanhGia,
            soSao: evaluate.soSao || 0,
            binhLuan: evaluate.binhLuan || '',
            maSanPham: product.maSanPham,
            maTaiKhoan: maTaiKhoan,
        };

        try {

            const reviewLabel = await axios.post(`${urlApiPython}/reviewLabel`,{review: reviewData.binhLuan},{
                headers: {
                  'Content-Type': 'application/json',
                },
            });

            console.log(reviewLabel.data.label);

            const label = reviewLabel.data.label;

            if(label === 'toxic'){
                toast.error("Bình luận của bạn đã bị xóa vì chứa 'Ngôn từ gây thù ghét'");
                setEvaluate({ ...evaluate, binhLuan: "" })
                reviewData.binhLuan = "";
                return;
            }

            const response = await axios.put(`${urlApi}/api/danh-gia/update`, reviewData, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${sessionToken}`,
                },
            });

            if (response.status === 200) {
                const imageUploadPromises = images.map(async (image, index) => {
                    if (image && image.file) {
                        // Nếu có hình ảnh cũ, tiến hành xóa trên Firebase
                        if (image.url) {
                            const oldImageRef = ref(storage, image.url); // Tham chiếu đến ảnh cũ
                            await deleteObject(oldImageRef).catch((error) => {
                                console.error('Error deleting old image:', error);
                            });
                        }

                        // Upload ảnh mới lên Firebase
                        const storageRef = ref(storage, `AnhDanhGia/${uuidv4()}`); // Đặt tên cho hình ảnh mới
                        const snapshot = await uploadBytes(storageRef, image.file); // Tải ảnh lên Firebase
                        const newImageUrl = await getDownloadURL(snapshot.ref); // Lấy URL ảnh mới

                        // Cập nhật URL ảnh mới vào hệ thống qua API
                        const formData = new FormData();
                        formData.append('file', image.file); // Add the new image file to the FormData
                        formData.append('url', newImageUrl); // Thêm URL mới vào API
                        if (image.maHinhAnhDG) {
                            return axios.put(`${urlApi}/api/hinh-anh-danh-gia/update?maDanhGia=${maDanhGia}&maHinhAnhDG=${image.maHinhAnhDG}`, formData, {
                                headers: {
                                    'Content-Type': 'multipart/form-data',
                                    'Authorization': `Bearer ${sessionToken}`, // Ensure token is sent
                                },
                            });
                        } else {
                            console.error("maHinhAnhDG is undefined for an image."); // Thêm thông báo lỗi
                        }
                    }
                });

                // Chờ tất cả các ảnh được upload lên và cập nhật API
                await Promise.all(imageUploadPromises);

                console.log('product.maSanPham', product.maSanPham)
                toast.success("Cập nhật thành công");
                setTimeout(() => {
                    navigate(`/home/product-details/${product.maSanPham}`);
                }, 2500);
            } else {
                toast.error("Cập nhật thất bại");
            }
        } catch (error) {
            console.error('Error updating evaluate or uploading images', error);
            toast.error("Cập nhật thất bại");
        }
    };


    return (

        <>
            <Navbar />
            <div className="main" style={{marginTop:50}}>
                <b className='text-center mt-3 mb-3'>CHỈNH SỬA ĐÁNH GIÁ</b>
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
                                    <span><img alt="" src={product.hinhAnh ? `${product.hinhAnh}` : 'https://t4.ftcdn.net/jpg/04/81/13/43/360_F_481134373_0W4kg2yKeBRHNEklk4F9UXtGHdub3tYk.jpg'} width="150px" /></span>
                                </div>
                            </td>
                            <td className="align-content-center">{product.tenSanPham}</td>
                            <td className="align-content-center">{1}</td>
                            <td className="align-content-center" style={{ color: 'red' }}>
                                {tongTien.toLocaleString()} đ
                            </td>
                        </tr>
                    </tbody>
                </table>
                <form onSubmit={handleFormSubmit} encType="multipart/form-data">
                    <div className="content">
                        <div className="stars">
                            {[5, 4, 3, 2, 1].map((star) => (
                                <React.Fragment key={star}>
                                    <input
                                        className="star"
                                        id={`star-${star}`}
                                        type="radio"
                                        name="star"
                                        value={star}
                                        checked={evaluate.soSao === star}
                                        onChange={() => setEvaluate({ ...evaluate, soSao: star })}
                                    />
                                    <label className="star" htmlFor={`star-${star}`} />
                                </React.Fragment>
                            ))}
                        </div>


                        <label className='content-lable' style={{ marginTop: '50px' }}>Hình ảnh</label>
                        <div className="image-input">
                            {images.map((preview, i) => (
                                <div className="image-container" key={i}>
                                    <img
                                        id={`previewImage${i + 1}`}
                                        src={preview?.file ? URL.createObjectURL(preview.file) : preview?.url || 'default_image_url'}
                                        alt="Chọn ảnh"
                                        width="150px" />
                                    {(i === 0 || imageInputVisibility[i]) && (
                                        <input
                                            name={`image${i + 1}`}
                                            id={`image${i + 1}`}
                                            className="mb-3"
                                            type="file"
                                            onChange={(e) => handleFileChange(e, i)}
                                            style={{ width: '150px' }}
                                        />
                                    )}
                                </div>
                            ))}
                        </div>
                        <textarea
                            name="content"
                            rows="5"
                            cols="80"
                            placeholder="Nội dung"
                            value={evaluate.binhLuan}
                            onChange={(e) => setEvaluate({ ...evaluate, binhLuan: e.target.value })}
                        />
                        <br />

                        <div className="content-btn mt-3" style={{ textAlign: 'right' }}>
                            <button className="btn btn-out btn-secondary me-3" type="button" onClick={() => window.history.back()}>
                                Thoát
                            </button>
                            <button className="btn btn-out btn-success me-3" type="submit">Cập nhật</button>
                        </div>
                    </div>
                </form>
            </div>
            <Footer />
        </>
    );
};

