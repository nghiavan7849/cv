import './App.css';
import { Login } from './Components/Login';
import { Register } from './Components/Register';
import { Forgotpassword } from './Components/Forgotpassword';
import {ConfirmForgotPassword} from './Components/Confirmforgotpassword';
import Product from './Components/product';
import { ProductDetail } from './Components/ProductDetail';
import Main from './Components/main';
import { Address } from './Components/Address';
import { Cart } from './Components/Cart';
import { ChangePassword } from './Components/Changepassword';
import { UpdateAccount } from './Components/Updateaccount';
import { EditEvaluate } from './Components/EditEvaluate';
import { Evaluate } from './Components/Evaluate';
import OrderHistory from './Components/orderhistory';
import { Pay } from './Components/Pay';
import { PayDetails } from './Components/PayDetails';

import Dashboard from './Components/Admin/Dashboard';

import { Routes, Route, Navigate } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import Order from './Components/Admin/Order';
import User from './Components/Admin/User';
import Products from './Components/Admin/Products';

//import css và jquery từ lightbox2
import 'lightbox2/dist/css/lightbox.min.css';
import 'lightbox2/dist/js/lightbox-plus-jquery.min.js';
import Statistics from './Components/Admin/Statistics';
import ProductType from './Components/Admin/ProductType';
import Reviews from './Components/Admin/Reviews';
import { useEffect, useState } from 'react';
import ChatBot from './Components/ChatBot';
import {AuthProvider} from './Service/AuthContext'







const PrivateRoute = ({ element: Element, ...rest }) => {
  let sessionTaiKhoan = JSON.parse(sessionStorage.getItem("sessionTaiKhoan"));
  let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));
  return sessionTaiKhoan && sessionToken ? (
    <Element {...rest}/>
  ):(
    <Navigate to="/login"/>
  )
}

function App() {

  return (
    <AuthProvider>
      <div>
        <Routes>
          <Route path="/" element={<Navigate to="/home" replace />} />
          <Route path='/login' element={<Login />} />
          <Route path='/forgot-password' element={<Forgotpassword />} />
          <Route path='/forgot-password/confirm' element={<ConfirmForgotPassword />} />
          <Route path='/register' element={<Register />} />
          <Route path='/home' element={<Main />} />
          <Route path='/home/product' element={<Product />} />
          <Route path='/home/address' element={<PrivateRoute element={Address}/>} />
          <Route path='/home/order-history' element={<PrivateRoute element={OrderHistory}/>}/>
          <Route path='/home/change-password' element={<PrivateRoute element={ChangePassword}/>}/>
          <Route path='/home/edit-profile' element={<PrivateRoute element={UpdateAccount}/>} />
          <Route path="home/cart" element={<PrivateRoute element={Cart}/>}/>
          <Route path='/home/product-details' element={<ProductDetail />} />
          <Route path='/home/product-details/:maSanPham' element={<ProductDetail />} />
          <Route path='/home/edit-evaluate' element={<PrivateRoute element={EditEvaluate}/>}/>
          <Route path='/home/edit-evaluate/:maDanhGia' element={<PrivateRoute element={EditEvaluate}/>}/>
          <Route path='/home/evaluate' element={<PrivateRoute element={Evaluate}/>}/>
          <Route path='/home/evaluate/:maSanPham/:soLuong/:maHoaDon' element={<PrivateRoute element={Evaluate}/>}/>
          <Route path='/home/pay' element={<PrivateRoute element={Pay}/>}/>
          <Route path='/home/pay-details' element={<PrivateRoute element={PayDetails}/>} />
          <Route path='/vnpay_return' element={<PrivateRoute element={Pay}/>} />
          <Route path='/Dashboard' element={<PrivateRoute element={Dashboard} />} />
          <Route path='/home-admin' element={<PrivateRoute element={Dashboard} />} />
          <Route path='/home-admin/order' element={ <PrivateRoute element={Order}></PrivateRoute> } ></Route>
          <Route path='/home-admin/user' element={ <PrivateRoute element={User}></PrivateRoute> } ></Route>
          <Route path='/home-admin/product' element={ <PrivateRoute element={Products}></PrivateRoute> } ></Route>
          <Route path='/home-admin/statistics' element={ <PrivateRoute element={Statistics}></PrivateRoute> } ></Route>
          <Route path='/home-admin/product-category' element={ <PrivateRoute element={ProductType}></PrivateRoute> } ></Route>
          <Route path='/home-admin/reviews' element={ <PrivateRoute element={Reviews}></PrivateRoute> } ></Route>
          {/*
          <Route path='/home-admin/product' element={ <PrivateRoute> <Dashboard></Dashboard> </PrivateRoute> } ></Route>
          <Route path='/home-admin/product-category' element={ <PrivateRoute> <Dashboard></Dashboard> </PrivateRoute> } ></Route>
          <Route path='/home-admin/statistics' element={ <PrivateRoute> <Dashboard></Dashboard> </PrivateRoute> } ></Route>
          <Route path='/home-admin/reviews' element={ <PrivateRoute> <Dashboard></Dashboard> </PrivateRoute> } ></Route> */}

        </Routes>
        <ToastContainer
          position="top-right"
          autoClose={3000}
          hideProgressBar={false}
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme="light"
        />
        <ChatBot></ChatBot>
      </div>
    </AuthProvider>
  );
}

export default App;
