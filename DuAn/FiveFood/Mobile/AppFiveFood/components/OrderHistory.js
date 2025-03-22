import React, { useCallback, useEffect, useState } from 'react';
import { FlatList,Modal, Image,TextInput, ScrollView, StyleSheet, Text, TouchableOpacity, View, KeyboardAvoidingView, Platform } from "react-native";
import MenuTop from './menu/MenuTop';
import className from 'twrnc';
import { Header } from 'react-native-elements';
import { SafeAreaView } from 'react-native-safe-area-context';
import {ApiUrl}  from '../service/ApiUrl';
import axios from 'axios';
import { useFocusEffect } from '@react-navigation/native'
import AsyncStorage from "@react-native-async-storage/async-storage";
import {formatVND} from '../service/formatVND'
import { Picker } from '@react-native-picker/picker';



const getLichSu = (page, maTaiKhoan, trangThai, token, size) => {
    return axios.get(`${ApiUrl}/api/lich-su-mua-hang?page=${page}&maTaiKhoan=${maTaiKhoan}&trangThai=${trangThai}&size=${size}`, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
    
}

const updateTrangThai = (maHoaDon,trangThai,lyDoHuy,token)=>{
    return axios.put(`${ApiUrl}/api/hoa-don/cap-nhat-trang-thai?maHoaDon=${maHoaDon}&trangThai=${trangThai}&lyDoHuy=${lyDoHuy}`, null, {
        headers: {
        'Authorization': `Bearer ${token}`,
        },
    });
}

async function getToken() {
    try {
        const token = await AsyncStorage.getItem('sessionTokenTK');
        return token != null ? token : null;
    } catch (error) {
        console.error("Error retrieving token:", error);
    }
}

async function getTaiKhoan(){
    try {
        const sessionTaiKhoan = await AsyncStorage.getItem('sessionTaiKhoan');
        return sessionTaiKhoan
    } catch (error) {
        console.error("Error retrieving taiKhoan:", error);
    }
}

const OrderHistory = ({ navigation }) => {
    const listStatus = [
        { id: '1', name: "Đã đặt hàng", mapping:"dadathang" },
        { id: '2', name: "Đã xác nhận", mapping:"daxacnhan" },
        { id: '3', name: "Đang xử lý", mapping:"dangxuly" },
        { id: '4', name: "Đang vận chuyển", mapping:"dangvanchuyen" },
        { id: '5', name: "Giao thành công", mapping:"giaothanhcong" },
        { id: '6', name: "Đã hủy", mapping:"dahuy" }
    ];
    const [statusNameClick, setStatusNameClick] = useState("dadathang");

    const [token, setToken] = useState(null);
    const [taiKhoan,setTaiKhoan] = useState({});
    const [size, setSize] = useState(100);
    const [listOrderHistory, setListOrderHistory] = useState([]);

    const [showModal, setShowModal] = useState(false);  // Modal trạng thái
    const [lyDoHuy, setLyDoHuy] = useState('');  // Lý do hủy
    const [hoaDonHuy, setHoaDonHuy] = useState(null);  // Thông tin hóa đơn cần hủy
    const [messageLyDoHuy, setMessageLyDoHuy] = useState('');
    const listLyDo = [
        { id: 1, lyDo: "Tôi không có nhu cầu mua nữa" },
        { id: 2, lyDo: "Tôi muốn cập nhật địa chỉ/sđt nhận hàng" },
        { id: 3, lyDo: "Tôi tìm thấy chỗ mua khác tốt hơn" },
        { id: 4, lyDo: "Tôi muốn thay đổi sản phẩm" },
        { id: 5, lyDo: "Khác" },
      ]
      const [lyDoKhac, isLyDoKhac] = useState(false);
      const [selectedLyDo, setSelectedLyDo] = useState("");

    useFocusEffect(
        useCallback(() => {
            const initialize = async () => {
                const token = await getToken();
                const getTK = await getTaiKhoan();
                const taiKhoan = JSON.parse(getTK);
    
                if (taiKhoan && taiKhoan.maTaiKhoan && token) {
                    setTaiKhoan(taiKhoan);
                    setToken(token);
                } else {
                    console.log("Thông tin tài khoản hoặc token bị thiếu.");
                }
            };
    
            initialize();

        }, [])
    );

    useEffect(() => {
        if (token && taiKhoan.maTaiKhoan) {
            loadPage();
        }
    }, [token, taiKhoan,statusNameClick]); 
    


    const loadPage = async () => {
        try {
            const maTaiKhoan = taiKhoan.maTaiKhoan;
    
            if (!maTaiKhoan || !token) {
                console.log("Thông tin tài khoản hoặc token bị thiếu.");
                return;
            }
    
            const res = await getLichSu(1, maTaiKhoan, statusNameClick, token, size);
            if (res && res.data && res.data.data) {
                setListOrderHistory(res.data.data.lichSu);
                console.log("Lịch sử đơn hàng:", res.data.data.lichSu);
            }
        } catch (error) {
            console.error("Lỗi khi tải dữ liệu:", error);
        }
    };

    const clickChiTietLichSu = (order)=>{
        navigation.navigate('DetaillOrderHistory',{
            maTaiKhoan: taiKhoan.maTaiKhoan,
            trangThai: statusNameClick,
            maHoaDon: order.maHoaDon,
            diaChiNhan: order.diaChiNhan,
            page:1,
            tongTienHD: order.tongTien})
    }

    const clickHuy = (order) => {
        setHoaDonHuy(order);
        setShowModal(true);
    };

    const clickXacNhanHuy = async()=>{
        if(lyDoHuy===''){
            setMessageLyDoHuy("Vui lòng nhập lý do hủy đơn hàng");
        }else{
            setShowModal(false);
            //Cập nhật trạng thái đơn hàng đã hủy
            let res = await updateTrangThai(hoaDonHuy.maHoaDon,"dahuy",lyDoHuy,token);
            if(res){
                loadPage();
            }
        }
    }

    const handleClose = ()=>{
        setShowModal(false);
        setLyDoHuy('');
        setMessageLyDoHuy('');
    }

    const clickChonLyDo = (value) => {
        setSelectedLyDo(value);
        if (value === "Khác") {
          isLyDoKhac(true);
          setLyDoHuy("");
        } else {
          isLyDoKhac(false);
          setLyDoHuy(value);
          setMessageLyDoHuy("");
        }
    }

    const onChangeLyDoHuy = (e) => {
        setLyDoHuy(e.target.value)
    
        setMessageLyDoHuy("");
        document.getElementById("lyDoHuy").classList.remove("error-validate");
    }

    return (
        <SafeAreaView style={styles.container} showsVerticalScrollIndicator={false}>
            <MenuTop title='Lịch sử đơn mua' />
            <View style={className`mt-1 mb-1 max-h-13 `}>
                <FlatList
                    h
                    data={listStatus}
                    renderItem={(item) => (
                        <TouchableOpacity style={className` justify-center max-h-13 items-center  ${item.item.mapping === statusNameClick ? 'border-b-2 border-blue-600' : 'border-b border-slate-400'}`}
                            onPress={() => {setStatusNameClick(item.item.mapping);loadPage()}} >
                            <Text style={className`mt-1 mb-2 px-2 font-bold ${item.item.mapping === statusNameClick ? 'text-base' : 'text-base text-slate-600'}`}>{item.item.name}</Text>
                        </TouchableOpacity>
                    )}
                    keyExtractor={(item) => item.id}
                    horizontal={true}
                    showsHorizontalScrollIndicator={false}
                />
            </View>
            <ScrollView showsVerticalScrollIndicator={false}>
                <View style={className`bg-[#FEFBEE] mt-2 p-2 rounded-2`}>
                    {listOrderHistory.length > 0 ? (
                    listOrderHistory.map((order, index) => (
                        <View key={index} style={className`bg-white mb-4 p-3 rounded-lg shadow`}>
                        {/* Mã hóa đơn và trạng thái */}
                        <View style={className`flex flex-row justify-between`}>
                            <Text style={className`font-bold text-black text-lg`}>
                            Mã hóa đơn: {order.maHoaDon}
                            </Text>
                            <Text style={className`text-sm ${order.tenTrangThai === 'Đã Hủy' ? 'text-red-500' : 'text-green-500'}`}>
                            {order.tenTrangThai}
                            </Text>
                        </View>
                        {/* Thông tin địa chỉ và tổng sản phẩm */}
                        <Text style={className`mt-2 text-gray-700`}>
                            <Text style={className`font-medium`}>Địa chỉ: </Text>{order.diaChiNhan}
                        </Text>
                        <Text style={className`mt-1 text-gray-700`}>
                            <Text style={className`font-medium`}>Tổng sản phẩm: </Text>{order.tongSanPham}
                        </Text>
                        {/* Tổng tiền */}
                        <Text style={className`mt-2 text-gray-700 font-medium`}>
                            Tổng tiền: <Text style={className`text-red-500 font-bold`}>{formatVND(order.tongTien)}</Text>
                        </Text>
                        {/* Lý do hủy (nếu có) */}
                        {order.tenTrangThai === 'Đã Hủy' && (
                            <Text style={className`mt-2 text-gray-500 italic`}>
                            Lý do hủy: {order.lyDoHuy}
                            </Text>
                        )}
                        {/* Nút thao tác */}
                        <View style={className`mt-3 flex flex-row justify-end`}>
                            {order.tenTrangThai === 'Đang vận chuyển' && (
                            <TouchableOpacity
                                style={className`px-4 py-2 bg-green-500 rounded-lg mr-2`}
                                onPress={() => clickDaNhanDuocHang(order)}
                            >
                                <Text style={className`text-white text-sm font-bold`}>Đã nhận được hàng</Text>
                            </TouchableOpacity>
                            )}
                            {order.tenTrangThai === 'Đã đặt hàng' && (
                            <TouchableOpacity
                                style={className`px-4 py-2 bg-red-500 rounded-lg mr-2`}
                                onPress={() => clickHuy(order)}
                            >
                                <Text style={className`text-white text-sm font-bold`}>Hủy đơn hàng</Text>
                            </TouchableOpacity>
                            )}
                            <TouchableOpacity
                            style={className`px-4 py-2 bg-blue-500 rounded-lg`}
                            onPress={() => clickChiTietLichSu(order)}
                            >
                                <Text style={className`text-white text-sm font-bold`}>Xem chi tiết</Text>
                            </TouchableOpacity>
                        </View>
                        </View>
                    ))
                    ) : (
                    <View style={className`h-14 flex justify-center items-center`}>
                        <Text style={className`text-gray-500`}>Không có dữ liệu</Text>
                    </View>
                    )}
                </View>
                </ScrollView>

                {/* Modal xác nhận hủy */}
                <Modal
                    visible={showModal}
                    transparent={true}
                    animationType="fade"
                    onRequestClose={handleClose}
                >
                    <KeyboardAvoidingView
                        style={className`flex-1 justify-center items-center bg-black bg-opacity-50`}
                        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
                    >
                        <ScrollView contentContainerStyle={className`flex-grow justify-center`}>
                        <View style={className`bg-white p-4 rounded-lg w-80`}>
                            <Text style={className`font-bold text-lg mb-4`}>Lý do hủy đơn hàng</Text>
                            {/* Picker */}
                            <View style={className`border p-2 rounded-md mb-3`}>
                            <Picker
                                selectedValue={selectedLyDo}
                                onValueChange={(value) => clickChonLyDo(value)}
                                style={className`text-base`}
                                itemStyle={className`text-[10px]`}
                            >
                                {listLyDo.map((item) => (
                                    <Picker.Item key={item.id} label={item.lyDo} value={item.lyDo} />
                                ))}
                            </Picker>
                            </View>
                            {/* Nhập lý do khác */}
                            {lyDoKhac && (
                            <>
                                <TextInput
                                style={className`border p-2 rounded-md mb-3`}
                                placeholder="Nhập lý do khác"
                                value={lyDoHuy}
                                onChangeText={setLyDoHuy}
                                multiline
                                />
                                {messageLyDoHuy !== '' && (
                                <Text style={className`text-red-500 mb-2`}>{messageLyDoHuy}</Text>
                                )}
                            </>
                            )}
                            {/* Nút hành động */}
                            <View style={className`flex-row justify-between`}>
                            <TouchableOpacity onPress={handleClose}>
                                <View style={className`px-4 py-2 bg-gray-500 rounded-lg`}>
                                <Text style={className`text-white font-bold`}>Hủy</Text>
                                </View>
                            </TouchableOpacity>
                            <TouchableOpacity onPress={clickXacNhanHuy}>
                                <View style={className`px-4 py-2 bg-blue-500 rounded-lg`}>
                                <Text style={className`text-white font-bold`}>Xác nhận</Text>
                                </View>
                            </TouchableOpacity>
                            </View>
                        </View>
                        </ScrollView>
                    </KeyboardAvoidingView>
                    </Modal>


        </SafeAreaView>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#F5F0DC',
        paddingHorizontal: 16
    }
});

export default OrderHistory;