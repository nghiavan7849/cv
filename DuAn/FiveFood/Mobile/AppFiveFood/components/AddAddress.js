import React, { useState, useEffect } from 'react';
import { View, Text, TextInput, TouchableOpacity, Switch, FlatList, Modal, StyleSheet } from 'react-native';
import tw from 'twrnc';
import Icon from 'react-native-vector-icons/MaterialIcons';
import MenuTop from "./menu/MenuTop";
import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';
import { ApiUrl } from '../service/ApiUrl';
import { SafeAreaView } from "react-native-safe-area-context";
const listProvinces = [
  { provinceID: 220, ProvinceName: "Cần Thơ" },
  // Add other provinces if needed
];

const AddAddressScreen = ({ navigation }) => {
  const [hoVaTen, setHoVaTen] = useState('');
  const [soDienThoai, setSoDienThoai] = useState('');
  const [diaChi, setDiaChi] = useState('');
  const [diaChiCuThe, setDiaChiCuThe] = useState("");
  const [defaultAddress, isDefaultAddress] = useState(false);
  const [token, setToken] = useState('');
  const [taiKhoan, setTaiKhoan] = useState({});
  const [selectDistrict, setSelectDistrict] = useState(null);
  const [selectWard, setSelectWard] = useState(null);
  const [wards, setWards] = useState([]);
  const [provinces, setProvinces] = useState([]);
  const [selectProvince, setSelectProvince] = useState(null);
  const [showDistrictModal, setShowDistrictModal] = useState(false);
  const [showWardModal, setShowWardModal] = useState(false);

  const listDistrict = [
    { DistrictID: 1574, DistrictName: "Quận Cái Răng" },
    { DistrictID: 1572, DistrictName: "Quận Ninh Kiều" },
  ];

  const getToken = async () => {
    try {
      const token = await AsyncStorage.getItem("sessionTokenTK");
      return token != null ? token : null;
    } catch (error) {
      console.error("Error retrieving token:", error);
    }
  };

  const getTaiKhoan = async () => {
    try {
      const sessionTaiKhoan = await AsyncStorage.getItem("sessionTaiKhoan");
      return sessionTaiKhoan ? JSON.parse(sessionTaiKhoan) : null;
    } catch (error) {
      console.error("Error retrieving taiKhoan:", error);
    }
  };

  const getWard = async (DistrictID) => {
    try {
      const res = await axios.get(`https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=${DistrictID}`, {
        headers: {
          "Token": "bab9fb8c-2323-11ef-a951-76299e96dead"
        }
      });
      setWards(res.data.data);
    } catch (error) {
      console.log(error);
    }
  };

  const getProvinces = async () => {
    try {
      const res = await axios.get("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province", {
        headers: {
          "Token": "bab9fb8c-2323-11ef-a951-76299e96dead"
        }
      });
      return res.data.data;
    } catch (error) {
      console.log(error);
      return [];
    }
  };

  const getCombinedProvinces = async () => {
    const provincesFromApi = await getProvinces();
    return [...listProvinces, ...provincesFromApi];
  };

  const handleDistrictSelect = (district) => {
    setSelectDistrict(district);
    getWard(district.DistrictID);
    setShowDistrictModal(false);
  };

  const handleWardSelect = (ward) => {
    setSelectWard(ward);
    console.log('Selected Ward:', ward); // Log selected ward
    setShowWardModal(false);
  };

  const createAddress = async () => {
    try {
      const token = await getToken();
      const taiKhoan = await getTaiKhoan();

      if (!token || !taiKhoan) {
        alert("Token hoặc tài khoản không hợp lệ.");
        return;
      }

      // Kiểm tra tính hợp lệ của dữ liệu đầu vào
      if (!hoVaTen || !soDienThoai) {
        alert("Vui lòng điền đầy đủ họ tên và số điện thoại.");
        return;
      }

      if (!selectProvince || !selectDistrict || !selectWard) {
        alert("Vui lòng chọn đầy đủ tỉnh, quận và xã.");
        return;
      }
      let diaChiCong = `${selectWard.WardName}, ${selectDistrict.DistrictName}, ${selectProvince.ProvinceName}`;

      // Tạo đối tượng newAddress
      const newAddress = {
        "hoVaTen": hoVaTen,
        "soDienThoai": soDienThoai,
        "trangThai": defaultAddress,
        "diaChi": diaChiCong,
        "diaChiChiTiet": diaChiCuThe,
        "maTinhThanh": selectProvince.provinceID,
        "maQuanHuyen": selectDistrict.DistrictID,
        "maXaPhuong": selectWard.WardCode,
        "maTaiKhoan": taiKhoan.maTaiKhoan
      };

      console.log("Dữ liệu gửi lên:", newAddress);

      // Gửi yêu cầu đến API
      const response = await axios.post(`${ApiUrl}/api/dia-chi/create`, newAddress, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.data.status === "success") {
        alert("Thêm địa chỉ mới thành công!");
        navigation.goBack(); // Quay lại màn hình trước
      } else {
        alert(`Thêm địa chỉ thất bại: ${response.data.message || "Có lỗi xảy ra"}`);
      }
    } catch (error) {
      console.error("Lỗi khi thêm địa chỉ:", error.response ? error.response.data : error.message);
      alert(`Có lỗi xảy ra: ${error.response ? error.response.data.message : error.message}`);
    }
  };


  const handleSwitchChange = (value) => {
    setIsDefault(value);
    console.log('Updated isDefault:', value);
  };

  useEffect(() => {
    const initialize = async () => {
      const token = await getToken();
      const taiKhoan = await getTaiKhoan();
  
      if (taiKhoan && taiKhoan.maTaiKhoan && token) {
        setTaiKhoan(taiKhoan);
        setToken(token);
      } else {
        console.log("Thông tin tài khoản hoặc token bị thiếu.");
      }
  
      // Cố định tỉnh Cần Thơ
      setSelectProvince({ provinceID: 220, ProvinceName: "Cần Thơ" });
    };
  
    initialize();
  }, []);


  const renderDistrictModal = () => (
    <Modal
      visible={showDistrictModal}
      transparent={true}
      animationType="slide"
    >
      <View style={tw`flex-1 justify-center bg-black bg-opacity-50`}>
        <View style={tw`bg-white rounded-lg m-4`}>
          <Text style={tw`text-lg text-center p-4`}>Chọn Quận</Text>
          <FlatList
            data={listDistrict}
            keyExtractor={(item) => item.DistrictID.toString()}
            renderItem={({ item }) => (
              <TouchableOpacity
                onPress={() => handleDistrictSelect(item)}
                style={tw`px-4 py-3 border-b ${selectDistrict?.DistrictID === item.DistrictID ? 'bg-gray-200' : ''}`}>
                <Text>{item.DistrictName}</Text>
              </TouchableOpacity>
            )}
          />
          <TouchableOpacity onPress={() => setShowDistrictModal(false)} style={tw`p-4`}>
            <Text style={tw`text-center text-red-500`}>Đóng</Text>
          </TouchableOpacity>
        </View>
      </View>
    </Modal>
  );

  const renderWardModal = () => (
    <Modal
      visible={showWardModal}
      transparent={true}
      animationType="slide"
    >
      <View style={tw`flex-1 justify-center bg-black bg-opacity-50`}>
        <View style={tw`bg-white rounded-lg m-4`}>
          <Text style={tw`text-lg text-center p-4`}>Chọn Xã</Text>
          <FlatList
            data={wards}
            keyExtractor={(item) => item.WardID ? item.WardID.toString() : Math.random().toString()}
            renderItem={({ item }) => (
              <TouchableOpacity
                onPress={() => handleWardSelect(item)}
                style={tw`px-4 py-3 border-b ${selectWard?.WardID === item.WardID ? 'bg-gray-200' : ''}`}>
                <Text>{item.WardName}</Text>
              </TouchableOpacity>
            )}
          />
          <TouchableOpacity onPress={() => setShowWardModal(false)} style={tw`p-4`}>
            <Text style={tw`text-center text-red-500`}>Đóng</Text>
          </TouchableOpacity>
        </View>
      </View>
    </Modal>
  );

  return (
    <SafeAreaView style={styles.container}>
      <MenuTop title="Thêm địa chỉ" />
      <FlatList
        data={[]}
        keyExtractor={() => 'dummy'}
        renderItem={() => null}
        showsVerticalScrollIndicator={false}
        ListHeaderComponent={
          <>
            <Text style={tw`text-gray-500 text-sm mb-4`}>Thông tin liên hệ</Text>
            <View style={tw`bg-white rounded-lg px-4 py-3 mb-6`}>
              <TextInput
                style={tw`text-base`}
                placeholder="Tên"
                value={hoVaTen}
                onChangeText={setHoVaTen}
              />
            </View>
            <View style={tw`bg-white rounded-lg px-4 py-3 mb-6`}>
              <TextInput
                style={tw`text-base`}
                placeholder="Số điện thoại"
                value={soDienThoai}
                onChangeText={setSoDienThoai}
                keyboardType="phone-pad"
              />
            </View>

            <Text style={tw`text-gray-500 text-sm mb-4`}>Thông tin địa chỉ</Text>

            <TouchableOpacity onPress={() => setSelectProvince({ provinceID: 220, ProvinceName: "Cần Thơ" })}>
              <View style={tw`bg-white rounded-lg flex-row items-center px-4 py-3 mb-4`}>
                <Text style={tw`flex-1 text-base`}>{selectProvince ? selectProvince.ProvinceName : "Cần Thơ"}</Text>
              </View>

            </TouchableOpacity>

            <TouchableOpacity onPress={() => setShowDistrictModal(true)} style={tw`bg-white rounded-lg flex-row items-center px-4 py-3 mb-4`}>
              <Text style={tw`flex-1 text-base`}>{selectDistrict ? selectDistrict.DistrictName : "Chọn quận"}</Text>
              <Icon name="arrow-drop-down" size={24} color="#4a4a4a" />
            </TouchableOpacity>

            {renderDistrictModal()}

            <TouchableOpacity onPress={() => setShowWardModal(true)} style={tw`bg-white rounded-lg flex-row items-center px-4 py-3 mb-4`}>
              <Text style={tw`flex-1 text-base`}>{selectWard ? selectWard.WardName : "Chọn xã"}</Text>
              <Icon name="arrow-drop-down" size={24} color="#4a4a4a" />
            </TouchableOpacity>

            {renderWardModal()}

            {/* <View style={tw`bg-white rounded-lg flex-row items-center px-4 py-3 mb-6`}>
              <Icon name="location-on" size={20} color="#4a4a4a" style={tw`mr-2`} />
              <TextInput
                style={tw`flex-1 text-base`}
                placeholder="Địa chỉ"
                value={diaChi}
                onChangeText={setDiaChi}
              />
            </View> */}
            <View style={tw`bg-white rounded-lg px-4 py-3 mb-6`}>
              <TextInput
                style={tw`text-base`}
                placeholder="Nhập các chi tiết khác (không bắt buộc)"
                value={diaChiCuThe}
                onChangeText={setDiaChiCuThe}
              />
            </View>
            <Text style={tw`text-gray-500 text-sm mb-2`}>Cài đặt</Text>
            <View style={tw`flex-row items-center justify-between bg-white rounded-lg px-4 py-3 mb-4`}>
              <Text style={tw`text-base`}>Đặt làm mặc định</Text>
              <Switch
                value={defaultAddress}
                onChange={() => { isDefaultAddress(!defaultAddress) }}
                checked={defaultAddress}
              />
            </View>

          </>
        }
        contentContainerStyle={tw`pb-25 `}
      />
      <TouchableOpacity style={styles.saveButton} onPress={createAddress}>
        <Text style={tw`text-white text-center font-bold text-lg`}>Lưu địa chỉ</Text>
      </TouchableOpacity>
    </SafeAreaView>
  );
};
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F5F0DC',
    paddingHorizontal: 16,
  },
  saveButton: {
    position: 'absolute',
    bottom: 20, // Adjust as needed
    left: 15,
    right: 15,
    backgroundColor: '#28a745', // Green color
    paddingVertical: 14,
    borderRadius: 8,
    alignItems: 'center',
  },
});
export default AddAddressScreen;