import React, { useState } from "react";
import { ScrollView, StyleSheet, Text, TextInput, TouchableOpacity, View, ActivityIndicator, Alert } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import { SafeAreaView } from "react-native-safe-area-context";
import axios from "axios";
import  {ApiUrl}  from '../service/ApiUrl';
import MenuTop from "./menu/MenuTop";

const Register = ({ navigation }) => {
    const [hoVaTen, setHoVaTen] = useState("");
    const [tenDangNhap, setTenDangNhap] = useState("");
    const [soDienThoai, setSoDienThoai] = useState("");
    const [email, setEmail] = useState("");
    const [matKhau, setMatKhau] = useState("");
    const [xacNhanMatKhau, setXacNhanMatKhau] = useState("");
    const [checkXemMK, isCheckXemMK] = useState(true);
    const [checkXemXNMK, isCheckXemXNMK] = useState(true);
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");

    const getRegister = async (data) => {
        return axios.post(`${ApiUrl}/api/tai-khoan/create`, data); // Thay <your_ip_address> bằng IP hoặc domain của API server.
    };

    const checkValidate = () => {
        if (!hoVaTen.trim() || !tenDangNhap.trim() || !email.trim() || !soDienThoai.trim() || !matKhau.trim() || !xacNhanMatKhau.trim()) {
            setMessage("Vui lòng điền đầy đủ thông tin.");
            return false;
        }
        if (matKhau !== xacNhanMatKhau) {
            setMessage("Mật khẩu và xác nhận mật khẩu không khớp.");
            return false;
        }
        if (!/^\S+@\S+\.\S+$/.test(email)) {
            setMessage("Email không hợp lệ.");
            return false;
        }
        if (!/^\d{10}$/.test(soDienThoai)) {
            setMessage("Số điện thoại phải có 10 chữ số.");
            return false;
        }
        return true;
    };

    const clickDangKy = async () => {
        if (!checkValidate()) return;

        const data = { hoVaTen, tenDangNhap, matKhau, email, soDienThoai };
        setLoading(true);
        setMessage("");
        try {
            let res = await getRegister(data);
            if (res.data.status === "error") {
                setMessage(res.data.message || "Có lỗi xảy ra.");
            } else {
                Alert.alert("Thông báo", "Đăng ký thành công!", [
                    { text: "OK", onPress: () => navigation.navigate("Login") },
                ]);
            }
        } catch (error) {
            console.error("Error:", error);
            setMessage("Lỗi kết nối, vui lòng thử lại.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <SafeAreaView style={styles.container}>
            <MenuTop title="Đăng ký"/>
            <ScrollView style={styles.body}>
                {/* <Text style={styles.title}>Đăng Ký</Text> */}
                {message && <Text style={styles.error}>{message}</Text>}
                {/* Họ và Tên */}
                <View style={styles.group}>
                    <Text style={styles.label}>Họ và Tên</Text>
                    <TextInput
                        style={styles.input}
                        placeholder="Họ và Tên"
                        value={hoVaTen}
                        onChangeText={setHoVaTen}
                    />
                </View>
                {/* Tên Đăng Nhập */}
                <View style={styles.group}>
                    <Text style={styles.label}>Tên Đăng Nhập</Text>
                    <TextInput
                        style={styles.input}
                        placeholder="Tên Đăng Nhập"
                        value={tenDangNhap}
                        onChangeText={setTenDangNhap}
                    />
                </View>
                {/* Số Điện Thoại */}
                <View style={styles.group}>
                    <Text style={styles.label}>Số Điện Thoại</Text>
                    <TextInput
                        style={styles.input}
                        placeholder="Số Điện Thoại"
                        value={soDienThoai}
                        onChangeText={setSoDienThoai}
                        keyboardType="numeric"
                    />
                </View>
                {/* Email */}
                <View style={styles.group}>
                    <Text style={styles.label}>Email</Text>
                    <TextInput
                        style={styles.input}
                        placeholder="Email"
                        value={email}
                        onChangeText={setEmail}
                        keyboardType="email-address"
                    />
                </View>
                {/* Mật Khẩu */}
                <View style={styles.group}>
                    <Text style={styles.label}>Mật Khẩu</Text>
                    <View style={styles.passwordContainer}>
                        <TextInput
                            style={styles.input}
                            placeholder="Mật Khẩu"
                            value={matKhau}
                            onChangeText={setMatKhau}
                            secureTextEntry={checkXemMK}
                        />
                        <TouchableOpacity
                            style={styles.eyePass}
                            onPress={() => isCheckXemMK(!checkXemMK)}
                        >
                            <Ionicons name={checkXemMK ? "eye-off-outline" : "eye-outline"} size={24} />
                        </TouchableOpacity>
                    </View>
                </View>
                {/* Xác Nhận Mật Khẩu */}
                <View style={styles.group}>
                    <Text style={styles.label}>Xác Nhận Mật Khẩu</Text>
                    <View style={styles.passwordContainer}>
                        <TextInput
                            style={styles.input}
                            placeholder="Xác Nhận Mật Khẩu"
                            value={xacNhanMatKhau}
                            onChangeText={setXacNhanMatKhau}
                            secureTextEntry={checkXemXNMK}
                        />
                        <TouchableOpacity
                            style={styles.eyePass}
                            onPress={() => isCheckXemXNMK(!checkXemXNMK)}
                        >
                            <Ionicons name={checkXemXNMK ? "eye-off-outline" : "eye-outline"} size={24} />
                        </TouchableOpacity>
                    </View>
                    
                </View>
            </ScrollView>
            {/* Nút Đăng Ký */}
            <TouchableOpacity
                    style={styles.buttonRegister}
                    onPress={clickDangKy}
                    disabled={loading}
                >
                    {loading ? (
                        <ActivityIndicator color="#fff" />
                    ) : (
                        <Text style={styles.textRegister}>Đăng Ký</Text>
                    )}
                </TouchableOpacity>
        </SafeAreaView>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: "#F5F0DC",
        paddingHorizontal: 16,
    },
    body: {
        marginTop: 15,
    },
    title: {
        fontSize: 24,
        fontWeight: "bold",
        textAlign: "center",
        marginBottom: 20,
    },
    group: {
        marginBottom: 15,
    },
    label: {
        fontSize: 16,
        marginBottom: 5,
    },
    input: {
        height: 50,
        backgroundColor: "#fff",
        borderRadius: 8,
        paddingHorizontal: 10,
        borderWidth: 1,
        borderColor: "#ccc",
    },
    passwordContainer: {
        position: 'relative',
    },
    eyePass: {
        position: "absolute",
        right: 10,
        bottom: 12
    },
    error: {
        color: "red",
        textAlign: "center",
        marginBottom: 10,
    },
    buttonRegister: {
        height: 55,
        backgroundColor: "#53A654",
        justifyContent: "center",
        alignItems: "center",
        borderRadius: 10,
        marginBottom: 20,
    },
    textRegister: {
        color: "#fff",
        fontSize: 18,
    },
});

export default Register;
