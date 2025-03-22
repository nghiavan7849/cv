import React, { useCallback, useEffect, useState } from "react";
import { ScrollView, Text, View, TouchableOpacity, StyleSheet } from "react-native";
import axios from "axios";
import { useFocusEffect } from "@react-navigation/native";
import AsyncStorage from "@react-native-async-storage/async-storage";
import className from "twrnc";
import MenuTop from "./menu/MenuTop";
import {ApiUrl} from "../service/ApiUrl";
import Icon from "react-native-vector-icons/FontAwesome";
import { useNavigation } from "@react-navigation/native"; // Import useNavigation
import { SafeAreaView } from "react-native-safe-area-context";
const AddressScreen = () => {
  const [taiKhoan, setTaiKhoan] = useState({});
  const [token, setToken] = useState("");
  const [addresses, setAddresses] = useState([]);
  const navigation = useNavigation(); // Hook for navigation

  async function getToken() {
    try {
      const token = await AsyncStorage.getItem("sessionTokenTK");
      return token != null ? token : null;
    } catch (error) {
      console.error("Error retrieving token:", error);
    }
  }

  async function getTaiKhoan() {
    try {
      const sessionTaiKhoan = await AsyncStorage.getItem("sessionTaiKhoan");
      return sessionTaiKhoan ? JSON.parse(sessionTaiKhoan) : null;
    } catch (error) {
      console.error("Error retrieving taiKhoan:", error);
    }
  }

  // Lấy danh sách địa chỉ của người dùng
  const fetchAddresses = async () => {
    try {
      const maTaiKhoan = taiKhoan.maTaiKhoan;
      const token = await AsyncStorage.getItem("sessionTokenTK");

      if (!maTaiKhoan) {
        console.error("No 'maTaiKhoan' found.");
        return;
      }

      if (token) {
        const response = await axios.get(
          `${ApiUrl}/api/dia-chi/list-by-matk?maTaiKhoan=${maTaiKhoan}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        if (response.data.data && Array.isArray(response.data.data.diaChi)) {
          setAddresses(response.data.data.diaChi);
        } else {
          console.error("Dữ liệu địa chỉ không hợp lệ.");
        }
      } else {
        console.error("No token found.");
      }
    } catch (error) {
      console.error("Error fetching address list:", error.response ? error.response.data : error);
    }
  };

  useFocusEffect(
    useCallback(() => {
      const initialize = async () => {
        const token = await getToken();
        const taiKhoan = await getTaiKhoan();

        if (taiKhoan && taiKhoan.maTaiKhoan && token) {
          setTaiKhoan(taiKhoan);
          setToken(token);
        } else {
          console.log("Thông tin tài khoản hoặc token bị thiếu.");
        }
      };

      initialize();
    }, [])
  );

  useEffect(() => {
    if (token && taiKhoan.maTaiKhoan) {
      fetchAddresses();
    }
  }, [token, taiKhoan]);
  // const formatAddress = (address) => {
  //   // Nếu địa chỉ là chuỗi dạng "Tên, Quận, Thành phố", bạn có thể chia nó ra.
  //   const parts = address.split(", ");
  //   return parts.map((part, index) => (
  //     <Text key={index} style={className`text-gray-700`}>
  //       {part}
  //     </Text>
  //   ));
  // };

  return (
    <SafeAreaView style={styles.container}>
      <MenuTop title="Danh sách địa chỉ" />
      <ScrollView showsVerticalScrollIndicator={false}>
        {addresses.length > 0 ? (
          addresses.map((address, index) => (
            <View
              key={index}
              style={className`bg-white rounded-lg p-4 mb-8 flex-row items-center top-7`}
            >
              <View style={className`flex-1 basis-2/12`}>
                {/* {formatAddress(address.diaChi)} */}
                <View style={className`ml-1 `}>
                  <Text style={className``}><Text style={className`text-lg font-bold  text-[#000000]`}>{address.hoVaTen} </Text></Text>
                  <Text style={className`text-base font-bold   text-[#000000]`}>(+84) {address.soDienThoai}</Text>
                  <Text style={className`text-sm text-[#000000] pr-2`} numberOfLines={3} ellipsizeMode='taill'>{address.diaChiChiTiet + ', ' + address.diaChi}</Text>
                </View>
                {address.trangThai ? (
                  <View style={className`mt-2 bg-gray-200 px-2 py-1 rounded-md w-18`}>
                    <Text style={className`text-gray-600 text-xs`}>Mặc định</Text>
                  </View>
                ) : (<></>)}


              </View>
              {/* Edit Button - Navigate to UpdateAddress.js */}
              <View style={className` basis-2/14`}>
                <TouchableOpacity
                  style={className`p-2`}
                  onPress={() => navigation.navigate("UpdateAddress", { address })}

                >
                  <Icon name="edit" size={24} color="#e74c3c" />
                </TouchableOpacity>
              </View>
            </View>
          ))
        ) : (
          <Text style={className`text-center text-gray-500 mt-4`}>
            Không có địa chỉ nào được tìm thấy.
          </Text>
        )}
      </ScrollView>
      <TouchableOpacity
          style={styles.saveButton}
          onPress={() => navigation.navigate("AddAddress")} // Dẫn đến trang thêm địa chỉ
        >
          <Text style={className`text-white text-center font-bold text-lg`}>
            Thêm địa chỉ mới
          </Text>
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
    left: 20,
    right: 20,
    backgroundColor: '#28a745', // Green color
    paddingVertical: 14,
    borderRadius: 8,
    alignItems: 'center',
  },
});
export default AddressScreen;
