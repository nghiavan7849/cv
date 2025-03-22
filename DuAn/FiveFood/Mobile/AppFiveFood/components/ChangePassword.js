import React, { useCallback, useEffect, useState } from 'react';
import { Button, StyleSheet, Text, TextInput, TouchableOpacity, View } from "react-native";
import className from 'twrnc';
import MenuTop from './menu/MenuTop';
import { useRef } from 'react';
import { SafeAreaView } from 'react-native-safe-area-context';
import Toast from 'react-native-toast-message';
import { Ionicons } from '@expo/vector-icons';
import axios from 'axios';
import {ApiUrl} from '../service/ApiUrl';
import { useFocusEffect } from '@react-navigation/native';
import AsyncStorage from '@react-native-async-storage/async-storage';

const apiChangePassword = (data, tokenapi) => {
    return axios.put(`${ApiUrl}/api/tai-khoan/change-password`, data,
        {
            headers: {
                'Authorization': `Bearer ${tokenapi}`,
            },
        }
    );
}

const ChangePassword = ({ navigation }) => {

    const inputRefMKC = useRef(null);
    const inputRefMKM = useRef(null);
    const inputRefXNMKM = useRef(null);

    const [matKhauCu, setMatKhauCu] = useState('');
    const [matKhauMoi, setMatKhauMoi] = useState('');
    const [confirmMatKhau, setConfirmMatKhau] = useState('');

    const [matKhauCuErros, setMatKhauCuErros] = useState('');
    const [matKhauMoiErros, setMatKhauMoiErros] = useState('');
    const [confirmMatKhauErros, setConfirmMatKhauErros] = useState('');

    const [showPasswordOld, isShowPasswordOld] = useState(true);
    const [showPasswordNew, isShowPasswordNew] = useState(true);
    const [showPasswordConfirm, isShowPasswordConfirm] = useState(true);
    
    const [token, setToken] = useState('');
    const [taiKhoan, setTaiKhoan] = useState({});

    const loadPage = async() => {
        const sessionTaiKhoanStored = JSON.parse(await AsyncStorage.getItem('sessionTaiKhoan'));
        const sessionToken = await AsyncStorage.getItem('sessionTokenTK');
        setToken(sessionToken);
        setTaiKhoan(sessionTaiKhoanStored);
    }

    useFocusEffect(
        useCallback(() => {
            loadPage();
        }, [navigation])
    );

    const onChangeOldPassword = (text) => {
        setMatKhauCu(text);
        setMatKhauCuErros('');
    }

    const onChangeNewPassword = (text) => {
        setMatKhauMoi(text);
        setMatKhauMoiErros('');
    }

    const onChangeConfirmPassword = (text) => {
        setConfirmMatKhau(text);
        setConfirmMatKhauErros('');
    }

    const formValid = () => {
        let checkValid = true;
        if (matKhauCu === '') {
            setMatKhauCuErros('Vui lòng nhập mật khẩu cũ!!!');
            checkValid = false;
        } else {
            if (matKhauCu.length < 8) {
                setMatKhauCuErros('Mật khẩu cũ phải có ít nhất 8 ký tự!!!');
                checkValid = false;
            }
        }
        if (matKhauMoi === '') {
            setMatKhauMoiErros('Vui lòng nhập mật khẩu mới!!!');
            checkValid = false;
        } else {
            if (matKhauMoi.length < 8) {
                setMatKhauMoiErros('Mật khẩu mới phải có ít nhất 8 ký tự!!!');
                checkValid = false;
            }
        }
        if (confirmMatKhau === '') {
            setConfirmMatKhauErros('Vui lòng nhập xác nhận mật khẩu mới!!!');
            checkValid = false;
        } else {
            if (confirmMatKhau.length < 8) {
                setConfirmMatKhauErros('Xác nhận mật khẩu phải có ít nhất 8 ký tự!!!');
                checkValid = false;
            } else if (confirmMatKhau !== matKhauMoi){
                setConfirmMatKhauErros('Xác nhận mật khẩu không trùng khớp!!!');
                checkValid = false;
            }
        }
        return checkValid;
    }

    const clickLuuThayDoi = async () => {
        if (formValid()) {
            const data = {
                "tenDangNhap": taiKhoan.tenDangNhap,
                "matKhau": matKhauCu,
                "changeMatKhau": matKhauMoi
            }
            try {
                const res = await apiChangePassword(data, token);
                if (res.data.status === 'success') {
                    Toast.show({
                        type: 'success',
                        text1: 'Đổi mật khẩu thành công!',
                        text1Style: className`text-lg`
                    });
                    setMatKhauCu('');
                    setMatKhauMoi('');
                    setConfirmMatKhau('');
                } else if (res.data.status === 'error') {
                    setMatKhauCuErros("Mật khẩu cũ không chính!!!");
                }
            } catch (error) {
                console.log(error);
            }
        }
    }



    return (
        <SafeAreaView style={styles.container}>
            <MenuTop title={"Đổi mật khẩu"} />
            <View style={styles.body}>
                <View style={styles.group}>
                    <Text style={styles.label}>Mật khẩu cũ <Text style={{ color: 'red' }}>*</Text></Text>
                    <TextInput
                        ref={inputRefMKC}
                        style={styles.input}
                        placeholder='Mật khẩu cũ'
                        onSubmitEditing={() => inputRefMKM.current.focus()}
                        secureTextEntry={showPasswordOld}
                        value={matKhauCu}
                        onChangeText={(text) => onChangeOldPassword(text)}
                    />
                    <Text style={styles.error}>{matKhauCuErros}</Text>
                    <TouchableOpacity style={styles.eyePass} onPress={() => isShowPasswordOld(!showPasswordOld)}>
                        <Ionicons name={showPasswordOld ? 'eye-off-outline' : 'eye-outline'} size={26} />
                    </TouchableOpacity>
                </View>

                <View style={styles.group}>
                    <Text style={styles.label}>Mật khẩu mới <Text style={{ color: 'red' }}>*</Text></Text>
                    <TextInput
                        ref={inputRefMKM}
                        style={styles.input}
                        placeholder='Mật khẩu mới'
                        onSubmitEditing={() => inputRefXNMKM.current.focus()}
                        secureTextEntry={showPasswordNew}
                        value={matKhauMoi}
                        onChangeText={(text) => onChangeNewPassword(text)}
                    />
                    <Text style={styles.error}>{matKhauMoiErros}</Text>
                    <TouchableOpacity style={styles.eyePass} onPress={() => isShowPasswordNew(!showPasswordNew)}>
                        <Ionicons name={showPasswordNew ? 'eye-off-outline' : 'eye-outline'} size={26} />
                    </TouchableOpacity>
                </View>
                <View style={styles.group}>
                    <Text style={styles.label}>Xác nhận mật khẩu mới <Text style={{ color: 'red' }}>*</Text></Text>
                    <TextInput
                        ref={inputRefXNMKM}
                        style={styles.input}
                        placeholder='Xác nhận mật khẩu mới'
                        secureTextEntry={showPasswordConfirm}
                        value={confirmMatKhau}
                        onChangeText={(text) => onChangeConfirmPassword(text)}
                    />
                    <Text style={styles.error}>{confirmMatKhauErros}</Text>
                    <TouchableOpacity style={styles.eyePass} onPress={() => isShowPasswordConfirm(!showPasswordConfirm)}>
                        <Ionicons name={showPasswordConfirm ? 'eye-off-outline' : 'eye-outline'} size={26} />
                    </TouchableOpacity>
                </View>
            </View>
            <TouchableOpacity style={styles.butttonEnd} onPress={() => clickLuuThayDoi()}>
                <Text style={styles.butttonEnd.text}>LƯU THAY ĐỔI</Text>
            </TouchableOpacity>
        </SafeAreaView>
    )
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#F5F0DC',
        paddingHorizontal: 16,
    },
    body: {
        // backgroundColor:'#F9DCDC',
        marginTop: 20,
        minHeight: 100
    },
    group: {
        marginTop: 5,
        marginHorizontal: 15,
        // backgroundColor:'#FFF',
        minHeight: 50
    },
    label: {
        marginTop: 5,
        fontSize: 16
    },
    input: {
        position: 'relative',
        borderRadius: 10,
        height: 50,
        // backgroundColor:'#000',
        backgroundColor: '#fff',
        marginTop: 5,
        paddingLeft: 10
    },
    eyePass: {
        position: 'absolute',
        right: 10,
        bottom: 25,
    },
    forgotPassword: {
        alignItems: 'flex-end',
        marginHorizontal: 15,
        zIndex: 10
    },
    error: {
        fontSize: 14,
        color: 'red',
        marginTop: 2,
        marginBottom: -5
    },
    butttonEnd: {
        position: 'absolute',
        bottom: 20,
        left: 30,
        right: 30,
        height: 60,
        justifyContent: 'center',
        alignItems: 'center',
        borderRadius: 10,
        fontWeight: 'bold',
        backgroundColor: '#53A654',
        text: {
            color: '#fff',
            fontSize: 16,
            fontWeight: 'bold'
        }
    }

});

export default ChangePassword;
