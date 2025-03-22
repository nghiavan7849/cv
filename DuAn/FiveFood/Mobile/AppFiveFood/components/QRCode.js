import React, { useCallback, useState } from "react";
import { View } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import WebView from "react-native-webview";
import className from 'twrnc';
import { useFocusEffect } from "@react-navigation/native";
import axios from "axios";
import ApiUrl from "../service/ApiUrl";
import AsyncStorage from "@react-native-async-storage/async-storage";

const updateTrangThai = (maHoaDon, trangThai, token) => {
    return axios.put(`${ApiUrl}/api/hoa-don/cap-nhat-trang-thai?maHoaDon=${maHoaDon}&trangThai=${trangThai}&lyDoHuy=`, null, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
};

const updateStatusPaymet = (maHoaDon, status, token) => {
    return axios.put(`${ApiUrl}/api/hoa-don/update-status-payment/${maHoaDon}?status=${status}`, null, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
}

const getPayment = (id, token) => {
    return axios.get(`${ApiUrl}/api/payos/get-payment/${id}`, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
}

const createGiaoDich = (data, token) => {
    return axios.post(`${ApiUrl}/api/giao-dich/create`, data,
        {
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        }
    );
}

const QRCode = ({ route, navigation }) => {
    const [hiddenPayOs, isHiddenPayOs] = useState(true);
    const { UrlPayment, dataHoaDon = [] } = route.params || '';
    const [token, setToken] = useState('');

    const loadPage = async () => {
        const sessionToken = await AsyncStorage.getItem('sessionTokenTK');
        setToken(sessionToken);
    }

    useFocusEffect(
        useCallback(() => {
            loadPage();
        }, [])
    );

    const handleNavigationStateChange = (navState) => {
        const { url } = navState;
        console.log('url', url)

        const urlObj = new URL(url);
        console.log('urlObj', urlObj)

        // Lấy phần đường dẫn (pathname)
        const basePath = urlObj.origin + urlObj.pathname;

        // Lấy tham số (query params)
        const params = Object.fromEntries(urlObj.searchParams.entries());

        console.log('Base Path:', basePath);
        console.log('Params:', params);
        console.log("url:", url);

        if (url.includes('https://fivefood.shop/home/pay')) {
            isHiddenPayOs(false);
            PaymenSucces(params.orderCode, dataHoaDon);
            navigation.navigate("OrderConfirmation");
        } else if (url.includes('https://fivefood.shop/home')) {
            isHiddenPayOs(false);

            navigation.navigate("Menu");
        }
    };

    const PaymenSucces = async (orderCode, dataHD) => {
        try {
            if (orderCode) {
                console.log(token);
                let res = await getPayment(orderCode, token);
                if (res.data.data.status === "PAID") {
                    console.log("thành công");
                    await updateTrangThai(dataHD.maHoaDon, "dadathang", token);
                    await updateStatusPaymet(dataHD.maHoaDon, true, token);
                    const data = {
                        "codeGiaoDich": orderCode,
                        "soTaiKhoan": res.data.data.transactions[0].counterAccountNumber,
                        "hoVaTen": res.data.data.transactions[0].counterAccountName,
                        "ngayGiaoDich": res.data.data.transactions[0].transactionDateTime,
                        "trangThai": true,
                        "soTien": res.data.data.transactions[0].amount,
                        "maTaiKhoan": sessionTaiKhoan.maTaiKhoan,
                        "maHoaDon": dataHD.maHoaDon
                    }
                    await createGiaoDich(data, token);
                }
                console.log("Thất bại");
                // navigate("/home/pay", {replace:true})
            }
        } catch (error) {
            console.log('errorPaymenSuccess: ', error)
        }

    }

    const PaymenCancel = async (orderCode, dataHD) => {
        if (orderCode) {
            if (dataHD) {
                await updateTrangThai(dataHD.maHoaDon, "dahuy", "Thanh toán thất bại", token);
            }
        }
    }

    return (
        <SafeAreaView >

            {hiddenPayOs ?
                (
                    <View style={className`bg-red-600 w-full h-full`}>
                        <WebView
                            containerStyle={className``}
                            source={{ uri: `${UrlPayment}` }} // URL thanh toán của PayOS
                            onNavigationStateChange={handleNavigationStateChange}
                        />
                    </View>
                ) : (
                    <></>
                )
            }


        </SafeAreaView>
    )
}

export default QRCode;