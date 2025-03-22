import React, { useState, useEffect, useCallback } from 'react';
import { View, Text, TextInput, TouchableOpacity, Switch, FlatList, Modal, StyleSheet } from 'react-native';
import tw from 'twrnc';
import Icon from 'react-native-vector-icons/MaterialIcons';
import MenuTop from "./menu/MenuTop";
import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';
import { ApiUrl } from '../service/ApiUrl';
import { SafeAreaView } from "react-native-safe-area-context";
import { useFocusEffect } from '@react-navigation/native';

const listDistrict = [
  { DistrictID: 1574, DistrictName: "Quận Cái Răng" },
  { DistrictID: 1572, DistrictName: "Quận Ninh Kiều" },
];

const UpdateAddressScreen = ({ navigation, route }) => {
  const { address } = route.params; // Nhận địa chỉ từ params

  const [hoVaTen, setHoVaTen] = useState(address.hoVaTen);
  const [soDienThoai, setSoDienThoai] = useState(address.soDienThoai);
  const [diaChi, setDiaChi] = useState(address.diaChi);
  const [diaChiCuThe, setDiaChiCuThe] = useState(address.diaChiChiTiet);
  const [defaultAddress, setDefaultAddress] = useState(address.trangThai);
  const [selectDistrict, setSelectDistrict] = useState(
    { "DistrictID": "", "DistrictName": "" }
  );
  const [selectWard, setSelectWard] = useState(
    { "WardID": "", "WardName": "" }
  );
  const [wards, setWards] = useState([]);
  const [selectProvince] = useState({ provinceID: 220, ProvinceName: "Cần Thơ" });
  const [showDistrictModal, setShowDistrictModal] = useState(false);
  const [showWardModal, setShowWardModal] = useState(false);

  useFocusEffect(
    useCallback(() => {
      loadPage();
    }, [])
  );
  const loadPage = async () => {
    if (address.maQuanHuyen === 1574) {
      setSelectDistrict({ "DistrictID": 1754, "DistrictName": "Quận Cái Răng" })
    } else {
      setSelectDistrict({ "DistrictID": 1572, "DistrictName": "Quận Ninh Kiều" })
    }
    try {
      const res = await axios.get(`https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=`+address.maQuanHuyen, {
        headers: {
          "Token": "bab9fb8c-2323-11ef-a951-76299e96dead"
        }
      });
      res.data.data.forEach(item => {
        
        console.log('item.WardCode', item.WardCode)
        if(address.maXaPhuong === parseInt(item.WardCode)){
          setSelectWard({ "WardID": item.WardCode, "WardName": item.WardName });
        }
      });
      // console.log(res.data.data);

    } catch (error) {
      console.log(error);
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

  const handleDistrictSelect = (district) => {
    setSelectDistrict({ "DistrictID": district.DistrictID, "DistrictName": district.DistrictName})
    getWard(district.DistrictID);
    setShowDistrictModal(false);
  };

  const handleWardSelect = (ward) => {
    setSelectWard({ "WardID": ward.WardCode, "WardName": ward.WardName });
    setShowWardModal(false);
  };

  const updateAddress = async () => {

    const token = await AsyncStorage.getItem("sessionTokenTK");
    const taiKhoan = JSON.parse(await AsyncStorage.getItem("sessionTaiKhoan"));

    if (!token || !taiKhoan) {
      alert("Token hoặc tài khoản không hợp lệ.");
      return;
    }

    // Kiểm tra tính hợp lệ của dữ liệu đầu vào
    if (!hoVaTen || !soDienThoai) {
      alert("Vui lòng điền đầy đủ họ tên và số điện thoại.");
      return;
    }

    if (!selectDistrict || !selectWard) {
      alert("Vui lòng chọn đầy đủ quận và xã.");
      return;
    }
    let diaChiCong = `${selectWard.WardName}, ${selectDistrict.DistrictName}, ${selectProvince.ProvinceName}`;
    const updatedAddress = {
      "maDiaChi": address.maDiaChi, // ID địa chỉ hiện tại để cập nhật
      "hoVaTen": hoVaTen,
      "soDienThoai": soDienThoai,
      "trangThai": defaultAddress,
      "diaChi": diaChiCong,
      "diaChiChiTiet": diaChiCuThe,
      "maTinhThanh": selectProvince.provinceID,
      "maQuanHuyen": selectDistrict.DistrictID,
      "maXaPhuong": selectWard.WardID,
      "maTaiKhoan": taiKhoan.maTaiKhoan
    };
    try {
      const response = await axios.put(`${ApiUrl}/api/dia-chi/update`, updatedAddress, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.data.status === "success") {
        alert("Cập nhật địa chỉ thành công!");
        navigation.goBack();
      } else {
        alert(`Cập nhật địa chỉ thất bại: ${response.data.message || "Có lỗi xảy ra"}`);
      }
    } catch (error) {
      console.log('error', error)
      console.error("Lỗi khi cập nhật địa chỉ:", error.response ? error.response.data : error.message);
      alert(`Có lỗi xảy ra: ${error}`);
    }
  };

  const renderDistrictModal = () => (
    <Modal visible={showDistrictModal} transparent={true} animationType="slide">
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
    <Modal visible={showWardModal} transparent={true} animationType="slide">
      <View style={tw`flex-1 justify-center bg-black bg-opacity-50`}>
        <View style={tw`bg-white rounded-lg m-4`}>
          <Text style={tw`text-lg text-center p-4`}>Chọn Xã</Text>
          <FlatList
            data={wards}
            keyExtractor={(item) => item.WardID?.toString() || Math.random().toString()}
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
      <MenuTop title="Cập nhật địa chỉ" />
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

            <TouchableOpacity onPress={() => { }} style={tw`bg-white rounded-lg flex-row items-center px-4 py-3 mb-4`}>
              <Text style={tw`flex-1 text-base`}>{selectProvince.ProvinceName}</Text>
            </TouchableOpacity>

            <TouchableOpacity onPress={() => setShowDistrictModal(true)} style={tw`bg-white rounded-lg flex-row items-center px-4 py-3 mb-4`}>
              {/* <Text style={tw`flex-1 text-base`}>{selectDistrict ? selectDistrict.DistrictName : "Chọn quận"}</Text> */}
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
                onValueChange={() => setDefaultAddress(!defaultAddress)}
              />
            </View>
          </>
        }

      />
      <TouchableOpacity style={styles.saveButton} onPress={updateAddress}>
        <Text style={tw`text-white text-center font-bold text-lg`}>Cập nhật địa chỉ</Text>
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
    bottom: 20,
    left: 15,
    right: 15,
    backgroundColor: '#28a745',
    paddingVertical: 14,
    borderRadius: 8,
    alignItems: 'center',
  },
});

export default UpdateAddressScreen;