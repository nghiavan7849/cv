import React, { useCallback, useEffect, useState } from "react";
import { FlatList, Image, Platform, ScrollView, StyleSheet, Text, TextInput, TouchableOpacity, View } from "react-native";
// import MenuTop from "./menu/MenuTop";
import className from 'twrnc';
// import { Button, CheckBox } from "react-native-elements";
import { CheckBox } from '@rneui/themed';
import { SafeAreaView } from "react-native-safe-area-context";
import { useFocusEffect } from "@react-navigation/native";
import axios from 'axios';
import {ApiUrl} from '../service/ApiUrl';
import { formatVND } from '../service/formatVND';
import AsyncStorage from "@react-native-async-storage/async-storage";
import Toast from "react-native-toast-message";

const getCardByTaiKhoan = (maTaiKhoan, token) => {
    return axios.get(`${ApiUrl}/api/chi-tiet-gio-hang/list-by-tk?maTaiKhoan=${maTaiKhoan}`,
        {
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        }
    );
}
const updateSoLuongCart = (maCTGH, soLuong, token) => {
    return axios.put(`${ApiUrl}/api/chi-tiet-gio-hang/update?maChiTietGioHang=${maCTGH}&soLuong=${soLuong}`,
        null, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    }
    );
}

const deleteCart = (maChiTietGioHang, token) => {
    return axios.delete(`${ApiUrl}/api/chi-tiet-gio-hang/delete/${maChiTietGioHang}`,
        {
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        }
    );
}

