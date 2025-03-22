import React, { useCallback, useState } from "react";
import { StyleSheet, Text, View, TouchableOpacity, ScrollView, Image, TextInput, FlatList, Modal, Pressable } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import MenuTop from "./menu/MenuTop";
import className from 'twrnc';
import { Ionicons } from '@expo/vector-icons';
import { formatVND } from '../service/formatVND';
import { CheckBox } from "react-native-elements";
import { useFocusEffect } from "@react-navigation/native";
import {ApiUrl} from '../service/ApiUrl';
import AsyncStorage from "@react-native-async-storage/async-storage";
import axios from 'axios';
import Toast from "react-native-toast-message";
import { WebView } from 'react-native-webview';
import Url from "../service/Url";

const getListDiaChiByMaTK = (maTaiKhoan, token) => {
    return axios.get(`${ApiUrl}/api/dia-chi/list-by-matk?maTaiKhoan=${maTaiKhoan}`, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
}
const creatHoaDon = (data, token) => {
    return axios.post(`${ApiUrl}/api/hoa-don/create`, data, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
}

const creatChiTietHoaDon = (data, token) => {
    return axios.post(`${ApiUrl}/api/chi-tiet-hoa-don/create`, data, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
}

const truSoLuongSP = (maSanPham, soLuong, token) => {
    return axios.put(`${ApiUrl}/api/san-pham/tru-so-luong?maSanPham=${maSanPham}&soLuong=${soLuong}`, null, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
}
const deleteChiTietGioHang = (maChiTietGioHang, token) => {
    return axios.delete(`${ApiUrl}/api/chi-tiet-gio-hang/delete/${maChiTietGioHang}`, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
}
const createPaymentLink = (data, token) => {
    return axios.post(`${ApiUrl}/api/payos/create-payment-link`, data,
        {
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        }
    );
}
const postApiTinhPhiVanChuyen = (code) => {
    return axios.post('https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services',
        {
            shop_id: 192476, // Thay bằng shop_id của bạn nếu cần
            from_district: 1574,
            to_district: parseInt(code)
        },
        {
            headers: {
                'Content-Type': 'application/json',
                'Token': 'bab9fb8c-2323-11ef-a951-76299e96dead'
            }

        }
    );
};
const getPhiVanChuyen = (data) => {
    return axios.post(`https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee`, data,
        {
            headers: {
                'Content-Type': 'application/json',
                'Token': 'bab9fb8c-2323-11ef-a951-76299e96dead',
                'ShopId': 192476
            }
        }
    );
}
const PayDetail = ({ route, navigation }) => {
    const [radioCOD, isRadioCOD] = useState(false);
    const [radioVNPAY, isRadioVNPAY] = useState(false);
    const [radioQRCode, isRadioQRCode] = useState(false);
    const [paymentMethod, isPaymentMethod] = useState(true);
    const [paymentMethodType, isPaymentMethodType] = useState(true);
    const [modalVisible, setModalVisible] = useState(false);
    const { listCartClick = [] } = route.params || {};
    const [totalProduct, setTotalProduct] = useState(0);
    const [shippingFee, setShippingFee] = useState(30000);
    const [totalPay, setTotalPay] = useState(0);
    const [token, setToken] = useState('');
    const [taiKhoan, setTaiKhoan] = useState({});

    const [valueNotes, setValueNotes] = useState("");
    const [listDiaChi, setListDiaChi] = useState([]);
    const [diaChi, setDiaChi] = useState({});
    const [showWebView, isShowWebView] = useState(false);

    const [totalWeight, setTotalWeight] = useState(0);
    const [totalTemp, setTotalTemp] = useState(0);

    const loadPage = async () => {
        const sessionTaiKhoanStored = JSON.parse(await AsyncStorage.getItem('sessionTaiKhoan'));
        const sessionToken = await AsyncStorage.getItem('sessionTokenTK');
        setToken(sessionToken);
        setTaiKhoan(sessionTaiKhoanStored);
        isRadioCOD(true);
        isRadioQRCode(false);
        let letTotalWeight = 0;
        let letTotalTam = 0;
        listCartClick.forEach(item => {
            letTotalWeight = letTotalWeight + item.sanPhamGH.trongLuong * item.soLuong;
            letTotalTam += item.sanPhamGH.gia * item.soLuong;
            setTotalProduct(totalProduct + item.sanPhamGH.gia * item.soLuong);
        });
        setTotalWeight(letTotalWeight);
        setTotalTemp(letTotalTam);
        // setTotalPay(tempTotalWeightProduct + shippingFee);
        let res = await getListDiaChiByMaTK(sessionTaiKhoanStored.maTaiKhoan, sessionToken);
        if (res) {
            setListDiaChi(res.data.data.diaChi.filter(item => item.trangThaiXoa === false && item.trangThai === false));
            res.data.data.diaChi.forEach(item => {
                if (item.trangThai) {
                    setDiaChi(item);
                    tinhPhiVanChuyen(item, letTotalWeight, letTotalTam);
                }
            })
        }

    }

    const tinhPhiVanChuyen = async (item, ttWeight, ttTemp) => {
        let wardCodeTo = item.maXaPhuong;
        let districtIdTo = item.maQuanHuyen;
        let provinceIdTo = item.maTinhThanh;

        let res = await postApiTinhPhiVanChuyen(districtIdTo);
        if (res) {
            const data = {
                from_district_id: 1574,
                from_ward_code: '550307',
                service_id: res.data.data[0].service_id,
                service_type_id: res.data.data[0].service_type_id,
                to_district_id: districtIdTo,
                to_ward_code: String(wardCodeTo),
                weight: parseFloat(ttWeight)
            }
            let resPhi = await getPhiVanChuyen(data);
            setShippingFee(resPhi.data.data.total);
            setTotalPay(ttTemp + resPhi.data.data.total);

        }
    }


    useFocusEffect(
        useCallback(() => {
            loadPage();
        }, [])
    );

    const clickRadioCOD = () => {
        isRadioCOD(true);
        isRadioVNPAY(false);
        isRadioQRCode(false);
        isPaymentMethod(true);
    }

    const clickRadioVNPAY = () => {
        isRadioCOD(false);
        isRadioVNPAY(true);
        isRadioQRCode(false);
        isPaymentMethod(false);
        isPaymentMethodType(true);
    }

    const clickRadioQRCode = () => {
        isRadioCOD(false);
        isRadioVNPAY(false);
        isRadioQRCode(true);
        isPaymentMethod(false);
        isPaymentMethodType(false);
    }

    const clickDatHang = async () => {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0'); // Tháng được tính từ 0-11 nên cần +1 và thêm '0' nếu cần
        const day = String(today.getDate()).padStart(2, '0'); // Thêm '0' nếu ngày nhỏ hơn 10

        const formattedDate = `${year}-${month}-${day}`; // Định dạng YYYY-MM-DD

        const data = {
            "phiVanChuyen": shippingFee,
            "tongTien": totalPay,
            "phuongThucThanhToan": paymentMethod,
            "ngayThanhToan": formattedDate,
            "ghiChu": valueNotes,
            "trangThai": false,
            "diaChiNhan": diaChi.diaChiChiTiet + ', ' + diaChi.diaChi,
            "dcHoTen": diaChi.hoVaTen,
            "dcSoDienThoai": diaChi.soDienThoai,
            "maTaiKhoan": taiKhoan.maTaiKhoan,
            "maTrangThaiHoaDon": 1
        }
        // Đặt hàng khi chọn thanh toán khi nhận hàng
        if (paymentMethod) {
            data.trangThai = true;
            try {
                let res = await creatHoaDon(data, token);
                if (res) {
                    Toast.show({
                        type: 'success',
                        text1: 'Đặt hàng thành công',
                        text1Style: className`text-lg`
                    });
                    listCartClick.forEach(item => {
                        const dataCTHD = {
                            "soLuong": item.soLuong,
                            "gia": item.sanPhamGH.gia,
                            "maHoaDon": res.data.data.hoaDon.maHoaDon,
                            "maSanPham": item.sanPhamGH.maSanPham
                        };
                        truSoLuongSP(item.sanPhamGH.maSanPham, item.soLuong, token);
                        creatChiTietHoaDon(dataCTHD, token);
                        deleteChiTietGioHang(item.maChiTietGioHang, token);
                    });

                    navigation.navigate("OrderConfirmation");
                }
            } catch (error) {
                console.log(error)
            }

        } else {
            // console.log("hehe1111");
            // console.log('paymentMethodType', paymentMethodType)
            // Đặt hàng khi chọn phương thức thanh toán bằng VNPAY (true)
            if (paymentMethodType) {
            } else { // Đặt hàng khi chọn phương thức thanh toán bằng QRCode(false)
                console.log('data', data)
                data.maTrangThaiHoaDon = 1;
                try {
                    let res = await creatHoaDon(data, token);
                    if (res) {
                        listCartClick.forEach(item => {
                            const dataCTHD = {
                                "soLuong": item.soLuong,
                                "gia": item.sanPhamGH.gia,
                                "maHoaDon": res.data.data.hoaDon.maHoaDon,
                                "maSanPham": item.sanPhamGH.maSanPham
                            };
                            creatChiTietHoaDon(dataCTHD, token);
                            deleteChiTietGioHang(item.maChiTietGioHang, token);
                        });
                        // Lưu thông tin vào sessionStorage
                        // sessionStorage.setItem("paymentMethod", JSON.stringify(paymentMethod));
                        // sessionStorage.setItem("dataHoaDon", JSON.stringify(res.data.data.hoaDon));
                        // sessionStorage.setItem("listItemClickChon", JSON.stringify(listItemClickChon));
                        const dataPay = {
                            "productName": "Thanh toán hóa đơn",
                            "description": `HD${res.data.data.hoaDon.maHoaDon}`,
                            "returnUrl": `${Url}/home/pay`,
                            "price": totalPay,
                            "cancelUrl": `${Url}/home`
                        }
                        let resPay = await createPaymentLink(dataPay, token);
                        if (resPay) {
                            const urlPayOs = resPay.data.data.checkoutUrl;
                            // console.log('urlPayOs', urlPayOs);
                            navigation.navigate('QRCode', { UrlPayment: urlPayOs, dataHoaDon: res.data.data.hoaDon });
                        }
                    }
                } catch (error) {
                    console.log('errorQRCode', error)

                }
            }
        }
    }

    // const handleNavigationStateChange = (navState) => {
    //     const { url } = navState;

    //     console.log(url);
    //     console.log('HHIHaaaaa');
    //     // Kiểm tra URL trả về và xử lý logic
    //     if (url.includes('https://fivefood.shop/home/pay')) {
    //         console.log('success');
    //         isShowWebView(false); // Ẩn WebView
    //     } else if (url.includes('https://fivefood.shop/home')) {
    //         console.log('cancel');
    //         isShowWebView(false); // Ẩn WebView
    //     }
    // };


    return (
        <SafeAreaView style={[styles.container, className`relative`]}>
            <MenuTop title={"Thông tin thanh toán"} />
            <ScrollView style={className`mt-2 mb-25`} showsVerticalScrollIndicator={false}>
                <TouchableOpacity style={className`min-h-17 bg-[#f7f6f0] rounded-lg flex flex-row`} onPress={() => navigation.navigate('Address')}>
                    <View style={className`mt-3 mb-3 ml-1 basis-8/9`}>
                        <Text style={className`ml-3`}><Text style={className`text-base font-bold  text-[#000000]`}>{diaChi.hoVaTen} </Text><Text style={className`text-base font-bold   text-[#000000]`}>(+84) {diaChi.soDienThoai}</Text><Text></Text></Text>
                        <Text style={className`ml-3 text-sm text-[#000000] pr-2`} numberOfLines={1} ellipsizeMode='taill'>{diaChi.diaChiChiTiet + ', ' + diaChi.diaChi}</Text>
                    </View>
                    <View style={className`basis-1/9 justify-center`}>
                        <Ionicons name="chevron-forward" size={28} color="black-white" />
                    </View>
                </TouchableOpacity>
                <View style={className`min-h-30 mt-2 bg-[#f7f6f0] rounded-lg flex flex-row`} >
                    <View style={className`mb-3`}>
                        <FlatList
                            data={listCartClick}
                            keyExtractor={(item) => item.maChiTietGioHang.toString()}
                            renderItem={(item) => (
                                <View style={className`min-h-22 mt-2 flex flex-row border-b border-slate-300`} >
                                    <View style={className`basis-3/10 justify-start items-center mt-1`}>
                                        <Image
                                            source={{ uri: `${item.item.sanPhamGH.hinhAnh}` }}
                                            style={className`h-20 w-20 rounded-2`}
                                        />
                                    </View>
                                    <View style={className`basis-7/10 pr-2`}>
                                        <View style={className`h-12 mt-1`}>
                                            <Text style={className`text-black text-base w-full mr-1 max-w-100`} numberOfLines={1} ellipsizeMode='taill'>{item.item.sanPhamGH.tenSanPham}</Text>
                                            <View style={className`flex flex-row justify-between mt-0.5`}>
                                                <Text style={className`mt-1.5 mr-1 font-semibold`}>{formatVND(item.item.sanPhamGH.gia)}</Text>
                                                <Text style={className`mt-1.5 mr-1`}>x{item.item.soLuong}</Text>
                                            </View>
                                        </View>
                                        <View style={className`h-10 justify-center items-end mt-1`}>
                                            <Text style={className`text-black text-sm mr-1`}>Tổng tiền: <Text style={className`font-semibold`}>{formatVND(item.item.sanPhamGH.gia * item.item.soLuong)}</Text></Text>
                                        </View>
                                    </View>
                                </View>
                            )}
                            scrollEnabled={false}
                        />



                        <View style={className`mt-3 min-h-5 px-3`}>
                            <View style={className`flex flex-row`}>
                                <View style={className`basis-2/4`}>
                                    <Text style={className`text-base`}>Tổng tiền sản phẩm</Text>
                                </View>
                                <View style={className`basis-2/4`}>
                                    <Text style={className`text-base text-right`}>{formatVND(totalProduct)}</Text>
                                </View>
                            </View>
                            <View style={className`flex flex-row mt-1`}>
                                <View style={className`basis-2/4`}>
                                    <Text style={className`text-base`}>Phí vận chuyển</Text>
                                </View>
                                <View style={className`basis-2/4`}>
                                    <Text style={className`text-base text-right`}>{formatVND(shippingFee)}</Text>
                                </View>
                            </View>
                            <View style={className`flex flex-row mt-1`}>
                                <View style={className`basis-2/4`}>
                                    <Text style={className`text-base font-semibold`}>Tổng thanh toán</Text>
                                </View>
                                <View style={className`basis-2/4`}>
                                    <Text style={className`text-base text-right font-semibold`}>{formatVND(totalPay)}</Text>
                                </View>
                            </View>
                        </View>
                    </View>
                </View>
                <View style={className`min-h-10 mt-2 bg-[#f7f6f0] rounded-lg py-2 px-3`}>
                    <View>
                        <Text style={className`text-base font-medium`}>Phương thức thanh toán</Text>
                    </View>
                    <TouchableOpacity style={className`mt-2 flex flex-row`} onPress={() => clickRadioCOD()}>
                        <View style={className`basis-7/8 flex flex-row`}>
                            <View style={className`basis-1/9`}>
                                <Image
                                    style={className`h-5 w-6 basis-1/6`}
                                    source={require('../assets/COD.png')}
                                />
                            </View>
                            <View style={className`basis-8/9`}>
                                <Text style={className`text-sm `}>Thanh toán khi nhận hàng (COD)</Text>
                            </View>
                        </View>
                        <View style={className`basis-1/8 items-end`}>
                            <CheckBox
                                size={22}
                                containerStyle={className`p-0 m-0 `}
                                checked={radioCOD}
                                onPress={() => clickRadioCOD()}
                                checkedIcon="dot-circle-o"
                                uncheckedIcon="circle-o"
                            />
                        </View>
                    </TouchableOpacity>
                    {/* <TouchableOpacity style={className`mt-2 flex flex-row`} onPress={() => clickRadioVNPAY()}>
                        <View style={className`basis-7/8 flex flex-row`}>
                            <View style={className`basis-1/9`}>
                                <Image
                                    style={className`h-5 w-6 basis-1/6`}
                                    source={require('../assets/VNPAY.png')}
                                />
                            </View>
                            <View style={className`basis-8/9`}>
                                <Text style={className`text-sm `}>VNPAY</Text>
                            </View>
                        </View>
                        <View style={className`basis-1/8 items-end`}>
                            <CheckBox
                                size={22}
                                containerStyle={className`p-0 m-0 `}
                                checked={radioVNPAY}
                                onPress={() => clickRadioVNPAY()}
                                checkedIcon="dot-circle-o"
                                uncheckedIcon="circle-o"
                            />
                        </View>
                    </TouchableOpacity> */}
                    <TouchableOpacity style={className`mt-2 mb-1.5 flex flex-row`} onPress={() => clickRadioQRCode()}>
                        <View style={className`basis-7/8 flex flex-row`}>
                            <View style={className`basis-1/9`}>
                                <Image
                                    style={className`h-5 w-6 basis-1/6`}
                                    source={require('../assets/PayOS.png')}
                                />
                            </View>
                            <View style={className`basis-8/9`}>
                                <Text style={className`text-sm `}>Pay OS</Text>
                            </View>
                        </View>
                        <View style={className`basis-1/8 items-end`}>
                            <CheckBox
                                size={22}
                                containerStyle={className`p-0 m-0 `}
                                checked={radioQRCode}
                                onPress={() => clickRadioQRCode()}
                                checkedIcon="dot-circle-o"
                                uncheckedIcon="circle-o"
                            />
                        </View>
                    </TouchableOpacity>
                </View>

                <View style={[styles.group, className`mt-2 mb-5 bg-[#f7f6f0] rounded-lg py-2 px-3`]}>
                    <Text style={styles.label}>Ghi chú đơn hàng (tùy chọn)</Text>
                    <TextInput
                        style={[styles.input, className`border border-slate-400 rounded mt-1`]}
                        placeholder='Nhập ghi chú về đơn hàng'
                        value={valueNotes}
                        onChangeText={(text) => setValueNotes(text)}
                        multiline={true}
                        numberOfLines={4}
                    />
                </View>
            </ScrollView>
            <View style={className`bg-[#FEFBEE] absolute bottom-0 inset-x-0 h-25 px-6`}>
                <TouchableOpacity style={className`mt-4 bg-[#067A38] px-11.5 py-3 rounded-lg`} onPress={() => clickDatHang()}>
                    <Text style={className`text-white text-center font-bold text-lg`}>Đặt hàng</Text>
                </TouchableOpacity>
            </View>
            {/* <Modal
                animationType="slide"
                transparent={true}
                visible={modalVisible}
                onRequestClose={() => {
                    setModalVisible(!modalVisible);
                }}>
                <TouchableOpacity style={className`h-1/5`} onPress={() => setModalVisible(!modalVisible)}>

                </TouchableOpacity>
                <View style={className`h-4/5 bg-white absolute bottom-0 left-0 right-0 rounded-3xl`}>
                    <View style={className`mt-2 mr-2 rounded-3xl justify-center flex flex-row`}>
                        <View style={className`basis-8/10`}></View>
                        <TouchableOpacity style={className` basis-2/10`} onPress={() => setModalVisible(!modalVisible)} >
                            <Ionicons style={className`py-1 text-right pr-3 `} name="close-circle-outline" size={42} color="black-white" />
                        </TouchableOpacity>
                    </View>
                    <View style={className`mt-2 bg-red-600 h-30`}>
                        <TouchableOpacity style={className`flex flex-row`}>
                            <View style={className`mt-3 mb-3 ml-1 basis-8/9`}>
                                <Text style={className`ml-3`}><Text style={className`text-base font-bold  text-[#000000]`}>{diaChi.hoVaTen} </Text><Text style={className`text-base font-bold   text-[#000000]`}>(+84) {diaChi.soDienThoai}</Text><Text></Text></Text>
                                <Text style={className`ml-3 text-sm text-[#000000] pr-2`} numberOfLines={1} ellipsizeMode='taill'>{diaChi.diaChiChiTiet + ', ' + diaChi.diaChi}</Text>
                            </View>
                            <View style={className`basis-1/9 justify-center`}>
                                <Ionicons name="chevron-forward" size={28} color="black-white" />
                            </View>
                        </TouchableOpacity>
                    </View>
                </View>
            </Modal> */}
        </SafeAreaView>

    )
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#F5F0DC',
        paddingHorizontal: 16
    },
    group: {
        minHeight: 90
    },
    label: {
        fontSize: 16
    },
});

export default PayDetail;