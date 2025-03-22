import React, { useState, useEffect, useRef } from 'react';
import { View, Text, Image, TextInput, FlatList, Animated, Platform, TouchableOpacity, ScrollView, SafeAreaView } from 'react-native';
import Icon from 'react-native-vector-icons/MaterialIcons';
import tw from 'twrnc'; import axios from 'axios';
import Toast from "react-native-toast-message";
import MenuTop from "./menu/MenuTop";
import { useNavigation } from '@react-navigation/native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {ApiUrl} from '../service/ApiUrl';
import { formatVND } from '../service/formatVND';
import * as ImagePicker from 'expo-image-picker';
import { v4 as uuidv4 } from 'uuid';
// import { getDownloadURL, ref, uploadBytes } from '@react-native-firebase/storage';
import 'react-native-get-random-values';
import { storage } from '../service/ConfigFirebase'; // Đường dẫn đến tệp cấu hình
import { ref, uploadBytes, getDownloadURL, getStorage } from 'firebase/storage';



const Evaluate = ({ route }) => {
  const [evaluate, setEvaluate] = useState({});
  const [product, setProduct] = useState({});
  const [images, setImages] = useState([null, null, null, null]);
  const [content, setContent] = useState('');
  const [rating, setRating] = useState(null);
  const [message, setMessage] = useState('');
  const [imageInputVisibility, setImageInputVisibility] = useState([true, false, false, false]);
  const [token, setToken] = useState('');
  const navigation = useNavigation();
  const [taiKhoan, setTaiKhoan] = useState({});
  const { maHoaDon, maSanPham } = route.params;
  const scrollViewRef = useRef();
  const [imageResultChoose, setImageResultChoose] = useState([]);
  // const maHoaDon = 20;

  useEffect(() => {
    const fetchEvaluateData = async () => {
      try {
        const sessionTaiKhoanStored = await AsyncStorage.getItem('sessionTaiKhoan');
        const sessionToken = await AsyncStorage.getItem('sessionTokenTK');
        console.log('sessionToken', sessionToken)
        if (sessionTaiKhoanStored) {
          setToken(sessionToken);
          const sessionTaiKhoan = JSON.parse(sessionTaiKhoanStored);
          setTaiKhoan(sessionTaiKhoan);
        } else {
          setTaiKhoan("");
        }
      } catch (error) {
        console.error('Lỗi khi lấy sessionTaiKhoan:', error);
      }
    };
    fetchEvaluateData();
    fetchProductDetails(maSanPham);
  }, [maSanPham]);

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

    const newImages = [...images];
    // newImages[index] = { file: result.assets[0], url: result.assets[0].uri }; // Replace image at index
    const file = result.assets[0];
    newImages[index] = file;
    setImages(newImages);
    setImageResultChoose(newImages);

    if (index < imageInputVisibility.length - 1) {
      const newVisibility = [...imageInputVisibility];
      newVisibility[index + 1] = true; // Mở ô tiếp theo
      setImageInputVisibility(newVisibility);
    } else {
      // Thêm một ô mới nếu đã đến ô cuối cùng
      setImageInputVisibility((prev) => [...prev, true]);
      setImages((prev) => [...prev, null]);
    }
  };

  const updateImageFirebase = async (result) => {
    const response = await fetch(result.uri);
    const blob = await response.blob();

    // Tải lên Firebase
    const storage = getStorage();
    const storageRef = ref(storage, `test/${uuidv4()}-${result.fileName}`); // Tạo tên tệp duy nhất

    try {
      await uploadBytes(storageRef, blob);
      const downloadURL = await getDownloadURL(storageRef);
      return downloadURL;
    } catch (error) {
      console.error('Lỗi khi tải lên:', error);
    }
  }

  const fetchProductDetails = async (maSanPham) => {
    try {
      const response = await axios.get(`${ApiUrl}/api/san-pham/get-one?maSanPham=${maSanPham}`);
      const data = response.data;
      if (data && data.data) {
        setProduct(data.data.sanPham);
      }
    } catch (error) {
      console.error('Error fetching product details:', error);
    }
  };

  const handleFormSubmit = async () => {
    // if (!evaluate.soSao) {
    //   Toast.show({ type: 'error', text1: "Vui lòng chọn sao đánh giá" });
    //   return;
    // }
    // if (!evaluate.binhLuan) {
    //   Toast.show({ type: 'error', text1: "Vui lòng nhập nội dung" });
    //   return;
    // }
    // let maTaiKhoan = taiKhoan.maTaiKhoan;
    // if (!maTaiKhoan) {
    //   Toast.show({ type: 'error', text1: "Không tìm thấy thông tin tài khoản" });
    //   return;
    // }

    const reviewData = {
      soSao: evaluate.soSao || 0,
      binhLuan: evaluate.binhLuan,
      maSanPham: maSanPham,
      maTaiKhoan: taiKhoan.maTaiKhoan,
      maHoaDon: maHoaDon
    };

    console.log('rating', evaluate.soSao || 0)
    console.log('maSanPham', maSanPham)
    console.log('maTaiKhoan', taiKhoan.maTaiKhoan)
    console.log('binhLuan: content,', evaluate.binhLuan,)
    console.log('maHoaDon', maHoaDon)
    try {
      const reviewResponse = await axios.post(`${ApiUrl}/api/danh-gia/create`, reviewData, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      const maDanhGia = reviewResponse.data.data.danhGia.maDanhGia;
      console.log(maDanhGia);
      if (imageResultChoose !== null) {
        for (const result of imageResultChoose) {
          if (result !== null) {
            const urlFire = await updateImageFirebase(result);
            console.log('urlFire', urlFire)

            const formData = new FormData();
            formData.append('url', urlFire);
            formData.append('maDanhGia', maDanhGia);

            try {
              axios.post(`${ApiUrl}/api/hinh-anh-danh-gia/create`, formData, {
                headers: {
                  'Content-Type': 'multipart/form-data',
                  Authorization: `Bearer ${token}`,
                },
              });
            } catch (error) {
              console.log('error', error)
            }
          }
        }
      }
      Toast.show({ type: 'success', text1: "Đánh giá thành công" });
      navigation.navigate('ProductDetail', { maSanPham: maSanPham });
    } catch (error) {
      console.error('Error submitting review or uploading images:', error);
      Toast.show({ type: 'error', text1: "Đánh giá không thành công" });
    }
  };

  const removeImage = (index) => {
    setImages((prevImages) => prevImages.filter((_, i) => i !== index)); // Xóa ảnh tại index
  };

  // if (__DEV__) {
  //   console.error = () => {};  // Tắt tất cả lỗi console
  // }

  return (
    <SafeAreaView style={tw`flex-1 bg-[#F5F0DC]`} >
      {/* Header */}
      <MenuTop title={'Đánh giá'} />
      <ScrollView ref={scrollViewRef} showsVerticalScrollIndicator={false}>
        <View style={tw`pl-2.5 flex-1 justify-between px-4`}>
          <Text style={tw`font-bold text-xl`}></Text>
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
            {[0, 1, 2, 3].map((i) => (
              imageInputVisibility[i] && (
                <View key={i} style={tw`mr-1 items-center`}>
                  <TouchableOpacity
                    style={tw`w-20 h-20 border border-gray-300 rounded-lg justify-center items-center`}
                    onPress={() => handleFileChange(i)}
                  >
                    {images[i] ? (
                      <Image
                        source={{ uri: images[i].uri }}
                        style={tw`w-full h-full rounded-lg`}
                        resizeMode="cover"
                      />
                    ) : (
                      <Text style={tw`text-gray-500`}>Chọn ảnh</Text>
                    )}
                    {/* Nút xóa ảnh */}
                    <TouchableOpacity
                      style={tw`absolute top-0 right-0 bg-red-500 rounded-full w-6 h-6 items-center justify-center`}
                      onPress={() => removeImage(i)}
                    >
                      <Text style={tw`text-white text-xs`}>X</Text>
                    </TouchableOpacity>
                  </TouchableOpacity>
                </View>
              )
            ))}
            {/* {images.length < 4 && (
              <TouchableOpacity onPress={() => handleFileChange()}>
                <View style={tw`w-20 ml-1 h-20 bg-gray-100 rounded-lg items-center justify-center border border-dashed border-gray-300`}>
                  <Text style={tw`text-center text-gray-500`}>Chọn thêm ảnh</Text>
                </View>
              </TouchableOpacity>
            )} */}
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
          <Text style={tw`text-white font-bold text-center`}>Đánh giá</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
};

export default Evaluate;
