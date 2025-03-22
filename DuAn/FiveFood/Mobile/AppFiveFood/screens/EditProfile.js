import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Alert,
  Image,
} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import { SafeAreaView } from 'react-native-safe-area-context';
import * as ImagePicker from 'react-native-image-picker';
import MenuTop from '../components/menu/MenuTop';
import axios from 'axios';
import { ApiUrl } from '../service/ApiUrl';
import AsyncStorage from "@react-native-async-storage/async-storage";
import Toast from "react-native-toast-message";
import className from 'twrnc';

const EditProfileScreen = ({ navigation }) => {
  const [hoVaTen, setHoVaTen] = useState('');
  const [soDienThoai, setSoDienThoai] = useState('');
  const [email, setEmail] = useState('');
  const [token, setToken] = useState('');
  const [taiKhoan, setTaiKhoan] = useState('');
  const [gioiTinh, setGioiTinh] = useState(true);
  const [hinhAnh, setHinhAnh] = useState(null);
  const [tenDangNhap, setTenDangNhap] = useState(''); // Để lưu tên đăng nhập

  const editProfile = (data, token) => {
    return axios.put(`${ApiUrl}/api/tai-khoan/edit-profile`, data,
      {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      }
    );

  }



  useEffect(() => {
    const loadPage = async () => {
      const sessionTaiKhoanStored = JSON.parse(await AsyncStorage.getItem('sessionTaiKhoan'));
      const sessionToken = await AsyncStorage.getItem('sessionTokenTK');
      setToken(sessionToken);
      setTaiKhoan(sessionTaiKhoanStored)
      console.log(sessionTaiKhoanStored)
      setEmail(sessionTaiKhoanStored.email)
      setHinhAnh(sessionTaiKhoanStored.hinhAnh)
      setGioiTinh(sessionTaiKhoanStored.gioiTinh)
      setHoVaTen(sessionTaiKhoanStored.hoVaTen)
      setSoDienThoai(sessionTaiKhoanStored.soDienThoai)
      if (res) {
        const data = res.data;
        console.log(data)
      }
    }
    loadPage();

  }, []);

  const handleChooseImage = () => {
    ImagePicker.launchImageLibrary(
      {
        mediaType: 'photo',
        includeBase64: true,
      },
      (response) => {
        if (response.didCancel) {
          console.log('Người dùng hủy chọn ảnh');
        } else if (response.errorMessage) {
          Alert.alert('Lỗi', 'Không thể chọn ảnh: ' + response.errorMessage);
        } else {
          setHinhAnh(response.assets[0]); // Lưu ảnh đã chọn
        }
      }
    );
  };

  const handleSave = async () => {
    console.log('taiKhoan', taiKhoan)
    const data = {
      "hoVaTen": hoVaTen,
      "tenDangNhap": taiKhoan.tenDangNhap,
      "email": email,
      "soDienThoai": soDienThoai,
      "gioiTinh": gioiTinh

    }
    console.log("data: ", data)
    console.log("token: ", token)
    try {
      let res = await editProfile(data, token);
      if (res) {
        Toast.show({ type: 'success', text1: "Cập nhật thành công" ,text1Style: className`text-lg`});
      } else {
        Toast.show({ type: 'error', text1: "Cập nhật thất bại" ,text1Style: className`text-lg`});
      }
    } catch (error) {
      console.log('error', error)
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <MenuTop title="Chỉnh sửa thông tin cá nhân" />

      {/* Avatar */}
      <View style={styles.avatarContainer}>
        {hinhAnh ? (
          <Image source={{ uri: hinhAnh }} style={styles.avatar} />
        ) : (
          <View style={styles.avatar}>
            <Icon name="person" size={60} color="#fff" />
          </View>
        )}

      </View>

      {/* Form Input */}
      <View style={styles.formContainer}>
        <Text style={styles.label}>Họ và Tên</Text>
        <TextInput
          style={styles.input}
          value={hoVaTen}
          onChangeText={setHoVaTen}
          placeholder="Nhập họ và tên"
        />

        <Text style={styles.label}>Email</Text>
        <TextInput
          style={styles.input}
          value={email}
          onChangeText={setEmail}
          keyboardType="email-address"
          placeholder="Nhập email"
        />

        <Text style={styles.label}>Số điện thoại</Text>
        <TextInput
          style={styles.input}
          value={soDienThoai}
          onChangeText={setSoDienThoai}
          keyboardType="phone-pad"
          placeholder="Nhập số điện thoại"
        />

        {/* Radio Button Giới Tính */}
        <Text style={styles.label}>Giới tính</Text>
        <View style={styles.radioGroup}>
          <TouchableOpacity
            style={styles.radioOption}
            onPress={() => setGioiTinh(true)}>
            <Icon
              name={gioiTinh === true ? 'radio-button-on' : 'radio-button-off'}
              size={20}
              color="#34C759"
            />
            <Text style={styles.radioLabel}>Nam</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={styles.radioOption}
            onPress={() => setGioiTinh(false)}>
            <Icon
              name={gioiTinh === false ? 'radio-button-on' : 'radio-button-off'}
              size={20}
              color="#34C759"
            />
            <Text style={styles.radioLabel}>Nữ</Text>
          </TouchableOpacity>
        </View>

        {/* Save Button */}
        <TouchableOpacity style={styles.saveButton} onPress={handleSave}>
          <Text style={styles.saveButtonText}>Lưu</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
};

export default EditProfileScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F5F0DC',
    paddingHorizontal: 20,
  },
  avatarContainer: {
    alignItems: 'center',
    marginVertical: 20,
  },
  avatar: {
    width: 120,
    height: 120,
    borderRadius: 60,
    backgroundColor: '#F28C8C',
    justifyContent: 'center',
    alignItems: 'center',
  },
  editIcon: {
    position: 'absolute',
    bottom: 0,
    right: 0,
    width: 36,
    height: 36,
    borderRadius: 18,
    backgroundColor: '#34C759',
    justifyContent: 'center',
    alignItems: 'center',
  },
  formContainer: {
    flex: 1,
  },
  label: {
    fontSize: 14,
    color: '#333',
    marginBottom: 8,
    fontWeight: '600',
  },
  input: {
    height: 45,
    borderColor: '#ddd',
    borderWidth: 1,
    borderRadius: 10,
    paddingHorizontal: 12,
    marginBottom: 15,
    backgroundColor: '#fff',
  },
  radioGroup: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 15,
  },
  radioOption: {
    flexDirection: 'row',
    alignItems: 'center',
    marginRight: 20,
  },
  radioLabel: {
    marginLeft: 8,
    fontSize: 14,
    color: '#333',
  },
  saveButton: {
    backgroundColor: '#34C759',
    paddingVertical: 14,
    borderRadius: 10,
    alignItems: 'center',
    marginTop: 20,
  },
  saveButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '600',
  },
});