import React, { useCallback, useEffect, useRef, useState } from "react";
import { SafeAreaView } from "react-native-safe-area-context";
import MenuTop from "./menu/MenuTop";
import { Image, ScrollView, StyleSheet, Text, TouchableOpacity, View } from "react-native";
import className from 'twrnc';
import axios from "axios";
import {ApiUrl} from "../service/ApiUrl";
import { useFocusEffect, useNavigation } from "@react-navigation/native";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { formatVND } from "../service/formatVND";
import Toast from 'react-native-toast-message';

const createGioHang = (data, token) => {
    return axios.post(`${ApiUrl}/api/chi-tiet-gio-hang/create`, data, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
};

const getHDByMaHD = (id, token) => {
    return axios.get(`${ApiUrl}/api/hoa-don/get-by-ma-hoa-don/${id}`, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
}



const DetailOrderHistory = ({ route }) => {

    const [taiKhoan, setTaiKhoan] = useState({});
    const [token, setToken] = useState("");
    const [listChiTietHoaDon, setListChiTietHoaDon] = useState([]);
    const [tongTien, setTongTien] = useState(0);
    const [hoaDon, setHoaDon] = useState({});
    const { maTaiKhoan, trangThai, maHoaDon, diaChiNhan, page, tongTienHD } = route.params;
    const [sanPhamDanhGia, setSanPhamDanhGia] = useState([{ maSanPham: '', check: true }]);
    const navigation = useNavigation();

    const trangThaiMapping = {
        'dadathang': 'đã được đặt',
        'daxacnhan': 'đã được xác nhận',
        'dangxuly': 'đang xử lý',
        'dangvanchuyen': 'đang vận chuyển',
        'giaothanhcong': 'được giao thành công',
        'dahuy': 'đã được hủy',
    }

    async function getToken() {
        // console.log("Đây là địa chỉ: ", diaChiNhan)
        try {
            const token = await AsyncStorage.getItem('sessionTokenTK');
            return token != null ? token : null;
        } catch (error) {
            console.error("Error retrieving token:", error);
        }
    }

    async function getTaiKhoan() {
        try {
            const sessionTaiKhoan = await AsyncStorage.getItem('sessionTaiKhoan');
            // console.log("Đây là sessionTaiKhoan ",sessionTaiKhoan)
            return sessionTaiKhoan
        } catch (error) {
            console.error("Error retrieving taiKhoan:", error);
        }
    }

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

    const fetchDetail = async () => {
        try {
            const response = await axios.get(`${ApiUrl}/api/lich-su-mua-hang/chi-tiet?maTaiKhoan=${maTaiKhoan}&trangThai=${trangThai}&maHoaDon=${maHoaDon}&page=${page}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            const updateSanPhamDanhGia = [];
            
            for (const ct of response.data.data.lichSu) {
                updateSanPhamDanhGia.push(ct.maSanPham);
            }
            setListChiTietHoaDon(response.data.data.lichSu);
            try {
                let res = await axios.post(`${ApiUrl}/api/danh-gia/check-list?maTaiKhoan=${maTaiKhoan}`, {
                    listIds: updateSanPhamDanhGia
                });
                setSanPhamDanhGia(res.data.listCheck);
            } catch (error) {
                console.log('error', error)
            }

            const resHoaDon = await getHDByMaHD(maHoaDon, token);
            if(resHoaDon){
                setHoaDon(resHoaDon.data.data.hoaDon);
            }

        } catch (error) {
            console.error("Lỗi khi gọi API: ", error.response ? error.response.data : error);
        }
    };

    const clickMuaLai = async (maSanPham, soLuong) => {
        let res = await createGioHang({
            "soLuong": soLuong,
            "maTaiKhoan": maTaiKhoan,
            "maSanPham": maSanPham
        }, token);
        if (res) {
            Toast.show({
                type: 'success',
                text1: 'Thêm vào giỏ hàng thành công',
                text1Style: className`text-lg`
            })
        }
    }

    const clickDaNhanDuocHang = async (hd) => {
        let res = await updateTrangThai(hd.maHoaDon, 'giaothanhcong', "", sessionToken);
        if (res) {
            loadPage();
            setListChiTietLichSu([]);
        }
    }

    useEffect(() => {
        if (token && taiKhoan.maTaiKhoan) {
            fetchDetail();
        }

    }, [token, taiKhoan]);

    useEffect(() => {
        if (listChiTietHoaDon.length > 0) {
            // Tính tổng tất cả các giá trị tongTien trong mảng
            const total = listChiTietHoaDon.reduce((acc, item) => acc + item.tongTien, 0);
            setTongTien(total); // Cập nhật useState
        }
    }, [listChiTietHoaDon]);

    const checkSPDaDanhGia = async (maSanPham) => {
        try {
            const response = await axios.get(`${ApiUrl}/api/danh-gia/check?maSanPham=${maSanPham}&maTaiKhoan=${maTaiKhoan}`);
            return response.data.check;
        } catch (error) {
            console.error("Error checking review status:", error);
        }
    }

    return (
        <SafeAreaView style={styles.container}>
            <MenuTop title={"Chi tiết đơn mua"} />
            <ScrollView showsVerticalScrollIndicator={false}>
                <View style={className`bg-[#FEFBEE] h-50 mt-2 rounded-lg`}>
                    <View style={className`bg-green-600 h-13 rounded-t-lg justify-center`}>
                        <Text style={className`ml-3 text-lg font-bold text-[#FFFFFF]`}>Đơn hàng {trangThaiMapping[trangThai]}</Text>
                    </View>
                    <View style={className`h-17 justify-center`}>
                        <View style={className`flex-row justify-between pr-2`}>
                            <Text style={className`ml-3 text-sm text-[#000000] `}>Mã đơn hàng: </Text>
                            <Text style={className` pr-2 text-sm text-[#000000]`}>{maHoaDon}</Text>
                        </View>

                        <View style={className`flex-row justify-between pr-2`}>
                            <Text style={className`ml-3 text-sm text-[#000000] mt-1`}>Phương thức thanh toán: </Text>
                            <Text style={className`mt-1 pr-2 text-sm text-[#000000]`}>{hoaDon.phuongThucThanhToan?'Thanh toán khi nhận hàng': 'Thanh toán online'}</Text>
                        </View>
                    </View>
                    <View style={className`h-20`}>
                        <Text style={className`ml-3 text-base font-bold text-[#000000]`}>Địa chỉ nhận hàng</Text>
                        <View style={className`mt-1`}>
                            <Text style={className`ml-3`}><Text style={className`text-sm font-medium text-[#000000]`}>{hoaDon.dcHoTen} </Text><Text style={className`text-sm font-normal text-[#686868]`}>(+84) {hoaDon.dcSoDienThoai}</Text></Text>
                            <Text style={className`ml-3 text-sm text-[#000000] pr-2`} numberOfLines={1} ellipsizeMode='taill'>{diaChiNhan}</Text>

                        </View>
                    </View>
                </View>
                <View style={className`bg-[#FEFBEE] mt-3 rounded-lg mb-3 pb-1`}>
                    <View style={className` h-13 rounded-t-lg justify-center border-b border-slate-300`}>
                        <Text style={className`ml-3 text-lg font-bold text-[#000000]`}>Sản phẩm</Text>
                    </View>
                    {listChiTietHoaDon.map((ct, index) => (
                        <View key={index}>
                            <View style={className`min-h-22 flex flex-row mt-2 pb-2 `}>
                                <View style={className`basis-3/10 justify-start items-center mt-1`}>
                                    <Image
                                        source={{ uri: ct.hinhAnh }}
                                        style={className`h-20 w-20 rounded-2`}
                                    />
                                </View>
                                <View style={className`basis-7/10 pr-2`}>
                                    <View style={className`mt-1 flex flex-row`}>
                                        <Text style={className`text-black text-base w-full mr-1 max-w-100 basis-8/11`} numberOfLines={1} ellipsizeMode='taill'>{ct.tenSanPham}</Text>
                                        <View style={className`basis-3/11 self-center`}><Text style={className`text-black text-sm mr-1 text-right pr-1 `}>x{ct.soLuong}</Text></View>
                                    </View>
                                    <View style={className`flex flex-row justify-between items-center mt-3`}>
                                        <Text></Text>
                                        <Text style={className`text-black mr-1 text-right`}>{formatVND(ct.tongTien / ct.soLuong)}<Text style={className`underline`}></Text></Text>
                                    </View>
                                    <View style={className`flex flex-row justify-end items-center pt-0.5 mt-2`}>
                                        <Text style={className`text-black mr-1`}>Thành tiền: </Text>
                                        <Text style={className`text-black mr-1 text-right font-semibold`}>{formatVND(ct.tongTien)}<Text style={className`underline`}></Text></Text>
                                    </View>
                                </View>
                            </View>
                            {trangThai === 'giaothanhcong' ? (
                             <View style={className`min-h-10 rounded-b-lg pr-2.5 pl-3.5 mb-1.5`}>
                                    <View style={className`flex flex-row mt-1 justify-end`}>
                                        <TouchableOpacity onPress={() => clickMuaLai(ct.maSanPham, ct.soLuong)} style={className`px-3.5 py-1.25 mx-3 rounded-md border border-green-600`}>
                                            <Text style={className`text-white text-center font-bold text-base text-green-600`}>Mua lại</Text>
                                        </TouchableOpacity>
                                        {sanPhamDanhGia.find(sp=> sp.maSanPham === ct.maSanPham && !sp.check) ? (
                                            <TouchableOpacity style={className`px-3 py-1.25 mx-1 rounded-md border border-orange-600`}
                                                onPress={() => navigation.navigate("Evaluate", { maHoaDon: maHoaDon, maSanPham: ct.maSanPham })}
                                            >
                                                <Text style={className`text-white text-center font-bold text-base text-orange-600`}>Đánh giá</Text>
                                            </TouchableOpacity>
                                         ) : (
                                            <></>
                                        )
                                        }
                                    </View>
                               
                            </View>
                        ) : (
                            <>

                            </>
                        )}
                        </View>
                    ))}
                    
                </View>
                <View style={className`bg-[#FEFBEE] rounded-lg min-h-10 px-3 py-2`}>
                    <View style={className`py-0.75 flex flex-row`}>
                        <Text style={className`basis-2/4`}>Tổng tiền sản phẩm:</Text>
                        <Text  style={className`basis-2/4 text-right`}>{formatVND(hoaDon.tongTien - hoaDon.phiVanChuyen)}</Text>
                    </View>
                    <View style={className`py-0.75 flex flex-row`}>
                        <Text style={className`basis-2/4`}>Phí vận chuyển:</Text>
                        <Text  style={className`basis-2/4 text-right`}>{formatVND(hoaDon.phiVanChuyen)}</Text>
                    </View>
                    <View style={className`py-0.75 flex flex-row`}>
                        <Text style={className`basis-2/4`}>Tổng tiền:</Text>
                        <Text  style={className`basis-2/4 text-right text-red-500 font-medium`}>{formatVND(hoaDon.tongTien)}</Text>
                    </View>
                </View>
            </ScrollView>
        </SafeAreaView>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#F5F0DC',
        paddingHorizontal: 16
    }
});

export default DetailOrderHistory;