import React, { useEffect, useState } from 'react';
import SideNav from './SideNav';
import HorizontalNav from './HorizontalNav';
import axios from 'axios';
import { toast } from 'react-toastify';
import { getDownloadURL, ref, uploadBytes } from 'firebase/storage';
import { storage } from '../Firebase';
import { NavLink } from 'react-router-dom';
import UploadImgSanPham from './UploadImgSanPham';
import { v4 } from 'uuid';
import ReactPaginate from 'react-paginate';
import { ImageUploadProvider } from "./ImageUploadContext";
import Footer from './Footer';


const urlApi = process.env.REACT_APP_API_URL;

const Products = () => {
    
    const [cards,setCards] = useState([]);
    const [editting,setEditting] = useState(false);
    const [products,setProducts] = useState([]);
    const [pagination,setPagination] = useState([]);
    const [search, setSearch] = useState('');
    const [sort, setSort] = useState('Loại');
    const [err, setErr] = useState({
        errProductName: '',
        errPrice: '',
        errWeight: '',
        errType: '',
        errImages: ''
    });

    //Chi tiết sản phẩm
    const [displayP,setDisplayP] = useState([]);
    const [displayProduct,setDisplayProduct] = useState([]);
    const [listLoaiSP,setListLoaiSP] = useState([]);
    const [displaySelected,setDisplaySelected] = useState("");
   
    const [imageFile, setImageFile] = useState(null);
    const [resetPreview,setResetPreview] = useState(false);
    const [imageUrls, setImageUrls] = useState({});

    const [listHinhSP,setListHinhSP] = useState([]);

    const formattedMoney = (amount) => (
        amount.toLocaleString('vi-VN',{
            style: 'currency',
            currency: 'VND'
        })
    )

    const baseUrl = `${urlApi}/api/admin/quan-ly-san-pham`;
    let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));
    const pages = Array.from({ length: pagination.totalPages }, (_, index) => index + 1);

    const fetchData = async(pageNo=1)=>{
        resetForm();
        try {
            const response = await axios.get(`${baseUrl}?pageNo=${pageNo}&sort=${sort}&search=${search}`,{
                headers:{
                    Authorization: `Bearer ${sessionToken}`
                }
            })
            setCards(response.data.data.cards);
            setEditting(response.data.data.editting);
            setProducts(response.data.data.products);
            setListLoaiSP(response.data.data.listLoaiSP);
            setPagination(response.data.data.pagination);
        } catch (error) {
            
        }finally{
            
        }
    }

    const fetchDetail = (maSanPham)=>(
        async()=>{
            try {
                const response = await axios.get(baseUrl+"/"+maSanPham,{
                    headers:{
                        Authorization: `Bearer ${sessionToken}`
                    }
                })
                setDisplayP(response.data.data.displayP);
                setDisplayProduct(response.data.data.displayProduct);
                setListHinhSP(response.data.data.listHinhSP);
                console.log(displayP);
                setEditting(response.data.data.editting);
                fetchImages();
            } catch (error) {
                toast.error("Có lỗi xảy ra khi lấy dữ liệu sản phẩm mới");
            }finally{
    
            }
        }
    )

    const basicValidation = (e,fieldName)=>{
        if(fieldName==="productName"){
            if(e.target.value.length<=0){
                setErr({...err,errProductName:"Vui lòng nhập vào tên sản phẩm!"})
            }
            else{
                setErr({...err,errProductName:""})
            }
            setDisplayP({...displayP,tenSanPham:e.target.value})
        }
        else if(fieldName==="productPrice"){
            if(e.target.value<0){
                setErr({...err,errPrice:"Giá không được nhỏ hơn 0!"})
            }
            else{
                setErr({...err,errPrice:""})
            }
            setDisplayP({...displayP,gia:e.target.value})
        }
        else if(fieldName==="productWeight"){
            if(e.target.value<0){
                setErr({...err,errWeight:"Khối lượng không được nhỏ hơn 0!"})
            }
            else{
                setErr({...err,errWeight:""})
            }
            setDisplayP({...displayP,trongLuong:e.target.value})
        }
        else if(fieldName==="productType"){
            if(e.target.value.length<=0){
                setErr({...err,errType:"Vui lòng chọn loại sản phẩm!"})
            }
            else{
                setErr({...err,errType:""})
            }
            setDisplayP({...displayP,loaiSP:e.target.value})
            console.log(displayP.loaiSP);
        }
        
    }

    const handleSearchChange = (event) => {
        setSearch(event.target.value);
    }

    const handlePageClick = (data) => {
        let selectedPage = data.selected + 1; // Page index is 0-based in react-paginate
        fetchData(selectedPage);  // Gọi hàm fetchData với số trang đã chọn
      };

    const handleSortChange = (event) => {
        setSort(event.target.value);
    }

    const handleSearchSubmit = (event) => {
        event.preventDefault();
        fetchData();
    }

    const fetchImages = async () => {
        try {
          const urls = {}; // Create an empty object to store image URLs
          for (const hinh of listHinhSP) {
            var imageRef;
            // if(product.hinhAnh===null){
            //     imageRef = ref(storage, `AnhSanPham/Image_not_available.png`);
            // }
            // else{
            //     imageRef = ref(storage, `AnhSanPham/${product.hinhAnh}`); // Use product.hinhAnh
            // }
            if((displayP.hinhAnh===null && listHinhSP.tenHinhAnh === null)){
                imageRef = ref(storage, `AnhSanPham/Image_not_available.png`);
            }
            else{
                imageRef = ref(storage, `AnhSanPham/${hinh.tenHinhAnh}`);
            }
            
            const url = await getDownloadURL(imageRef);
            urls[hinh.tenHinhAnh] = url; // Store URL by product.hinhAnh
          }
          setImageUrls(urls); // Set the fetched image URLs
        } catch (error) {
          console.error("Error fetching image URLs:", error);
        }
    };

    //THÊM SỬA CHO PRODUCT
    const handlePost = async()=>{
        let imageURL = null;
        let newUniqueFileName = null;

        const payload = {
            // idForUpdate: displayU?.idForUpdate || null,  // Hidden input for update
            tenSanPham: displayP.tenSanPham ,             // Tên sản phẩm
            gia: displayP.gia,     // Giá
            moTa: displayP.moTa,             // Mô tả
            listHinhAnhSP: [],                 // List hình ảnh sản phẩm
            trongLuong: displayP.trongLuong , // Trọng lượng
            trangThai: displayP.trangThai,     // Trạng thái hiển thị
            //ngayThem tự thêm vào ở backend
            loaiSP:displayP.loaiSP,   //Loại sản phẩm
            hinhAnh:  '',              // Image (for now, placeholder)
        };

        // Nếu có ảnh được chọn, tiến hành upload lên Firebase, nếu không thì hiển thị lỗi
        if (imageFile!==null) {

            for(const file of imageFile){
                // // Tách tên file và phần mở rộng (extension)
                let fileNameSplit = file.name.split('.');
                let fileExtension = fileNameSplit.pop(); // Lấy phần mở rộng
                let fileNameWithoutExtension = fileNameSplit.join('.'); // Tên file không bao gồm phần mở rộng
                
                // // Tạo tên file mới với v4()
                newUniqueFileName = `${fileNameWithoutExtension}_${v4()}.${fileExtension}`;
            
                const storageRef = ref(storage, `AnhSanPham/${newUniqueFileName}`);  // Tạo reference trong Firebase Storage
                await uploadBytes(storageRef, file);  // Tải ảnh lên Firebase
                imageURL = await getDownloadURL(storageRef);  // Lấy URL ảnh đã tải lên

                payload.listHinhAnhSP.push(imageURL);
            }
            payload.hinhAnh = payload.listHinhAnhSP[0];
            console.log(payload);
        }
        


        if (
            (err.errPrice === '' || err.errPrice === undefined) && 
            (err.errProductName === '' || err.errProductName === undefined) && 
            (err.errType === '' || err.errType === undefined) && 
            (err.errWeight === '' || err.errWeight === undefined) &&
            (err.errImages === '' || err.errImages === undefined)
        ){
            axios.post(baseUrl + "/create", payload, {
                headers: {
                    Authorization: `Bearer ${sessionToken}`
                }
            })
            .then((response) => {
                toast.success(response.data.message);
                fetchData();
                
            })
            .catch((error) => {
                if (error.response) {
                    const errorMessage = error.response.data.message || "Đã xảy ra lỗi.";
                    setErr(error.response.data.data);
                    console.log(error)
                    toast.error(errorMessage);
                } else {
                    toast.error("Đã xảy ra lỗi không xác định.");
                }
            })
            .finally(() => {
                // resetForm();
            });
        }
        else{
            toast.error("Vui lòng kiểm tra lỗi hiển thị!");
            console.log(Object.keys(err).length); // In ra số lượng lỗi
            console.log(err.errImages)
        }
    }

    const handlePut = async()=>{
        let imageURL = null;
        let newUniqueFileName = null;

        const payload = {
            // idForUpdate: displayU?.idForUpdate || null,  // Hidden input for update
            tenSanPham: displayP.tenSanPham || '',             // Tên sản phẩm
            gia: displayP.gia || '',     // Giá
            moTa: displayP.moTa || '',             // Mô tả
            listHinhAnhSP: [] || '',                 // List hình ảnh sản phẩm
            trongLuong: displayP.trongLuong , // Trọng lượng
            trangThai: displayP.trangThai || '',     // Trạng thái hiển thị
            //ngayThem tự thêm vào ở backend
            loaiSP:displayP.loaiSP.tenLoai || displayP.loaiSP,   //Loại sản phẩm
            hinhAnh:  '',              // Image (for now, placeholder)
        };

        // Nếu có ảnh được chọn, tiến hành upload lên Firebase, nếu không thì hiển thị lỗi
        if (imageFile!==null) {

            for(const file of imageFile){
                // // Tách tên file và phần mở rộng (extension)
                let fileNameSplit = file.name.split('.');
                let fileExtension = fileNameSplit.pop(); // Lấy phần mở rộng
                let fileNameWithoutExtension = fileNameSplit.join('.'); // Tên file không bao gồm phần mở rộng
                
                // // Tạo tên file mới với v4()
                newUniqueFileName = `${fileNameWithoutExtension}_${v4()}.${fileExtension}`;
            
                const storageRef = ref(storage, `AnhSanPham/${newUniqueFileName}`);  // Tạo reference trong Firebase Storage
                await uploadBytes(storageRef, file);  // Tải ảnh lên Firebase
                imageURL = await getDownloadURL(storageRef);  // Lấy URL ảnh đã tải lên

                payload.listHinhAnhSP.push(imageURL);
            }
            payload.hinhAnh = payload.listHinhAnhSP[0];
            console.log(payload);
        }
        else{
            payload.listHinhAnhSP = listHinhSP.map(hinh=>hinh.tenHinhAnh);
            payload.hinhAnh = listHinhSP[0].tenHinhAnh;
            console.log(payload);
        }


        if (
            (err.errPrice === '' || err.errPrice === undefined) && 
            (err.errProductName === '' || err.errProductName === undefined) && 
            (err.errType === '' || err.errType === undefined) && 
            (err.errWeight === '' || err.errWeight === undefined) &&
            (err.errImages === '' || err.errImages === undefined)
        ){
            axios.put(baseUrl + "/update/"+displayP.maSanPham, payload, {
                headers: {
                    Authorization: `Bearer ${sessionToken}`
                }
            })
            .then((response) => {
                toast.success(response.data.message);
                fetchData();
                setErr({...err,errImages:""})
                setImageFile(null);
                setListHinhSP([]);

            })
            .catch((error) => {
                if (error.response) {
                    const errorMessage = error.response.data.message || "Đã xảy ra lỗi.";
                    setErr(error.response.data.data);
                    console.log(error)
                    toast.error(errorMessage);
                } else {
                    toast.error("Đã xảy ra lỗi không xác định.");
                }
            })
            .finally(() => {
                resetForm();
            });
        }
        else{
            toast.error("Vui lòng kiểm tra lỗi hiển thị!");
            console.log(Object.keys(err).length); // In ra số lượng lỗi
        }
    }

    const resetForm = () => {
        setDisplayP({
            tenSanPham: '',
            gia: 0,
            trongLuong: 0,
            loai: 'Chọn loại',  // Giá trị mặc định cho loại
            trangThai: true, // Giá trị mặc định cho trạng thái
        });
        setResetPreview(true);  // Truyền giá trị true để reset preview
    };

    useEffect(()=>{
        fetchData();
        fetchImages();
    },[sort, search]);

    useEffect(() => {
        if (listHinhSP.length >= 0) {
          fetchImages(); // Fetch lại URL ảnh từ Firebase mỗi khi trang reload
        }
      }, [listHinhSP]);

    useEffect(() => {
        if (imageFile?.length >= 0) {
            setErr({
                errImages:""
            })
        }
    }, [imageFile]);

    return (
        <div className="wrapper">
            <SideNav></SideNav>
            <div className="main main-admin p-0">
                <HorizontalNav></HorizontalNav>
                <main className="content">
                    <div className="container-fluid p-0">
                        <h1 className="h3 mb-3">Danh sách sản phẩm</h1>

                        <div className="row">
                            <div className="col-md-6">
                                <div className="card">
                                    <div className="card-body" style={{borderRadius:8}}>
                                        <div className="row">
                                            <div className="col mt-0">
                                                <h5 className="card-title">Tổng số sản phẩm</h5>
                                            </div>

                                            <div className="col-auto">
                                                <div className="stat text-primary">
                                                    <i className="fa-solid fa-arrow-up-right-dots" style={{color: "#067a38"}}></i>
                                                </div>
                                            </div>
                                        </div>
                                        <h1 className="mt-1 mb-3 fs-2">
                                            {cards.total}
                                        </h1>
                                    </div>
                                </div>
                            </div>
                            <div className="col-md-6">
                                <div className="card">
                                    <div className="card-body" style={{borderRadius:8}}>
                                        <div className="row">
                                            <div className="col mt-0">
                                                <h5 className="card-title">Số sản phẩm ngưng bán</h5>
                                            </div>

                                            <div className="col-auto">
                                                <div className="stat text-primary">
                                                    <i className="fa-solid fa-arrow-up-right-dots" style={{color: "#067a38"}}></i>
                                                </div>
                                            </div>
                                        </div>
                                        <h1 className="mt-1 mb-3 fs-2">
                                            {cards.inactive}
                                        </h1>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="userDetail" style={{marginTop:"20px"}}>
                            <h4>Chi tiết sản phẩm</h4>
                            <form id="productForm">
                                <input type="text" readOnly hidden name="idForUpdate" value="${idForUpdate}"></input>
                                <div className="row">
                                    <div className="col-md-6">
                                        <div className="mb-3">
                                            <label className="form-label fw-bold">Tên sản phẩm:</label>
                                            <input value={displayP?.tenSanPham || ''} type="text" 
                                                name="tenSanPham" 
                                                className="form-control" 
                                                placeholder="Nhập vào tên sản phẩm"
                                                onChange={(event)=>basicValidation(event,"productName")}
                                                />
                                            <em className="text-danger">{err?.errProductName}</em>
                                        </div>
                                    </div>
                                    <div className="col-md-6">
                                        <div className="mb-3">
                                            <label className="form-label fw-bold">Giá:</label>
                                            <input value={displayP?.gia || 0}
                                                type="number" name="gia" className="form-control" 
                                                placeholder="Nhập vào giá sản phẩm"
                                                onChange={(event)=>basicValidation(event,"productPrice")}
                                                />
                                            <em className="text-danger">{err?.errPrice}</em>
                                        </div>
                                    </div>
                                </div>

                                <div className="row">
                                    
                                    <div className="col-md-6">
                                        <div className="mb-3">
                                            <label className="form-label fw-bold">Khối lượng (gram):</label>
                                            <input value={displayP?.trongLuong || 0}
                                                type="number" name="trongLuong" 
                                                className="form-control" 
                                                placeholder="Nhập vào khối lượng"
                                                onChange={(event)=>basicValidation(event,"productWeight")}
                                                />
                                            <em className="text-danger">{err?.errWeight}</em>
                                        </div>
                                    </div>
                                    <div className="col-md-6">
                                        <div className="mb-3">
                                            <label className="form-label fw-bold">Trạng thái:</label>
                                            <div className="form-check">
                                                <input type="radio" checked={displayP.trangThai === true}
                                                    className="form-check-input" value={true} name="trangThai"
                                                    id="flexRadioDefault1"
                                                    onChange={(e)=>setDisplayP({...displayP,trangThai: e.target.value === 'true'})}
                                                />Còn bán
                                            </div>
                                            <div className="form-check">
                                                <input checked={displayP.trangThai === false}
                                                    className="form-check-input" value={false} type="radio" name="trangThai"
                                                    id="flexRadioDefault2"
                                                    onChange={(e)=>setDisplayP({...displayP,trangThai: e.target.value === 'true'})}/>Ngưng bán
                                            </div> 
                                        </div>
                                    </div>
                                </div>

                                <div className="row">
                                    <div className="col-md-12">
                                        <div className="mb-3">
                                            <label className="form-label fw-bold">Loại:</label>
                                            <select 
                                                value={displayP?.loaiSP ? displayP.loaiSP.tenLoai : ''} 
                                                name="tenLoai" 
                                                className="form-select" 
                                                aria-label="Default select example"
                                                onChange={(event)=>basicValidation(event,"productType")}
                                            >
                                                <option value="" hidden>Chọn loại</option>
                                                {listLoaiSP.map((loaiSP, index) => (
                                                    <option key={index} value={loaiSP.tenLoai}>
                                                        {loaiSP.tenLoai}
                                                    </option>
                                                ))}
                                            </select>
                                            <em className="text-danger">{err?.errType}</em>
                                        </div>
                                    </div>
                                    
                                </div>

                                <div className="row">
                                    <div className="col-md-12">
                                        <ImageUploadProvider>
                                            <div className="mb-3">
                                                <label className="form-label fw-bold">Chọn ảnh:</label>
                                                <UploadImgSanPham
                                                    className='form-control'
                                                    inputClass='upload-instructions'
                                                    onChange
                                                    onFileSelect={setImageFile}
                                                    reset={resetPreview}
                                                />
                                                <div className="image-gallery" style={{ marginTop: '10px' }}>
                                                    {listHinhSP && listHinhSP.map((hinh, index) => (
                                                        <a 
                                                        key={index}
                                                        href={hinh.tenHinhAnh}
                                                        data-lightbox="product-images" 
                                                        data-title={hinh.tenHinhAnh}
                                                        >
                                                        <img 
                                                            src={hinh.tenHinhAnh}
                                                            alt={hinh.tenHinhAnh}
                                                            style={{
                                                            width: '100px', 
                                                            height: '100px', 
                                                            objectFit: 'cover', 
                                                            margin: '5px', 
                                                            border: '1px solid #ddd', 
                                                            padding: '5px'
                                                            }}
                                                        />
                                                        </a>
                                                    ))}
                                                </div>
                                                <em className="text-danger">{err?.errImages}</em>
                                            </div>
                                        </ImageUploadProvider>
                                    </div>
                                </div>

                                <div className="row">
                                    <div className="col-md-12">
                                        <div className="mb-3">
                                            <label className="form-label fw-bold">Mô tả:</label>
                                            <textarea name="moTa" className="form-control" rows="4" cols="5" value={displayP?.moTa || ''}
                                                onChange={(event)=>setDisplayP({...displayP,moTa:event.target.value})}></textarea>
                                        </div>
                                    </div>
                                </div>

                                <button type='button' disabled={editting} onClick={handlePost}
                                    className="btn btn-success me-2">Thêm</button>
                                <button disabled={!editting}
                                    type='button' className="btn btn-primary me-2" onClick={handlePut}>Cập nhật</button>
                                <button type='button' onClick={resetForm} className="btn me-2" style={{ backgroundColor: '#656565' }}>Làm mới</button>

                                <input type="hidden" name="hinhAnh" id="hinhAnh"></input>

                            </form>
                        </div>

                        <div className="userList" style={{marginTop:"20px"}}>
                            <h4 className="text-center"> Danh sách sản phẩm </h4>
                            <form>
                                <div className="row">
                                    <div className="col-md-4 col-xxl-8">
                                        <div className="input-group mb-3">
                                        {/* value={displaySelected !== null && displaySelected !== '' ? displaySelected : 'Chọn bộ lọc'} */}
                                            <select onChange={handleSortChange}  name="sort" className="form-select" aria-label="Default select example">
                                                <option
                                                    disabled={displaySelected===null || displaySelected==='' ? 'disabled' : '' } selected
                                                    hidden>
                                                    Chọn bộ lọc
                                                </option>
                                                <option value="Loại">Loại</option>
                                                <option value="Tên sản phẩm">Tên sản phẩm</option>
                                                <option value="Khối lượng">Khối lượng</option>
                                                <option value="Giá thấp tới cao">Giá thấp tới cao</option>
                                                <option value="Giá cao tới thấp">Giá cao tới thấp</option>
                                                <option value="Trạng thái hoạt động">Trạng thái hoạt động</option>
                                                <option value="Trạng thái ngưng bán">Trạng thái ngưng bán</option>
                                            </select>
                                            <input value={search} name="search" type="search" className="form-control" placeholder="Tìm kiếm" onChange={handleSearchChange}/>
                                            {/* <button type="button" className="btn btn-success">Tìm kiếm</button> */}
                                        </div>
                                    </div>
                                </div>
                            </form>

                            <table className="table table-striped table-hover table-light">
                                <thead className="text-center" style={{ backgroundColor: '#067a38', color: '#fff',fontSize:'0.8rem' }}>
                                    <th>Mã sản phẩm</th>
                                    <th>Tên sản phẩm</th>
                                    <th>Giá</th>
                                    <th>Khối lượng</th>
                                    <th>Loại</th>
                                    <th>Trạng thái</th>
                                    <th>Mô tả</th>
                                    <th>Hành động</th>
                                </thead>
                                <tbody className="text-center">
                                    {products.map((p,index)=>(
                                        <tr key={index}>
                                            <td>{p.maSanPham}</td>
                                            <td>{p.tenSanPham}</td>
                                            <td>{formattedMoney(p.gia)}</td>
                                            <td>{p.trongLuong}</td>
                                            <td>{p.loaiSP.tenLoai}</td>
                                            <td>
                                                <span className={`badge ${p.trangThai === false ? 'bg-danger' : 'bg-success'}`}>
                                                    {p.trangThai === true ? 'Còn bán' : 'Ngưng bán'}
                                                </span>
                                            </td>
                                            <td>{p.moTa}</td>
                                            <td><i className="fa-solid fa-pen-to-square" onClick={fetchDetail(p.maSanPham)}></i></td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>                    

                            <div className="text-center" hidden={pagination.totalPages!==0}>
                                <p className="fw-bold">Không tìm thấy sản phẩm tương ứng</p>
                            </div>

                            <div hidden={pagination.totalPages===0} className="d-flex justify-content-between" style={{marginTop:"25px"}}>
                                {/* Vị trí hiển thị số trang */}
                                <p className="fw-bold">Đang xem trang {pagination.currentPage} / {pagination.totalPages}</p>

                                {/* Cập nhật nút phân trang */}
                                <ReactPaginate
                                    previousLabel={"«"}
                                    nextLabel={"»"}
                                    breakLabel={"..."}
                                    breakClassName={"page-item"} // Áp dụng style cho phần tử 3 chấm
                                    breakLinkClassName={"page-link"} // Áp dụng style cho link của 3 chấm
                                    pageCount={pagination.totalPages}
                                    marginPagesDisplayed={2}
                                    pageRangeDisplayed={5}  // Số lượng trang hiển thị
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
                                    forcePage={pagination.currentPage > 0 ? pagination.currentPage - 1 : 0}  // Chuyển currentPage thành 0-based index cho ReactPaginate
                                />
                                <p className="fw-bold">5 sản phẩm / 1 trang</p>
                            </div>


                        </div>
                    </div>
                </main>
                <Footer></Footer>
            </div>
        </div>
    );
};

export default Products;