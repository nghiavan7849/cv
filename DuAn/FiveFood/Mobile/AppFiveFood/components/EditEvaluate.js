import React, { useState, useEffect, useRef } from 'react';
import { View, Text, Image, TextInput, FlatList, Animated, Platform, TouchableOpacity, ScrollView, SafeAreaView } from 'react-native';
import Icon from 'react-native-vector-icons/MaterialIcons';
import tw from 'twrnc';
import axios from 'axios';
import Toast from "react-native-toast-message";
import MenuTop from "./menu/MenuTop";
import { useNavigation } from '@react-navigation/native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {ApiUrl} from '../service/ApiUrl';
import { formatVND } from '../service/formatVND';
// import * as ImagePicker from 'expo-image-picker';
// import { storage } from './Firebase';

const EditEvaluate = ({ route }) => {
  const [evaluate, setEvaluate] = useState({});
  const [product, setProduct] = useState({});
  const [images, setImages] = useState([{ file: null, url: null }, { file: null, url: null }, { file: null, url: null }, { file: null, url: null }]);
  const [soLuong, setSoLuong] = useState(0);
  const [tongTien, setTongTien] = useState(0);
  const [imageInputVisibility, setImageInputVisibility] = useState([true, false, false, false]);
  const scrollViewRef = useRef();
  const navigation = useNavigation();
  const { maDanhGia, maSanPham } = route.params;
  const [token, setToken] = useState('');
  const [taiKhoan, setTaiKhoan] = useState({});

  useEffect(() => {
    const fetchEvaluateData = async () => {
      try {
        const sessionToken = await AsyncStorage.getItem('sessionTokenTK');
        const sessionTaiKhoanStored = await AsyncStorage.getItem('sessionTaiKhoan');
        const response = await axios.get(`${ApiUrl}/api/danh-gia/get-one?maDanhGia=${maDanhGia}`, {
          headers: { 'Authorization': `Bearer ${sessionToken}` },
        });
        if (sessionTaiKhoanStored) {
          setToken(sessionToken);
          const sessionTaiKhoan = JSON.parse(sessionTaiKhoanStored);
          setTaiKhoan(sessionTaiKhoan);
        } else {
          setTaiKhoan("");
        }

        const data = response.data.data.danhGia;
        if (data) {
          setEvaluate(data);
          setProduct(data.sanPhamDG);
          setSoLuong(data.sanPhamDG.soLuong);
          setTongTien(data.sanPhamDG.gia * 1);

          // Lấy hình ảnh đánh giá
          const imageResponse = await axios.get(`${ApiUrl}/api/hinh-anh-danh-gia/page/1?maDanhGia=${maDanhGia}`, {
            headers: { 'Authorization': `Bearer ${sessionToken}` },
          });
          const imageData = imageResponse.data.data.hinhAnhDG;
          if (imageData && imageData.length > 0) {
            const newImages = imageData.map((img) => ({
              file: null,
              url: `${img.tenHinhAnh}`,
              maHinhAnhDG: img.maHinhAnhDG,
            }));
            setImages(newImages);
          }
          // Thiết lập khả năng hiển thị của hình ảnh đầu vào
          const newVisibility = Array(4).fill(false);
          if (imageData.length < 4) {
            newVisibility[imageData.length] = true;
          }
          setImageInputVisibility(newVisibility);
        } else {
          console.error('Data or sanPhamDG is not an object:', data);
        }
      } catch (error) {
        console.error('Error fetching evaluate data', error);
      }
    };
    fetchEvaluateData();
  }, [maDanhGia, maSanPham]);


  const handleFormSubmit = async () => {
    let maTaiKhoan = taiKhoan.maTaiKhoan;
    if (!maTaiKhoan) {
      console.log('Không tìm thấy thông tin tài khoản');
      return;
    }

    const reviewData = {
      maDanhGia,
      soSao: evaluate.soSao || 0,
      binhLuan: evaluate.binhLuan || '',
      maSanPham: parseInt(product.maSanPham),
      maTaiKhoan: maTaiKhoan,
     
    }; 
    console.log('maTaiKhoan: maTaiKhoan', maTaiKhoan)
    console.log('product.maSanPham', product.maSanPham)
    console.log('evaluate.binhLuan', evaluate.binhLuan)
    console.log('evaluate.soSao', evaluate.soSao)
    try {
      // Gửi đánh giá cập nhật
      const response = await axios.put(`${ApiUrl}/api/danh-gia/update`, reviewData, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });
      if (response.status === 200) {
        const imageUploadPromises = images.map(async (image) => {
          if (image && image.file) {
            // Nếu hình ảnh đã có URL, xóa ảnh cũ trên Firebase
            if (image.url) {
              const oldImageRef = ref(storage, image.url);
              await deleteObject(oldImageRef).catch((error) => {
                console.error('Error deleting old image:', error);
              });
            }
            // Tải ảnh lên Firebase
            const storageRef = ref(storage, `/AnhDanhGia/${image.file.fileName || uuidv4()}`);
            const snapshot = await uploadBytes(storageRef, image.file.uri);
            const newImageUrl = await getDownloadURL(snapshot.ref);

            // Thêm ảnh mới vào hệ thống thông qua API
            const formData = new FormData();
            formData.append('file', image.file);
            formData.append('url', newImageUrl); // Đường dẫn ảnh mới

            // Nếu ảnh có ID (để cập nhật), thì gửi yêu cầu PUT
            if (image.maHinhAnhDG) {
              await axios.put(`${ApiUrl}/api/hinh-anh-danh-gia/update?maDanhGia=${maDanhGia}&maHinhAnhDG=${image.maHinhAnhDG}`, formData, {
                headers: {
                  'Content-Type': 'multipart/form-data',
                  'Authorization': `Bearer ${token}`,
                },
              });
            } else {
              // Nếu không có ID, gửi yêu cầu POST để tạo ảnh mới
              await axios.post(`${ApiUrl}/api/hinh-anh-danh-gia/create?maDanhGia=${maDanhGia}`, formData, {
                headers: {
                  'Content-Type': 'multipart/form-data',
                  'Authorization': `Bearer ${token}`,
                },
              });
            }
          }
        });
      //   // Đợi tất cả hình ảnh được tải lên
        await Promise.all(imageUploadPromises);
      //   // Cập nhật thành công
        Toast.show({ type: 'success', text1: "Cập nhật thành công" });
        setTimeout(() => {
          navigation.navigate('ProductDetail', { maSanPham: product.maSanPham, check: true });
        }, 1000);
      } else {
        Toast.show({ type: 'error', text1: "Cập nhật thất bại" });
      }
    } catch (error) {
      console.error('Error updating evaluate or uploading images', error);
      Toast.show({ type: 'error', text1: "Cập nhật thất bại" });
    }
  };

  const handleFileChange = async (index) => {
    const { status } = await ImagePicker.requestMediaLibraryPermissionsAsync();
    if (status !== 'granted') {
      alert('Ứng dụng cần quyền truy cập thư viện ảnh để tiếp tục.');
      return;
    }
    // Mở thư viện ảnh
    const result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.Images, // Chỉ chọn ảnh
      allowsEditing: true, // Cho phép chỉnh sửa ảnh
      aspect: [4, 3], // Tỉ lệ cắt ảnh (tuỳ chọn)
      quality: 1, // Chất lượng ảnh
    });

    if (!result.canceled) {
      if (index !== undefined) {
        // Thay thế ảnh tại index nếu có
        setImages((prevImages) => {
          const newImages = [...prevImages];
          newImages[index] = { file: result.assets[0], url: result.assets[0].uri }; 
          return newImages;
        });
      } else {
        // Nếu không có index, thêm ảnh mới vào mảng
        setImages((prevImages) => [...prevImages, result.assets[0].uri]);
      }
    }
  }

  const removeImage = (index) => {
    setImages((prevImages) => prevImages.filter((_, i) => i !== index)); // Xóa ảnh tại index
  };

  // if (__DEV__) {
  //   console.error = () => {};  // Tắt tất cả lỗi console
  // }

  return (
    <SafeAreaView style={tw`flex-1 bg-[#F5F0DC]`} >
      {/* Header */}
      <MenuTop title={'Chỉnh sửa'} />
      {/* <KeyboardAvoidingView
        style={tw`flex-1`}
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'} > */}
      <ScrollView ref={scrollViewRef} showsVerticalScrollIndicator={false}>
        <View style={tw`pl-2.5 flex-1 justify-between px-4`}>
          <Text style={tw`font-bold text-xl`}></Text>
          {/* <View style={tw`mt-5`}> */}
          <View style={tw`flex-row justify-between mr-3`}>
            <Image
              source={{ uri: product.hinhAnh ? `${product.hinhAnh}` : 'default_image_url' }}
              style={tw`w-12 h-12 mr-3`}
            />
            <View style={tw`flex-1 ml-2 mr-3`}>
              <Text style={tw`font-bold text-l`}>{product.tenSanPham}</Text>
              <Text style={tw`font-bold text-l text-red-500`}>
                {new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(product.gia)}
              </Text>
            </View>
          </View>

          <View style={tw`flex-row justify-center mt-1`}>
            {[1, 2, 3, 4, 5].map((star) => (
              <TouchableOpacity
                key={star}
                style={tw`mr-2`}
                onPress={() => setEvaluate({ ...evaluate, soSao: star })}
              >
                <Text style={tw`text-3xl text-yellow-400`}>{star <= evaluate.soSao ? '★' : '☆'}</Text>
              </TouchableOpacity>
            ))}
          </View>

          <Text style={tw`text-lg font-bold`}>Hình ảnh</Text>
          <View style={tw`flex-row flex-wrap`}>
            {/* Hiển thị tất cả ảnh đã chọn */}
            {images.map((imageUri, index) => (
              <View>
                <TouchableOpacity
                  key={index}
                  style={tw`w-20 h-20 bg-gray-100 rounded-lg items-center justify-center border border-dashed border-gray-300`}
                  onPress={() => handleFileChange(index)} // Thay đổi ảnh tại index
                >
                  <Image
                    source={{ uri: imageUri.file ? URL.createObjectURL(imageUri.file) : imageUri?.url}}
                    style={tw`w-full h-full rounded-lg`}
                    resizeMode="cover"
                  />
                  {/* Nút xóa ảnh */}
                  <TouchableOpacity
                    style={tw`absolute top-0 right-0 bg-red-500 rounded-full w-6 h-6 items-center justify-center`}
                    onPress={() => removeImage(index)}
                  >
                    <Text style={tw`text-white text-xs`}>X</Text>
                  </TouchableOpacity>
                </TouchableOpacity>
              </View>
            ))}
            {images.length < 4 && (
              <TouchableOpacity onPress={() => handleFileChange()}>
                <View style={tw`w-20 h-20 bg-gray-100 rounded-lg items-center justify-center border border-dashed border-gray-300`}>
                  <Text style={tw`text-center text-gray-500`}>Chọn thêm ảnh</Text>
                </View>
              </TouchableOpacity>
            )}
          </View>
          <TextInput
            style={tw`h-30 bg-gray-100 p-2 mt-5`}
            multiline
            numberOfLines={5}
            placeholder="Nội dung"
            value={evaluate.binhLuan}
            onChangeText={(text) => setEvaluate({ ...evaluate, binhLuan: text })}
          />
        </View>
        <View style={tw`h-80`}></View>
      </ScrollView>
      {/* </KeyboardAvoidingView> */}
      <View style={tw`flex-row justify-between ml-3 mr-3 mt-1`}>
        <TouchableOpacity
          style={tw`mr-3 bg-gray-300 py-2 px-3 h-15 flex-1 justify-center`}
          onPress={() => navigation.goBack()}
        >
          <Text style={tw`text-black font-bold text-center`}>Hủy</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={tw`bg-[#28a745] py-2 px-3 flex-1 justify-center`}
          onPress={handleFormSubmit}
        >
          <Text style={tw`text-white font-bold text-center`}>Lưu</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
};

export default EditEvaluate;