const Cart = ({ navigation }) => {
    const [isCheckedAll, setIsCheckedAll] = useState(false);
    const [listCart, setListCart] = useState([]);
    const [totalAmount, setTotalAmount] = useState(0);
    const [token, setToken] = useState('');
    const [clickChinhSua, isClickChinhSua] = useState(false);
    const [checkButton, isCheckButton] = useState(true);

    const loadPage = async () => {
        const sessionTaiKhoanStored = JSON.parse(await AsyncStorage.getItem('sessionTaiKhoan'));
        const sessionToken = await AsyncStorage.getItem('sessionTokenTK');
        if(sessionToken){
            let res = await getCardByTaiKhoan(sessionTaiKhoanStored.maTaiKhoan, sessionToken);
            console.log(listCart);
            setToken(sessionToken);
            if (res) {
                const ctgh = res.data.data.chiTietGioHang.map((item) => ({
                    ...item,
                    isChecked: false
                }));
                setListCart(ctgh);
                setTotalAmount(0);
                isCheckButton(true);
            }
    
        } else {
            navigation.navigate('Login');
        }
        

    }

    useFocusEffect(
        useCallback(() => {
            loadPage();
        }, [])
    );

    // Click để trừ số lượng sản phẩm
    const clickTruSoLuong = async (item) => {
        if (item.soLuong > 1) {
            var soLuong = +item.soLuong - 1;
            try {
                let res = await updateSoLuongCart(item.maChiTietGioHang, soLuong, token);
                if (res) {
                    loadPage();
                }
            } catch (error) {
                console.error('Error updating quantity:', error);
            };
        }
    }
    // Click để cộng số lượng sản phẩm
    const clickCongSoLuong = async (item) => {
        var soLuong = +item.soLuong + 1;
        let res = await updateSoLuongCart(item.maChiTietGioHang, soLuong, token);
        if (res) {
            console.log(res.data.data);
            // loadPage();
            // loadPage();
            // listItemClickChon.forEach((i) => {
            //     if (i.maChiTietGioHang === item.maChiTietGioHang) {
            //         setTotalAmount(preTotalAmout => preTotalAmout + item.sanPhamGH.gia);
            //     }
            // });
            // Cập nhật lại listCart với item mới
            const ctgh = res.data.data.chiTietGioHang
            setListCart((prevState) =>
                prevState.map((cartItem) =>
                    cartItem.maChiTietGioHang === ctgh.maChiTietGioHang
                        ? { ...cartItem, soLuong: ctgh.soLuong } // Cập nhật số lượng mới
                        : cartItem
                )
            );
        }
    }

    const toggleCheckBox = (item) => {
        const updateList = listCart.map((cartItem) =>
            cartItem.maChiTietGioHang === item.maChiTietGioHang
                ? { ...cartItem, isChecked: !cartItem.isChecked } // Đảo trạng thái isChecked
                : cartItem
        );
        setListCart(updateList);
        const allSelected = updateList.every((cartItem) => cartItem.isChecked);
        setIsCheckedAll(allSelected);
        calculateToTalAmount(updateList);
        const checkBtnSelected = updateList.every((cartItem) => !cartItem.isChecked);
        isCheckButton(checkBtnSelected);

    };
    const toggleCheckBoxAll = () => {
        const updateList = listCart.map((cartItem) => ({
            ...cartItem, isChecked: !isCheckedAll
        }));
        setIsCheckedAll(!isCheckedAll);
        setListCart(updateList);
        calculateToTalAmount(updateList);
        isCheckButton(isCheckedAll);
    };

    const calculateToTalAmount = (list) => {
        let total = 0;
        list.forEach((item) => {
            if (item.isChecked) {
                total += item.sanPhamGH.gia * item.soLuong;
            }
        });
        setTotalAmount(total);
    }

    const clickXoaSP = () => {
        const listSPChon = listCart.filter((cartItem) => cartItem.isChecked);
        listSPChon.forEach(async (item) => {
            try {
                let res = await deleteCart(item.maChiTietGioHang, token);
                if (res) {
                    isCheckButton(true);
                    loadPage();
                    Toast.show({
                        type: 'success',
                        text1: 'Xóa sản phẩm thành công!',
                        text1Style: className`text-lg`
                    });
                }
            } catch (error) {
                console.log(error);
            }


        });
    }

    const clickDatHang = () => {
        const listClick = listCart.filter((item) => item.isChecked)
        navigation.navigate('PayDetail', { listCartClick: listClick });
    }

    return (
        <SafeAreaView style={styles.container}>
        
            <View style={className`flex flex-row`}>
                <View style={[className`basis-2/4 flex flex-row`, styles.clickAll]}>
                    <CheckBox
                        title='Chọn tất cả'
                        size={22}
                        checked={isCheckedAll}
                        onPress={() => toggleCheckBoxAll()}
                        containerStyle={styles.checkBox}
                        textStyle={styles.labelCheckBox}
                    />
                </View>
                <View style={[className`basis-2/4`, styles.clickUpdate]}>
                    {
                        !clickChinhSua ? (
                            <TouchableOpacity onPress={() => isClickChinhSua(true)}>
                                <Text style={styles.textUpdate} >Chỉnh sửa</Text>
                            </TouchableOpacity>
                        ) : (
                            <TouchableOpacity onPress={() => isClickChinhSua(false)}>
                                <Text style={[styles.textUpdate, className`text-red-600`]} >Xong</Text>
                            </TouchableOpacity>
                        )
                    }
                </View>
            </View>
            <FlatList
                showsVerticalScrollIndicator={false}
                data={listCart}
                keyExtractor={(item) => item.maChiTietGioHang.toString()}
                extraData={listCart}
                renderItem={(item) => (
                    <View style={[styles.cartBody, className`flex flex-row`]}>
                        <View style={[className`basis-1/10`, styles.checkBoxItem]}>
                            <CheckBox
                                size={22}
                                checked={item.item.isChecked}
                                onPress={() => toggleCheckBox(item.item)}
                                containerStyle={styles.checkBox}
                            />
                        </View>
                        <View style={[className`basis-2.5/10`, styles.devImage]}>
                            <Image
                                source={{ uri: item.item.sanPhamGH.hinhAnh }}
                                style={styles.image}
                            />
                            {/* {item.item} */}
                        </View>
                        <View style={className`basis-6.5/10`}>
                            <View style={styles.infoItem}>
                                <Text style={styles.infoItem.name}>{item.item.sanPhamGH.tenSanPham}</Text>
                            </View>
                            <View style={[styles.infoQuaPriceItem, className`flex flex-row`]}>
                                <View style={className`basis-3/6`}>
                                    <Text style={styles.infoQuaPriceItem.price}>{formatVND(item.item.sanPhamGH.gia)}</Text>
                                </View>
                                <View style={[className`basis-3/6 flex flex-row`, styles.divQuantity]}>
                                    <TouchableOpacity style={styles.buttonQuantity} onPress={() => clickTruSoLuong(item.item)}>
                                        <Text style={styles.buttonQuantityText}>-</Text>
                                    </TouchableOpacity>
                                    <Text style={styles.quantityText}>{item.item.soLuong}</Text>
                                    <TouchableOpacity style={styles.buttonQuantity}>
                                        <Text style={styles.buttonQuantityText} onPress={() => clickCongSoLuong(item.item)}>+</Text>
                                    </TouchableOpacity>
                                </View>
                            </View>
                        </View>
                    </View>
                )}

            />
            <View style={[styles.footer, className`flex flex-row`]}>
                {
                    !clickChinhSua ? (
                        <>
                            <View style={[className`basis-2/4 `, styles.divTongTien]}>
                                <Text style={styles.labelTongTien}>{formatVND(totalAmount)}</Text>
                            </View>
                            <TouchableOpacity style={className`basis-2/4 items-center`} disabled={checkButton} onPress={() => clickDatHang()}>
                                <View style={[styles.buttonDatHang, className`${checkButton ? 'bg-[#b0b2b5]' : 'bg-[#53A654]'}`]}>
                                    <Text style={styles.labelButton}>Đặt hàng</Text>
                                </View>
                            </TouchableOpacity>
                        </>
                    ) : (
                        <>
                            <View style={[className`basis-2/4`, styles.divTongTien]}></View>
                            <TouchableOpacity style={className`basis-2/4 items-center`} disabled={checkButton} onPress={() => clickXoaSP()}>
                                <View style={[styles.buttonDatHang, className`${checkButton ? 'bg-[#b0b2b5]' : 'bg-red-500'}`]}>
                                    <Text style={styles.labelButton}>Xóa</Text>
                                </View>
                            </TouchableOpacity>
                        </>
                    )
                }

            </View>
        </SafeAreaView>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#F5F0DC',
        paddingHorizontal: 16,
        paddingBottom: Platform.OS === 'android' ? 0 : -34,
    },
    clickAll: {
        // backgroundColor:'#007AFF',
    },
    checkBox: {
        marginLeft: 5,
        paddingLeft: 5,
        backgroundColor: '#F5F0DC',
        borderWidth: 0
    },
    labelCheckBox: {
        marginLeft: 5,
        fontSize: 16,
        fontWeight: 'normal'
    },
    clickUpdate: {
        justifyContent: 'center',
    },
    textUpdate: {
        textAlign: 'right',
        marginRight: 10,
        fontSize: 16
    },
    cartBody: {
        height: 110,
        borderBottomWidth: 1,
        borderColor: '#CCCCCC'
    },
    checkBoxItem: {
        justifyContent: 'center'
    },
    devImage: {
        justifyContent: 'center',
        alignItems: 'center'
    },
    image: {
        height: 80,
        width: 80,
        borderRadius: 5
    },
    infoItem: {
        height: 65,
        name: {
            fontSize: 16,
            marginTop: 10,
            marginLeft: 5
        }
    },
    infoQuaPriceItem: {
        height: 45,
        price: {
            fontWeight: 'bold',
            color: 'red',
            fontSize: 16,
            marginTop: 5,
            marginLeft: 5
        },

    },
    divQuantity: {
        justifyContent: 'flex-end',
        // alignItems: '',
    },
    buttonQuantity: {
        backgroundColor: '#F5F0DC',
        width: 30,
        borderRadius: 35,
        marginHorizontal: 5,
        height: 30,
        justifyContent: 'center',
        alignItems: 'center',
        borderWidth: 1,
        borderColor: '#999999'
    },
    buttonQuantityText: {
        fontSize: 18,
        color: '#000000',
        fontWeight: 'bold',
        marginTop: 0
    },
    quantityText: {
        fontSize: 18,
        marginHorizontal: 8,
        color: '#000',
    },
    footer: {
        marginHorizontal: -16,
        height: 80,
        backgroundColor: '#F8F8FF'
    },
    buttonDatHang: {
        // backgroundColor: '#53A654',
        width: 150,
        height: 50,
        justifyContent: 'center',
        alignItems: 'center',
        borderRadius: 10,
        marginLeft: 20,
        marginTop: 15
    },
    labelButton: {
        fontSize: 18,
        color: '#FFF'
    },
    divTongTien: {
        height: 50,
        marginTop: 15,
        paddingRight: -10,
        fontSize: 18,
        justifyContent: "center",
    },
    labelTongTien: {
        textAlign: 'right',
        fontSize: 18,
        fontWeight: 'bold',
        color: '#000'
    }
});

export default Cart;