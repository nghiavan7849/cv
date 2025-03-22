import React, { useRef, useState } from "react";
import { Image, StyleSheet, Text, TextInput, TouchableOpacity, View, Keyboard, TouchableWithoutFeedback, ScrollView } from "react-native";
import MenuTop from "./menu/MenuTop";
import className from 'twrnc';
import { Ionicons } from '@expo/vector-icons';
import axios from "axios";
import AsyncStorage from "@react-native-async-storage/async-storage";
import Toast from "react-native-toast-message";
import { jwtDecode } from "jwt-decode";
import { SafeAreaView } from "react-native-safe-area-context";
import {ApiUrl} from '../service/ApiUrl';

import { storage } from '../service/ConfigFirebase'; // Đường dẫn đến tệp cấu hình
import { ref, uploadBytes, getDownloadURL, getStorage } from 'firebase/storage';
import * as FileSystem from 'expo-file-system';
import { Asset } from 'expo-asset';

const Login = ({ navigation }) => {
    const [checkXemMK, isCheckXemMK] = useState(true);
    const inputRefTDN = useRef(null);
    const inputRefMK = useRef(null);

    const getLogin = (tenDangNhap, matKhau) => {
        return axios.get(`${ApiUrl}/api/get-tai-khoan?tenDangNhap=${tenDangNhap}&matKhau=${matKhau}`);
    }

    const [tenDangNhap, setTenDangNhap] = useState("");
    const [matKhau, setMatKhau] = useState("");
    const [message, setMessage] = useState("");

    const handleLogin = async () => {
        try {
            let res = await getLogin(tenDangNhap, matKhau);

            if (res.data.status === 'error') {
                await AsyncStorage.removeItem('sessionTaiKhoan');
                setMessage(res.data.message);
                Toast.show({
                    type: 'error',
                    text1: 'Đăng nhập thất bại!',
                    text1Style: className`text-lg`
                });
            } else {
                Toast.show({
                    type: 'success',
                    text1: 'Đăng nhập thành công!',
                    text1Style: className`text-lg`
                });

                const token = jwtDecode(res.data.token);
                await AsyncStorage.setItem('sessionTaiKhoan', JSON.stringify(token.data.taiKhoan));
                await AsyncStorage.setItem('sessionTokenTK', res.data.token);

                console.log(res.data.token)

                setTimeout(() => {
                    navigation.reset({
                        index: 1,
                        routes: [
                            { name: 'Home' },
                            { name: 'Menu' },
                        ],
                    });
                }, 1500);
                if (!token) {
                    navigation.navigate('Login'); // Navigate to login if token doesn't exist
                }
            }
        } catch (error) {
            console.error('Lỗi đăng nhập:', error);
        }
    };


    const uploadImage = async (file, newUniqueFileName) => {
        try {
            const storageRef = ref(storage, `test/${newUniqueFileName}`);
            await uploadBytes(storageRef, file);
            console.log('Ảnh đã được tải lên thành công!');

            const imageURL = await getDownloadURL(storageRef);
            console.log('URL ảnh:', imageURL);

            return imageURL; // Trả về URL ảnh
        } catch (error) {
            console.error('Lỗi khi tải ảnh lên:', error);
        }
    };

    // const clickLoginGoogle = async () => {
    //     // Tải tệp hình ảnh từ assets
    //     const asset = Asset.fromModule(require('../assets/LogoFiveFoodXoaNen.png'));
    //     await asset.downloadAsync();

    //     //   // Lấy URI của tệp
    //     const uri = asset.localUri || asset.uri;
    //     const response = await fetch(uri);
    //     const blob = await response.blob();

    //     // Tải lên Firebase
    //     const storage = getStorage();
    //     const storageRef = ref(storage, `test/${asset.name}` + `${Date.now()}.png`); // Tạo tên tệp duy nhất

    //     try {
    //         await uploadBytes(storageRef, blob);
    //         const downloadURL = await getDownloadURL(storageRef);
    //         console.log('Ảnh đã tải lên với URL:', downloadURL);
    //     } catch (error) {
    //         console.error('Lỗi khi tải lên:', error);
    //     }

    //     console.log(asset);
    //     console.log(blob);
    //     console.log(storageRef);
    //     // uploadImage()
    // }


    return (

        <TouchableWithoutFeedback onPress={Keyboard.dismiss}>
            <SafeAreaView style={styles.container}>
                <MenuTop title='Đăng nhập' navi="Menu" />
                <ScrollView showsVerticalScrollIndicator={false}>
                    <View style={className`items-center`}>
                        <Image source={require('../assets/LogoFiveFoodXoaNen.png')} style={className`w-50 h-50`} />
                    </View>
                    <View style={styles.body}>
                        <View style={styles.group}>
                            <Text style={styles.label}>Tên đăng nhập <Text style={{ color: 'red' }}>*</Text></Text>
                            <TextInput
                                ref={inputRefTDN}
                                style={styles.input}
                                placeholder='Tên đăng nhập'
                                onChangeText={(text) => { setTenDangNhap(text); setMessage("") }}
                                onSubmitEditing={() => inputRefMK.current.focus()}
                            />
                        </View>
                        <View style={styles.group}>
                            <Text style={styles.label}>Mật khẩu <Text style={{ color: 'red' }}>*</Text></Text>
                            <TextInput
                                ref={inputRefMK}
                                style={styles.input}
                                placeholder='Mật khẩu'
                                secureTextEntry={checkXemMK}
                                onChangeText={(text) => { setMatKhau(text); setMessage(""); }}
                            />
                            <TouchableOpacity style={styles.eyePass} onPress={() => isCheckXemMK(!checkXemMK)}>
                                <Ionicons name={checkXemMK ? 'eye-off-outline' : 'eye-outline'} size={26} />
                            </TouchableOpacity>
                        </View>
                        <TouchableOpacity style={styles.forgotPassword} onPress={() => navigation.navigate('ForgotPassword')}>
                            <Text style={{ color: '#0066FF' }}>Quên mật khẩu?</Text>
                        </TouchableOpacity>
                        <Text style={className`text-red-600 text-center`}>{message}</Text>
                    </View>

                    <TouchableOpacity onPress={handleLogin} style={styles.buttonLogin}>
                        <Text style={styles.textLogin}>Đăng nhập</Text>
                    </TouchableOpacity>
                    {/* <Text style={{ textAlign: 'center', marginVertical: 10, fontSize: 16 }}>Hoặc</Text>
                    <TouchableOpacity style={[styles.buttonLoginGoogle, className`flex flex-row`]} >
                        <Image
                            source={{ uri: 'https://lh3.googleusercontent.com/COxitqgJr1sJnIDe8-jiKhxDx1FrYbtRHKJ9z_hELisAlapwE9LUPh6fcXIfb5vwpbMl4xl9H9TRFPc5NOO8Sb3VSgIBrfRYvW6cUA' }}
                            style={styles.logoGoogle}
                        />
                        <Text style={styles.textLoginGoogle}>Đăng nhập bằng Google</Text>
                    </TouchableOpacity> */}
                    <Text style={{ textAlign: 'center', fontSize: 16, marginTop: 20 }}>Bạn chưa có tài khoản? </Text>
                    <Text style={{ textAlign: 'center', fontSize: 16, marginTop: 2 }}>Hãy đăng ký 1 tài khoản cho bản thân nhé!</Text>
                    <TouchableOpacity onPress={() => navigation.navigate('Register')} style={styles.buttonRegister}>
                        <Text style={styles.textRegister}>Đăng ký</Text>
                    </TouchableOpacity>
                </ScrollView>
            </SafeAreaView>
        </TouchableWithoutFeedback>
    );
}
const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#F5F0DC',
        paddingHorizontal: 16
    },
    body: {
        marginTop: 10,
        minHeight: 100
    },
    group: {
        marginTop: 5,
        marginHorizontal: 15,
        minHeight: 90
    },
    label: {
        fontSize: 16
    },
    input: {
        position: 'relative',
        borderRadius: 10,
        height: 50,
        backgroundColor: '#fff',
        marginTop: 5,
        paddingLeft: 10,
        paddingRight: 45
    },
    eyePass: {
        position: 'absolute',
        right: 10,
        bottom: 25
    },
    error: {
        fontSize: 14,
        color: 'red',
        marginTop: 2,
        marginBottom: -5
    },
    forgotPassword: {
        alignItems: 'flex-end',
        marginHorizontal: 15,
    },
    buttonLogin: {
        height: 55,
        backgroundColor: '#53A654',
        justifyContent: 'center',
        alignItems: 'center',
        borderRadius: 10,
        marginTop: 10,
        marginHorizontal: 15,
    },
    textLogin: {
        color: '#fff',
        fontSize: 20
    },
    buttonLoginGoogle: {
        height: 55,
        backgroundColor: '#F5F0DC',
        justifyContent: 'center',
        alignItems: 'center',
        marginHorizontal: 15,
        borderRadius: 10,
        borderWidth: 1,
        borderColor: '#BBBBBB'
    },
    textLoginGoogle: {
        lineHeight: 55,
        color: '#000',
        fontSize: 20,
    },
    logoGoogle: {
        height: 30,
        width: 30,
        marginLeft: 10,
        marginRight: 10,
    },
    buttonRegister: {
        height: 55,
        backgroundColor: '#F5F0DC',
        justifyContent: 'center',
        alignItems: 'center',
        borderRadius: 10,
        borderWidth: 1,
        borderColor: '#007bff',
        marginHorizontal: 15,
        marginTop: 10
    },
    textRegister: {
        fontSize: 20,
        color: '#007bff',
    }
});
export default Login;
